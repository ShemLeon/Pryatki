package com.leoevg.pryatki.domain.repository

import com.leoevg.pryatki.data.storage.PersonEntity
import com.leoevg.pryatki.domain.models.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun insertPerson(person: Person)
    suspend fun deletePerson(person: Person)
    fun getPersons(): Flow<List<Person>>
    suspend fun getPersonsByName(name: String): Person?
    suspend fun getUsedImages(): List<String>
    suspend fun updatePerson(person: Person)
}