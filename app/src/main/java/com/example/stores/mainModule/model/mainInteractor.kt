package com.example.stores.mainModule.model

import androidx.lifecycle.LiveData
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class mainInteractor {

//    interface StoresCallback {
//        fun getStoresCallBack(stores: MutableList<StoreEntity>)
//    }
//
//    fun getStoresCallback(callback: StoresCallback) {// recibe datos de la interface
//
//        Thread {
//            val storeList = StoreApplication.database.storeDao().getAllStores()
//            callback.getStoresCallBack(storeList)//pasarle el estado
//        }.start()
//
//        GlobalScope.launch(Dispatchers.IO) {
//            val storeList = StoreApplication.database.storeDao().getAllStores()
//            callback.getStoresCallBack(storeList)
//        }
//    }

    fun getStores(callback: (MutableList<StoreEntity>)->Unit){//Manera mas corta de consulta de datos

        Thread {
            val storeList = StoreApplication.database.storeDao().getAllStores()
            callback(storeList)//pasarle el estado
        }.start()
    }
    fun deleteStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){

        Thread {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            callback(storeEntity)//pasarle el estado
        }.start()
    }
    fun updateStore(storeEntity: StoreEntity,callback: (StoreEntity) -> Unit){

        Thread {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            callback(storeEntity)//pasarle el estado
        }.start()
    }
}