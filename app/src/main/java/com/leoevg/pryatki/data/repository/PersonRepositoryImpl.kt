package com.leoevg.pryatki.data.repository

import com.leoevg.pryatki.data.PersonDao
import com.leoevg.pryatki.data.PersonEntity
import com.leoevg.pryatki.domain.models.Person
import com.leoevg.pryatki.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonRepositoryImpl(
    private val dao: PersonDao
): PersonRepository {

    override suspend fun insertPerson(person: Person) {
        dao.insertItem(person.toPersonEntity())
    }

    override suspend fun deletePerson(person: Person) {
        dao.deleteItem(person.toPersonEntity()) // <-- Конвертирует в Entity
    }

    override fun getPersons(): Flow<List<Person>> {
        return dao.getAllPersons().map { entities ->
            entities.map { it.toPerson() }
        }
    }

    override suspend fun getPersonsByName(name: String): Person? {
        return dao.getPersonByName(name)?.toPerson()
    }

    override suspend fun getUsedImages(): List<String> {
        return dao.getUsedImages()
    }

    override suspend fun updatePerson(person: Person) {
        dao.updateItem(person.toPersonEntity())
    }


    // Мапперы из Entity в Модель дб в модель домена и наоборот
    private fun PersonEntity.toPerson(): Person {
        return Person(
            id = this.id,
            name = this.name,
            count = this.count,
            image = this.image
        )
    }

    private fun Person.toPersonEntity(): PersonEntity {
        return PersonEntity(
            id = this.id,
            name = this.name,
            count = this.count,
            image = this.image
        )
    }
}