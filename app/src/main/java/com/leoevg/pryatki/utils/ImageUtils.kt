package com.leoevg.pryatki.utils


import android.content.Context
import android.util.Log

object ImageUtils {
    private var cachedImageNames: List<String>? = null

    private fun getAllWebpImages(context: Context): List<String> {
        if (cachedImageNames == null) {
            cachedImageNames = try {
                context.assets.list("images")
                    ?.filter { it.endsWith(".webp") }
                    ?.sorted() // Сортируем для предсказуемого порядка
                    ?: emptyList()
            } catch (e: Exception) {
                Log.e("ImageUtils", "Ошибка чтения assets: ${e.message}")
                // Fallback список на случай ошибки
                listOf("pikachu.webp")
            }
        }
        return cachedImageNames!!
    }

    fun getUnusedImageName(context: Context, usedImages: List<String>): String {
        val allImages = getAllWebpImages(context)

        // Если нет картинок вообще - возвращаем дефолт
        if (allImages.isEmpty()) return "pikachu.webp"

        // Ищем картинку, которой еще не было
        val unusedImages = allImages.filterNot { it in usedImages }

        return if (unusedImages.isNotEmpty()) {
            // СЛУЧАЙНО выбираем из неиспользованных
            unusedImages.random()
        } else {
            // Если все использованы - случайно выбираем любую
            allImages.random()
        }
    }

    fun getAllImageNames(context: Context): List<String> {
        return getAllWebpImages(context)
    }
}