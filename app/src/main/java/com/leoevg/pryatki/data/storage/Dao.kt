package com.leoevg.pryatki.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.leoevg.pryatki.data.storage.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(personEntity: PersonEntity)

    @Update
    suspend fun updateItem(personEntity: PersonEntity)

    @Delete
    suspend fun deleteItem(personEntity: PersonEntity)

    @Query("SELECT * FROM name_table")
    fun getAllPersons(): Flow<List<PersonEntity>>

    @Query("SELECT COUNT(*) FROM name_table WHERE name = :name")
    suspend fun getCountByName(name: String): Int

    @Query("SELECT * FROM name_table WHERE name = :name LIMIT 1")
    suspend fun getPersonByName(name: String): PersonEntity?

    // Repository нуждается в этом методе для проверки дублирования имён
    @Query("SELECT DISTINCT image FROM name_table")
    suspend fun getUsedImages(): List<String>


}