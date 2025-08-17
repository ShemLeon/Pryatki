package com.leoevg.pryatki.presenter.screens.main

import com.leoevg.pryatki.data.PersonEntity

sealed interface MainScreenEvent {
    data class OnTextChange(val text: String): MainScreenEvent
    object OnAddClick: MainScreenEvent
    data class OnItemClick(val item: PersonEntity): MainScreenEvent
    data class OnIncrement(val item: PersonEntity): MainScreenEvent
    data class OnDecrement(val item: PersonEntity): MainScreenEvent
    data class OnDelete(val item: PersonEntity): MainScreenEvent
}