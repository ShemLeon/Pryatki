package com.leoevg.pryatki.presenter.screens.main

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.leoevg.pryatki.data.MainDB
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.leoevg.pryatki.app.App
import com.leoevg.pryatki.data.PersonEntity
import com.leoevg.pryatki.utils.ImageUtils
import kotlinx.coroutines.launch
import com.leoevg.pryatki.domain.models.Person
import com.leoevg.pryatki.domain.usecase.AddPersonUseCase
import com.leoevg.pryatki.domain.usecase.DecrementCountUseCase
import com.leoevg.pryatki.domain.usecase.IncrementCountUseCase

class MainScreenViewModel(
    val database: MainDB,
    private val context: Context,
    private val decrementCountUseCase: DecrementCountUseCase,
    private val incrementCountUseCase: IncrementCountUseCase,
    private val addPersonUseCase: AddPersonUseCase,
): ViewModel() {
    val personsList = database.dao.getAllPersons()
    private val _state = mutableStateOf(MainScreenState())
    val state = _state
    val newText = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)
    var personEntity: PersonEntity? = null

    fun onEvent(event: MainScreenEvent){
        // SOLID
        when(event){
            is MainScreenEvent.OnTextChange -> {
                _state.value = _state.value.copy(
                    inputText = event.text,
                    errorMessage = null
                )
                // Для совместимости с UI (ВАЖНО!)
                newText.value = event.text
                errorMessage.value = null
            }
            MainScreenEvent.OnAddClick -> insertItem()
            is MainScreenEvent.OnItemClick -> {
                _state.value = _state.value.copy(
                    editingItem = event.item,
                    inputText = event.item.name
                )
                // Для совместимости с UI (ВАЖНО!)
                personEntity = event.item
                newText.value = event.item.name
            }
            is MainScreenEvent.OnIncrement -> incrementCount(event.item)
            is MainScreenEvent.OnDecrement -> decrementCount(event.item)
            is MainScreenEvent.OnDelete -> deleteItem(event.item)
        }
    }

    fun insertItem() = viewModelScope.launch {
        val name = newText.value.trim()
        // Логика редактирования существующего элемента пока остается здесь для простоты
        if (personEntity != null) {
            val updatedItem = personEntity!!.copy(name = name)
            database.dao.updateItem(updatedItem) // Это тоже нужно будет вынести в UseCase
            personEntity = null
            newText.value = ""
            return@launch
        }

        addPersonUseCase.execute(name)
            .onSuccess {
            // Успех: сбрасываем поле ввода и ошибку
            newText.value = ""
            errorMessage.value = null
            _state.value = _state.value.copy(errorMessage = null)
        }
            .onFailure { error ->
                // Ошибка: показываем сообщение из UseCase
                errorMessage.value = error.message
                _state.value = _state.value.copy(errorMessage = error.message)
            }
        }

    fun deleteItem(item: PersonEntity) = viewModelScope.launch {
        database.dao.deleteItem(item)
    }

    // Минус в рейтинг
    fun decrementCount(item: PersonEntity) = viewModelScope.launch {
        // Конвертируем Entity в модель домена перед вызовом UseCase
        decrementCountUseCase.execute(item.toPerson())
    }

    // Плюс в рейтинг
    fun incrementCount(item: PersonEntity) = viewModelScope.launch {
        incrementCountUseCase.execute(item.toPerson())
    }

// Mapper -
    private fun PersonEntity.toPerson(): Person {
        return Person(
            id = this.id,
            name = this.name,
            count = this.count,
            image = this.image
        )
    }
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val app = checkNotNull(extras[APPLICATION_KEY]) as App
                val repository = com.leoevg.pryatki.data.repository.PersonRepositoryImpl(app.database.dao)
                val decrementCountUseCase = DecrementCountUseCase(repository)
                val incrementCountUseCase = IncrementCountUseCase(repository)
                val addPersonUseCase = AddPersonUseCase(repository, app.applicationContext)

                return MainScreenViewModel(
                    app.database,
                    app.applicationContext,
                    decrementCountUseCase,
                    incrementCountUseCase,
                    addPersonUseCase
                ) as T
            }
        }
    }
}