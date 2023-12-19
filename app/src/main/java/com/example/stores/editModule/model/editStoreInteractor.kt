package com.example.stores.editModule.model

import androidx.lifecycle.LiveData
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity

class editStoreInteractor {

    fun getStoreById(id:Long):LiveData<StoreEntity>{
        return  StoreApplication.database.storeDao().getStoreById(id)

    }

    fun saveStore(storeEntity: StoreEntity, callback: (Long) -> Unit){

        Thread {
            val newId = StoreApplication.database.storeDao().addStore(storeEntity)// se añade eñ "store.id" para notificar los cambios de favoritos
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            callback(newId)//pasarle el estado
        }.start()
    }

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){

        Thread {
            StoreApplication.database.storeDao().updateStore(storeEntity)// se añade eñ "store.id" para notificar los cambios de favoritos
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            callback(storeEntity)//pasarle el estado
        }.start()
    }
}