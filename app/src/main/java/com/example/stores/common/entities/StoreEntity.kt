package com.example.stores.common.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "StoresEntity", indices = arrayOf(Index(value = ["name"], unique = true)))
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var phone: String,
    var webSite: String = "",
    var photoUrl: String,
    var isFavorite: Boolean = false
) {

    constructor() : this(name = "", phone = "", photoUrl = "")

    //Se reemplaza en la clase STORLISTADAPTER
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (javaClass != other?.javaClass) return false
//
//            other as StoreEntity
//
//            if (id != other.id) return false
//
//            return true
//        }
//        override fun hashCode(): Int {
//            return id.hashCode()
//
//    }
}
