package com.example.stores

interface mainAux {

    fun hideFab(isVisible: Boolean = false)

    fun addStore(storeEntity:StoreEntity)

    fun updateStore(storeEntity:StoreEntity)
}