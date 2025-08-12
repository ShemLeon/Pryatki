package com.leoevg.pryatki.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "name_table")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val name: String,
    val count: Int,
    val image: Int
)