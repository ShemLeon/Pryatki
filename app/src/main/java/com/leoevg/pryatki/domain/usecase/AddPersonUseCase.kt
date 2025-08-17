package com.leoevg.pryatki.domain.usecase

import android.content.Context
import com.leoevg.pryatki.domain.models.Person
import com.leoevg.pryatki.domain.repository.PersonRepository
import com.leoevg.pryatki.utils.ImageUtils

class AddPersonUseCase(
    private val repository: PersonRepository,
    private val context: Context // Контекст нужен для доступа к assets
) {
    suspend fun execute(name: String): Result<Unit> {
        // 1. Проверка на пустое имя
        if (name.isBlank()) {
            return Result.failure(Exception("Имя не может быть пустым!"))
        }
        // 2. Проверка на дублирование имени
        if (repository.getPersonsByName(name) != null) {
            return Result.failure(Exception("Игрок с таким именем уже есть!"))
        }
        // 3.  Получаем используемые картинки и выбираем оптимальную
        val usedImages = repository.getUsedImages()
        val selectedImage = ImageUtils.getUnusedImageName(context, usedImages)
        // 4. Создание и вставка Person
        val newPerson = Person(
            name = name,
            count = 0,
            image = selectedImage
        )
        repository.insertPerson(newPerson)
        return Result.success(Unit)
    }
}