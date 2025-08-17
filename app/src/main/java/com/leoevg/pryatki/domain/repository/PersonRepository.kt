package com.leoevg.pryatki.domain.repository

import com.leoevg.pryatki.data.PersonEntity
import com.leoevg.pryatki.domain.models.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun insertPerson(person: Person)
    suspend fun deletePerson(person: PersonEntity)
    suspend fun decrementCount(person: PersonEntity)
    suspend fun incrementCount(person: PersonEntity)
    fun getPersons(): Flow<List<Person>>
    suspend fun getPersonsByName(name: String): Person?
    suspend fun getUsedImages(): List<String>
    suspend fun updatePerson(person: Person)

}