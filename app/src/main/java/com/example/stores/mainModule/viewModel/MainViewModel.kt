package com.example.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.mainModule.model.mainInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.suspendCoroutine

class MainViewModel : ViewModel() {

    //private val storesList: MutableLiveData<List<StoreEntity>> = MutableLiveData()// Esta propiedad nos ayudara a reflejar los datos en la vista
    private var interactor: mainInteractor = mainInteractor()
    private var storeslists: MutableList<StoreEntity> = mutableListOf()


    private val storesList: MutableLiveData<List<StoreEntity>> by lazy {

        MutableLiveData<List<StoreEntity>>()
    }

    // crear una funcion que puede accesar a este valor
    //No recibe parametro pero devuelve un dato de LiveData configurado de la misma manera que "Stores"
    fun getStores(): LiveData<List<StoreEntity>> {
        return storesList.also {//Inicializamos la variable "storesList" y al mismo tiempo inicializamos "loadStores"
            loadStores()
        }
    }

    // Se crea una funcion que pueda alimentar ese arreglo
    private fun loadStores() {
//        interactor.getStoresCallback(object : mainInteractor.StoresCallback{
//            override fun getStoresCallBack(stores: MutableList<StoreEntity>) {
//                storesList.postValue(stores)
//            }
//        })

        interactor.getStores {
            storesList.postValue(it)
            storeslists = it
        }
    }
     fun deleteStore(storeEntity: StoreEntity){
        interactor.deleteStore(storeEntity){
            val index = storeslists.indexOf(storeEntity)
            if (index != -1) {
                storeslists.removeAt(index)
                storesList.postValue(storeslists)
            }
        }
    }
    fun updateStore(storeEntity: StoreEntity){
        storeEntity.isFavorite = !storeEntity.isFavorite
        interactor.updateStore(storeEntity){
            val index = storeslists.indexOf(storeEntity)
            if (index != -1) {
                storeslists[index]= storeEntity
                storesList.postValue(storeslists)
            }
        }
    }
}