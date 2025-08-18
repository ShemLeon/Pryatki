package com.leoevg.pryatki.domain.usecase

import com.leoevg.pryatki.domain.models.Person
import com.leoevg.pryatki.domain.repository.PersonRepository

class UpdatePersonUseCase(private val repository: PersonRepository) {
    suspend fun execute(personToUpdate: Person, newName: String): Result<Unit> {
        // Проверяем, не пытается ли пользователь установить пустое имя
        if (newName.isBlank()) {
            return Result.failure(Exception("Имя не может быть пустым"))
        }
        val existingPerson = repository.getPersonsByName(newName)
        // Проверка на дублирование имени
        if (existingPerson != null && existingPerson.id != personToUpdate.id) {
            return Result.failure(Exception("Игрок с таким именем уже существует"))
        }

        val updatedPerson = personToUpdate.copy(name = newName)
        repository.updatePerson(updatedPerson)

        return Result.success(Unit)
    }
}