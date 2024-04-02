package com.example.stores.mainModule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.common.utils.StoreExceptions
import com.example.stores.common.utils.TypeError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class mainInteractor {

    //Scope es el contexto que maneja el ciclo de vida de una corrutina
    val stores: LiveData<MutableList<StoreEntity>> = liveData {
        delay(1000)
        val storeLiveData = StoreApplication.database.storeDao().getAllStores()
        emitSource(storeLiveData.map { stores ->// se organiza con "Map"
            stores.sortedBy { it.name }.toMutableList()
        })
    }

    suspend fun deleteStore(storeEntity: StoreEntity) = withContext(Dispatchers.IO) {
        delay(1_500)
        val result = StoreApplication.database.storeDao().deleteStore(storeEntity)
        if (result == 0) throw StoreExceptions(typeError = TypeError.DELETE)
    }

    suspend fun updateStore(storeEntity: StoreEntity) = withContext(Dispatchers.IO) {

        delay(300)
        val result = StoreApplication.database.storeDao().updateStore(storeEntity)
        if (result == 0) throw StoreExceptions(typeError = TypeError.UPDATE)
    }
}