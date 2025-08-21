package com.leoevg.pryatki.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leoevg.pryatki.data.storage.PersonEntity

@Database(
    entities = [
        PersonEntity::class
    ],
    version = 1,
    exportSchema = false,
    views = [
    ]
)

abstract class MainDB: RoomDatabase() {
    abstract val dao: PersonDao
    companion object{
        fun createDatabase(context: Context): MainDB{
            return Room.databaseBuilder(
                context,
                MainDB::class.java,
                "test.db"
            )
 //               .fallbackToDestructiveMigration()  // Автоматически сносит базу при изменениях
                .build()
        }
    }
}