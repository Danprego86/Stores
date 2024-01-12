package com.example.stores.mainModule.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.common.utils.Constans
import com.example.stores.common.utils.StoreExceptions
import com.example.stores.common.utils.TypeError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class mainInteractor {
    val stores: LiveData<MutableList<StoreEntity>> = liveData {
        delay(1000)
        val storeLiveData = StoreApplication.database.storeDao().getAllStores()
        emitSource(storeLiveData.map { stores ->
            stores.sortedBy { it.name }.toMutableList()

        })
    }

    suspend fun deleteStore(storeEntity: StoreEntity) = withContext(Dispatchers.IO) {
        delay(1_500)
         val result = StoreApplication.database.storeDao().deleteStore(storeEntity)

        if (result == 0) throw StoreExceptions(typeError = TypeError.DELETE)

//        Thread {
//            StoreApplication.database.storeDao().deleteStore(storeEntity)
//            callback(storeEntity)//pasarle el estado
//        }.start()
    }

    suspend fun updateStore(storeEntity: StoreEntity) = withContext(Dispatchers.IO) {

        delay(300)
       val result = StoreApplication.database.storeDao().updateStore(storeEntity)
        if (result == 0) throw StoreExceptions(typeError = TypeError.UPDATE)
//        Thread {
//       StoreApplication.database.storeDao().updateStore(storeEntity)
//            callback(storeEntity)//pasarle el estado
//        }.start()
    }
}