package com.example.stores.common.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.stores.common.entities.StoreEntity


@Dao
interface StoreDao {

    @Query("SELECT *FROM StoresEntity")
    fun getAllStores():MutableList<StoreEntity>

    @Query("SELECT * FROM StoresEntity WHERE id = :id")
    fun getStoreById(id:Long): StoreEntity

    @Insert
    fun addStore(storeEntity: StoreEntity):Long

    @Update
    fun updateStore(storeEntity: StoreEntity)

    @Delete
    fun deleteStore(storeEntity: StoreEntity)
}