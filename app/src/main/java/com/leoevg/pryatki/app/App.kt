package com.leoevg.pryatki.app

import android.app.Application
import com.leoevg.pryatki.data.MainDB

import kotlin.getValue

class App: Application() {
    val database by lazy { MainDB.createDatabase(this) }
}