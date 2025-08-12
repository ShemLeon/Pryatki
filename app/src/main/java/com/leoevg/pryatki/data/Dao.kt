package com.leoevg.pryatki.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(PersonEntity: PersonEntity)
    @Update
    suspend fun updateItem(PersonEntity: PersonEntity)
    @Delete
    suspend fun deleteItem(personEntity: PersonEntity)

    @Query("SELECT * FROM name_table")
    fun getAllItems(): Flow<List<PersonEntity>>
}