package com.example.stores

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.databinding.ActivityMainBinding
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mbinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout:GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        mbinding.btnSave.setOnClickListener {

            val store = StoreEntity(name = mbinding.etName.text.toString().trim())

            Thread{
                StoreApplication.database.storeDao().addStore(store)
            }.start()

            mAdapter.add(store)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        mAdapter= StoreAdapter(mutableListOf(), this)
        mGridLayout= GridLayoutManager(this,2)
        getStore()
        mbinding.recyclerView.apply {

            setHasFixedSize(true)
            layoutManager= mGridLayout
            adapter = mAdapter
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getStore(){

       val queue = LinkedBlockingQueue<MutableList<StoreEntity>>()

                thread{
                    val stores = StoreApplication.database.storeDao().getAllStores()
                    queue.add(stores)
                }

       mAdapter.setStores(queue.take())

    }

    // OnclickLisener
    override fun onClick(storeEntity: StoreEntity) {

    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        val queue= LinkedBlockingQueue<StoreEntity>()

        Thread{
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            queue.add(storeEntity)
        }.start()
        mAdapter.delete(queue.take())
    }
}

