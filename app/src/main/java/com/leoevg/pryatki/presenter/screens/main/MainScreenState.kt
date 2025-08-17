package com.leoevg.pryatki.presenter.screens.main

import com.leoevg.pryatki.data.PersonEntity

data class MainScreenState(
    val items: List<PersonEntity> = emptyList(),
    val inputText: String = "",
    val errorMessage: String? = null,
    val editingItem: PersonEntity? = null
)