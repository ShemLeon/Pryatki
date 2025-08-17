package com.leoevg.pryatki.domain.usecase

import com.leoevg.pryatki.domain.models.Person  // <-- Работаем с моделью домена
import com.leoevg.pryatki.domain.repository.PersonRepository

class DecrementCountUseCase(private val repository: PersonRepository) {
    suspend fun execute(person: Person) {       // <-- Принимаем Person
        if (person.count > 0) {
            val updatedPerson = person.copy(count = person.count - 1)
            repository.updatePerson(updatedPerson)
        }
    }
}
