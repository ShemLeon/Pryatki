package com.leoevg.pryatki.domain.repository

import com.leoevg.pryatki.data.PersonEntity

interface ItemRepository {
    fun insertItem()
    fun deleteItem(item: PersonEntity)
    fun decrementCount(item: PersonEntity)
    fun incrementCount(item: PersonEntity)
}