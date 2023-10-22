package com.example.stores.editModule.viewModel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stores.common.entities.StoreEntity
import com.example.stores.editModule.model.editStoreInteractor
import com.example.stores.mainModule.model.mainInteractor

class editStoreViewModel : ViewModel() {

    private val storeSelected = MutableLiveData<StoreEntity>()
    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()

    private val interactor: editStoreInteractor = editStoreInteractor()


    fun setStoreSelected(storeEntity: StoreEntity) {
        storeSelected.postValue(storeEntity)
    }

    fun getStoreSelected(): LiveData<StoreEntity> {
        return storeSelected
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

    fun saveStore(storeEntity: StoreEntity){
        interactor.saveStore(storeEntity,{newId->
            result.postValue(newId)
        })
    }
    fun updateStores(storeEntity: StoreEntity){
        interactor.updateStore(storeEntity,{storeUpdated->
            result.postValue(storeUpdated)
        })
    }

}