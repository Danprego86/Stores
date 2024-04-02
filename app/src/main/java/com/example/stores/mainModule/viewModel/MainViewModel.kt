package com.example.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stores.common.entities.StoreEntity
import com.example.stores.common.utils.Constans
import com.example.stores.common.utils.StoreExceptions
import com.example.stores.common.utils.TypeError
import com.example.stores.mainModule.model.mainInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    //private val storesList: MutableLiveData<List<StoreEntity>> = MutableLiveData()// Esta propiedad nos ayudara a reflejar los datos en la vista
    private var interactor: mainInteractor = mainInteractor()
    //private var storeslists: MutableList<StoreEntity> = mutableListOf()


    private val showProgress: MutableLiveData<Boolean> = MutableLiveData()

    private val typeError: MutableLiveData<TypeError> = MutableLiveData()


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
    fun getTypeError(): MutableLiveData<TypeError> = typeError

    fun isShowProgress(): MutableLiveData<Boolean> {
        return showProgress
    }
    fun deleteStore(storeEntity: StoreEntity) {
        executeAction { interactor.deleteStore(storeEntity) }
    }

    fun updateStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        executeAction { interactor.updateStore(storeEntity) }

    }
    private fun executeAction(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            showProgress.value = Constans.SHOW
            try {
                block()
            } catch (e: StoreExceptions) {
                typeError.value= e.typeError
            } finally {
                showProgress.value = Constans.HIDE
            }
        }
    }

}