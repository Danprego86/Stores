package com.example.stores.mainModule.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.common.utils.Constans
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    fun getStores(callback: (MutableList<StoreEntity>) -> Unit) {

        val url = Constans.STORES_URL + Constans.GET_ALL_PATH

        var storeList = mutableListOf<StoreEntity>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->

           // val status = response.getInt(Constans.STATUS_PROPERTY)
            val status = response.optInt(Constans.STATUS_PROPERTY, Constans.ERROR)

            if (status == Constans.SUCCESS) {
                Log.i("status", status.toString())

                //Obtener Listado completo como en room
                val jsonList = response.optJSONArray (Constans.STORES_PROPERTY)?.toString()
                 if (jsonList!= null) {
                     val mutableListType = object : TypeToken<MutableList<StoreEntity>>() {}.type
                     storeList = Gson().fromJson(jsonList, mutableListType)
                     callback(storeList)
                     return@JsonObjectRequest
                 }
            }
            callback(storeList)
        }) {
            it.printStackTrace()
            callback(storeList)
        }
        StoreApplication.storeApi.addToRequestQueue(jsonObjectRequest)
    }

    fun getStoresRoom(callback: (MutableList<StoreEntity>) -> Unit) {//Manera mas corta de consulta de datos

        Thread {
            val storeList = StoreApplication.database.storeDao().getAllStores()
            val json = Gson().toJson(storeList)
            Log.i("Gson", json)
            callback(storeList)//pasarle el estado
        }.start()
    }

    fun deleteStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit) {

        Thread {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            callback(storeEntity)//pasarle el estado
        }.start()
    }

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit) {

        Thread {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            callback(storeEntity)//pasarle el estado
        }.start()
    }
}