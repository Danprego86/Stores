package com.example.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stores.common.entities.StoreEntity
import com.example.stores.common.utils.Constans
import com.example.stores.mainModule.model.mainInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    //private val storesList: MutableLiveData<List<StoreEntity>> = MutableLiveData()// Esta propiedad nos ayudara a reflejar los datos en la vista
    private var interactor: mainInteractor = mainInteractor()
    private var storeslists: MutableList<StoreEntity> = mutableListOf()


    private val showProgress: MutableLiveData<Boolean> = MutableLiveData()

//    private val storesList: MutableLiveData<MutableList<StoreEntity>> by lazy {
//
//        MutableLiveData<MutableList<StoreEntity>>().also {
//            loadStores()//Inicializamos la variable "storesList" y al mismo tiempo inicializamos "loadStores"
//        }
//    }

    private val storesList = interactor.stores

    // crear una funcion que puede accesar a este valor
    //No recibe parametro pero devuelve un dato de LiveData configurado de la misma manera que "Stores"
    fun getStores(): LiveData<MutableList<StoreEntity>> {
        return storesList
    }

    fun isShowProgress():MutableLiveData<Boolean> {
        return showProgress
    }

    // Se crea una funcion que pueda alimentar ese arreglo
//    private fun loadStores() {
////        interactor.getStoresCallback(object : mainInteractor.StoresCallback{
////            override fun getStoresCallBack(stores: MutableList<StoreEntity>) {
////                storesList.postValue(stores)
////            }
////        })
//        showProgress.value = Constans.SHOW
//
//        interactor.getStores {
//            showProgress.value = Constans.HIDE
//            storesList.postValue(it)
//            storeslists = it
//        }
//    }



    fun deleteStore(storeEntity: StoreEntity) {

//        viewModelScope.launch {
//            interactor.deleteStore(storeEntity)
//        }
        executeAction {interactor.deleteStore(storeEntity)}


//        interactor.deleteStore(storeEntity) {
//            val index = storeslists.indexOf(storeEntity)
//            if (index != -1) {
//                storeslists.removeAt(index)
//               // storesList.postValue(storeslists)
//            }
//        }
    }

    fun updateStore(storeEntity: StoreEntity) {

        storeEntity.isFavorite = !storeEntity.isFavorite
        executeAction { interactor.updateStore(storeEntity) }

//         {
//            val index = storeslists.indexOf(storeEntity)
//            if (index != -1) {
//                storeslists[index] = storeEntity
//              //  storesList.postValue(storeslists)
//            }
//        }
    }


    private fun executeAction(block: suspend ()-> Unit): Job{

        return viewModelScope.launch {
            showProgress.value= Constans.SHOW
            try {
                block()
                /*storeEntity.isFavorite = !storeEntity.isFavorite // block reemplaza esta parte de codigo
                interactor.updateStore(storeEntity)*/
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
                showProgress.value = Constans.HIDE
            }

        }
    }

}