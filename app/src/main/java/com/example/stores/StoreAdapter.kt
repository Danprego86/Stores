package com.example.stores

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stores.databinding.ItemStoreBinding

class StoreAdapter(
    private var stores: MutableList<StoreEntity>,
    private var listener: OnClickListener
) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflar la vista
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores.get(position)

        with(holder) {
            setListener(store)
            binding.tvName.text = store.name
        }
    }

    override fun getItemCount(): Int = stores.size

    @SuppressLint("NotifyDataSetChanged")
    fun add(storeEntity: StoreEntity) {
        stores.add(storeEntity)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setStores(stores: MutableList<StoreEntity>) {

        this.stores = stores
        notifyDataSetChanged()
    }

    fun updateStore(storeEntity: StoreEntity?) {
        val index = stores.indexOf(storeEntity)

        if (index != 1){
            if (storeEntity != null) {
                stores.set(index, storeEntity)
            }
        }
        notifyItemChanged(index)
    }

    fun delete (storeEntity: StoreEntity){
        val index = stores.indexOf(storeEntity)
        if(index != 1){
            stores.removeAt(index)
            notifyItemRemoved(index)
        }

    }

    //Class Inner
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemStoreBinding.bind(view)

        fun setListener(storeEntity: StoreEntity) {

            with(binding.root) {

                binding.root.setOnClickListener {
                    listener.onClick(storeEntity)
                }

                binding.root.setOnLongClickListener {
                    listener.onDeleteStore(storeEntity)
                    true
                }

                binding.cbFavorite.setOnClickListener {
                    listener.onFavoriteStore(storeEntity)
                }
            }
        }
    }
}