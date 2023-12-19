package com.example.stores.common.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.stores.common.entities.StoreEntity


@Dao
interface StoreDao {

    @Query("SELECT *FROM StoresEntity")
    fun getAllStores(): LiveData<MutableList<StoreEntity>>

    @Query("SELECT * FROM StoresEntity WHERE id = :id")
    fun getStoreById(id:Long): LiveData<StoreEntity>

    @Insert
    fun addStore(storeEntity: StoreEntity):Long

    @Update
    fun updateStore(storeEntity: StoreEntity)

    @Delete
    fun deleteStore(storeEntity: StoreEntity)
}