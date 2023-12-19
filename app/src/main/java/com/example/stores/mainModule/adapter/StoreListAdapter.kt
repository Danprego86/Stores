package com.example.stores.mainModule.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.stores.R
import com.example.stores.common.entities.StoreEntity
import com.example.stores.databinding.ItemStoreBinding

class StoreListAdapter(
    private var listener: OnClickListener
) : ListAdapter<StoreEntity, RecyclerView.ViewHolder>(storeDiffCallback()) {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflar la vista
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val store = getItem(position)

        with(holder as ViewHolder) {
            setListener(store)
            binding.tvName.text = store.name
            binding.cbFavorite.isChecked = store.isFavorite

            Glide.with(mContext)
                .load(store.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPhoto)
        }
    }

    //  override fun getItemCount(): Int = stores.size
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun add(storeEntity: StoreEntity) {
//
//        if (storeEntity.id!=0L) {
//            if (!stores.contains(storeEntity)){ // validacion de si una tienda ya existe
//
//                stores.add(storeEntity)
//                notifyItemInserted(stores.size-1)//Valida el elemento ingresado con el de la ultima posicion
//            }else{
//                updateStore(storeEntity)
//            }
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setStores(stores: MutableList<StoreEntity>) {
//
//        this.stores = stores
//        notifyDataSetChanged()
//    }
//
//     private fun updateStore(storeEntity: StoreEntity?) {
//        val index = stores.indexOf(storeEntity)
//
//        if (index != -1) {
//            if (storeEntity != null) {
//                stores[index] = storeEntity
//                notifyItemChanged(index)
//            }
//        }
//    }

    //Class Inner
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemStoreBinding.bind(view)

        fun setListener(storeEntity: StoreEntity) {

            with(binding.root) {

                setOnClickListener {
                    listener.onClick(storeEntity)
                }

                setOnLongClickListener {
                    listener.onDeleteStore(storeEntity)
                    true
                }
            }

            binding.cbFavorite.setOnClickListener {
                listener.onFavoriteStore(storeEntity)
            }
        }
    }

    class storeDiffCallback : DiffUtil.ItemCallback<StoreEntity>() {
        override fun areItemsTheSame(oldItem: StoreEntity, newItem: StoreEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoreEntity, newItem: StoreEntity): Boolean {
            return oldItem == newItem
        }

    }

}