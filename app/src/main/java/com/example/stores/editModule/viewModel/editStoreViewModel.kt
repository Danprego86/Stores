package com.example.stores.editModule.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stores.common.entities.StoreEntity
import com.example.stores.common.utils.StoreExceptions
import com.example.stores.common.utils.TypeError
import com.example.stores.editModule.model.editStoreInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class editStoreViewModel : ViewModel() {

    private var storeId: Long = 0

    //private val storeSelected = MutableLiveData<StoreEntity>()
    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()

    private val interactor: editStoreInteractor = editStoreInteractor()

    private val typeError: MutableLiveData<TypeError> = MutableLiveData()

    fun setTypeError(typeError: TypeError){
        this.typeError.value = typeError
    }

    fun getTypeError(): MutableLiveData<TypeError> = typeError
    fun setStoreSelected(storeEntity: StoreEntity) {
        storeId = storeEntity.id
        // storeSelected.postValue(storeEntity)
    }

    fun getStoreSelected(): LiveData<StoreEntity> {
        return interactor.getStoreById(storeId)
    }

    fun setShowFab(isVisible: Boolean) {
        showFab.postValue(isVisible)
    }

    fun getShowFab(): LiveData<Boolean> {
        return showFab
    }

    fun setResult(value: Any) {
        result.postValue(value)
    }

    fun getResult(): LiveData<Any> {
        return result
    }

    fun saveStore(storeEntity: StoreEntity) {
        executeAction(storeEntity) { interactor.saveStore(storeEntity) }
    }

    fun updateStores(storeEntity: StoreEntity) {
        executeAction(storeEntity) { interactor.updateStore(storeEntity) }
    }

    private fun executeAction(storeEntity: StoreEntity, block: suspend () -> Unit): Job {

        return viewModelScope.launch {
            try {
                block()
                result.postValue(storeEntity)
            } catch (e: StoreExceptions) {
                typeError.value = e.typeError
            }
        }
    }

}