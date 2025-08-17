package com.leoevg.pryatki.domain.usecase

import com.leoevg.pryatki.domain.models.Person
import com.leoevg.pryatki.domain.repository.PersonRepository

class IncrementCountUseCase(private val repository: PersonRepository) {
    suspend fun execute(person: Person) {
        repository.updatePerson(person.copy(count = person.count + 1))
    }
}

