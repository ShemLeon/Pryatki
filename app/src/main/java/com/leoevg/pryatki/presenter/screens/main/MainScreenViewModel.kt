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


class MainScreenViewModel(val database: MainDB, private val context: Context): ViewModel() {
    val itemsList = database.dao.getAllItems()
    val newText = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)
    var personEntity: PersonEntity? = null

    fun insertItem() = viewModelScope.launch {
        val name = newText.value.trim()

        // Проверка на пустое имя
        if (name.isEmpty()) {
            errorMessage.value = "Введи имя!"
            return@launch
        }

        if (personEntity != null) {
            // Редактирование существующего элемента
            val updatedItem = personEntity!!.copy(name = name)
            database.dao.updateItem(updatedItem)
        } else {
            // Проверка на дублирование имени
            val existingCount = database.dao.getCountByName(name)
            if (existingCount > 0) {
                errorMessage.value = "Персонаж с таким именем уже существует!"
                return@launch
            }

            // Получаем используемые картинки и выбираем оптимальную
            val usedImages = database.dao.getUsedImages()
            val selectedImage = ImageUtils.getUnusedImageName(context, usedImages)

            val newItem = PersonEntity(
                name = name,
                count = 0,
                image = selectedImage
            )
            database.dao.insertItem(newItem)
            errorMessage.value = null // Сбрасываем ошибку при успешном добавлении
        }

        personEntity = null
        newText.value = ""
    }

    fun deleteItem(item: PersonEntity) = viewModelScope.launch {
        database.dao.deleteItem(item)
    }

    // Минус в рейтинг
    fun decrementCount(item: PersonEntity) = viewModelScope.launch {
        if (item.count > 0) {
            val updatedItem = item.copy(count = item.count - 1)
            database.dao.updateItem(updatedItem)
        }
    }

    // Плюс в рейтинг
    fun incrementCount(item: PersonEntity) = viewModelScope.launch {
        val updatedItem = item.copy(count = item.count + 1)
        database.dao.updateItem(updatedItem)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val app = checkNotNull(extras[APPLICATION_KEY]) as App
                return MainScreenViewModel(app.database, app.applicationContext) as T
            }
        }
    }
}