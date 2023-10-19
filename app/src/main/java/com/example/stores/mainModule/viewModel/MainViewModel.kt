package com.example.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.mainModule.model.mainInteractor
import java.util.concurrent.LinkedBlockingQueue

class MainViewModel : ViewModel() {

    private var stores: MutableLiveData<List<StoreEntity>> = MutableLiveData()// Esta propiedad nos ayudara a reflejar los datos en la vista
    private var interactor: mainInteractor = mainInteractor()

    init {
        //stores = MutableLiveData()
        loadStores()
    }

    // crear una funcion que puede accesar a este valor
    //No recibe parametro pero devuelve un dato de LiveData configurado de la misma maneta que "Stores"
    fun getStores(): LiveData<List<StoreEntity>> {
        return stores
    }

    // Se crea una funcion que pueda alimentar ese arreglo
    private fun loadStores() {
        interactor.getStoresCallback(object : mainInteractor.StoresCallback {

            override fun getStoresCallBack(stores: MutableList<StoreEntity>) {
                this@MainViewModel.stores.value = stores
            }
        })
    }
}