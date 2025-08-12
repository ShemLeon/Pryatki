package com.leoevg.pryatki.presenter.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.leoevg.pryatki.data.MainDB
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.leoevg.pryatki.app.App
import com.leoevg.pryatki.data.PersonEntity
import kotlinx.coroutines.launch

class MainScreenViewModel(val database: MainDB): ViewModel() {

    val itemsList = database.dao.getAllItems()
    val newText = mutableStateOf("")
    var personEntity: PersonEntity? = null

    fun insertItem() = viewModelScope.launch {
        val personItem = personEntity?.copy(name = newText.value)
            ?: PersonEntity(name = newText.value, count = 0, image = 0)
        database.dao.insertItem(personItem)
        personEntity = null;
    }

    fun deleteItem(item: PersonEntity) = viewModelScope.launch {
        database.dao.deleteItem(item)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                return MainScreenViewModel(database) as T
            }
        }
    }





}


