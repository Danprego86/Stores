package com.example.stores

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.stores.common.database.StoreDatabase
import com.example.stores.common.database.StoreApi


class StoreApplication:Application() {

    companion object{//Acceso desde cualquier parte de la aplicacion
        lateinit var database: StoreDatabase
        lateinit var storeApi: StoreApi
    }

    override fun onCreate() {
        super.onCreate()

        val MIGRATION_1_2 = object : Migration(1,2){

            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE StoresEntity ADD COLUMN photoUrl TEXT NOT NULL DEFAULT ''")
            }
        }

        database= Room.databaseBuilder(this,
            StoreDatabase::class.java,
            "StoreDatabase")
            .addMigrations(MIGRATION_1_2)
            .build()

        //volley
        storeApi = StoreApi.getInstance(this)
    }

}