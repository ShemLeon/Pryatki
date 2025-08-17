package com.leoevg.pryatki.domain.usecase

import com.leoevg.pryatki.domain.models.Person
import com.leoevg.pryatki.domain.repository.PersonRepository

class DecrementCountUseCase(private val repository: PersonRepository) {
    suspend fun execute(param: Person) {
        repository.updatePerson(param.copy(count = param.count + 1))
    }
}