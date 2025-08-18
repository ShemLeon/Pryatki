package com.leoevg.pryatki.domain.usecase

import com.leoevg.pryatki.domain.models.Person  // <-- Работаем с моделью домена
import com.leoevg.pryatki.domain.repository.PersonRepository


class DeletePersonUseCase(private val repository: PersonRepository){
    suspend fun execute(person: Person){
        repository.deletePerson(person)
    }
}