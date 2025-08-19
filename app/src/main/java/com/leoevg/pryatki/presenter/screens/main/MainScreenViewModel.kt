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
import com.leoevg.pryatki.domain.usecase.DeletePersonUseCase
import com.leoevg.pryatki.domain.usecase.IncrementCountUseCase
import com.leoevg.pryatki.domain.usecase.UpdatePersonUseCase

class MainScreenViewModel(
    val database: MainDB,
    private val context: Context,
    private val decrementCountUseCase: DecrementCountUseCase,
    private val incrementCountUseCase: IncrementCountUseCase,
    private val addPersonUseCase: AddPersonUseCase,
    private val deletePersonUseCase: DeletePersonUseCase,
    private val updatePersonUseCase: UpdatePersonUseCase
) : ViewModel() {
    val personsList = database.dao.getAllPersons()
    private val _state = mutableStateOf(MainScreenState())
    val state = _state
    val newText = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)
    var personEntity: PersonEntity? = null

    fun onEvent(event: MainScreenEvent) {
        // SOLID
        when (event) {
            // 1. Когда пользователь вводит текст
            is MainScreenEvent.OnTextChange -> {
                _state.value = _state.value.copy(
                    inputText = event.text,
                    errorMessage = null
                )
                // Для совместимости с UI (ВАЖНО!)
                newText.value = event.text
                errorMessage.value = null
            }
            // 2. Когда пользователь нажимает кнопку добавить
            MainScreenEvent.OnAddClick -> insertItem()
            // 3. Когда пользователь нажимает на элемент
            is MainScreenEvent.OnItemClick -> {
                _state.value = _state.value.copy(
                    editingItem = event.item,
                    inputText = event.item.name
                )
                // Для совместимости с UI (ВАЖНО!)
                personEntity = event.item
                newText.value = event.item.name
            }
            // 4. Когда пользователь нажимает на кнопку минус
            is MainScreenEvent.OnIncrement -> incrementCount(event.item)
            // 5. Когда пользователь нажимает на кнопку плюс
            is MainScreenEvent.OnDecrement -> decrementCount(event.item)
            // 6. Когда пользователь нажимает на кнопку удалить
            is MainScreenEvent.OnDelete -> deleteItem(event.item)
        }
    }

    // Добавление игрока
    fun insertItem() = viewModelScope.launch {
        val name = newText.value.trim()
        val personToEdit = personEntity
        if (personToEdit != null) {
            // РЕЖИМ РЕДАКТИРОВАНИЯ
            updatePersonUseCase.execute(personToEdit.toPerson(), name)
                .onSuccess {
                    newText.value = ""
                    errorMessage.value = null
                    personEntity = null // Сбрасываем режим редактирования
                }
                .onFailure { error ->
                    errorMessage.value = error.message
                }
        } else {
            // РЕЖИМ ДОБАВЛЕНИЯ
            addPersonUseCase.execute(name)
                .onSuccess {
                    newText.value = ""
                    errorMessage.value = null
                }
                .onFailure { error ->
                    errorMessage.value = error.message
                }
        }
    }

    // Удаление игрока
    fun deleteItem(person: PersonEntity) = viewModelScope.launch {
        deletePersonUseCase.execute(person.toPerson())
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

    // Mapper
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
                val repository =
                    com.leoevg.pryatki.data.repository.PersonRepositoryImpl(app.database.dao)
                val decrementCountUseCase = DecrementCountUseCase(repository)
                val incrementCountUseCase = IncrementCountUseCase(repository)
                val addPersonUseCase = AddPersonUseCase(repository, app.applicationContext)
                val deletePersonUseCase = DeletePersonUseCase(repository)
                val updatePersonUseCase = UpdatePersonUseCase(repository)
                return MainScreenViewModel(
                    app.database,
                    app.applicationContext,
                    decrementCountUseCase,
                    incrementCountUseCase,
                    addPersonUseCase,
                    deletePersonUseCase,
                    updatePersonUseCase
                ) as T
            }
        }
    }
}