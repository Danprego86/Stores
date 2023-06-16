package com.example.stores

import android.app.Application
import androidx.room.Room


class StoreApplication:Application() {

    companion object{//Acceso desde cualquier parte de la aplicacion
        lateinit var database: StoreDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database= Room.databaseBuilder(this,
            StoreDatabase::class.java,
            "StoreDatabase").
        build()
    }

}