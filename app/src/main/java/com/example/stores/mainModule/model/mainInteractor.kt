package com.example.stores.mainModule.model

import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import java.util.concurrent.LinkedBlockingQueue

class mainInteractor {

    interface StoresCallback{
        fun getStoresCallBack(stores: MutableList<StoreEntity>)
    }

    fun getStoresCallback(callback: StoresCallback) {

        //val queue = LinkedBlockingQueue<MutableList<StoreEntity>>()

        Thread {
            val storeList = StoreApplication.database.storeDao().getAllStores()
            //queue.add(storeList)//pasarle el estado a "queue"
            callback.getStoresCallBack(storeList)
        }.start()


    }
}