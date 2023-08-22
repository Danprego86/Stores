package com.example.stores

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.databinding.ActivityMainBinding
import com.google.android.material.internal.ContextUtils.getActivity
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), OnClickListener, mainAux {

    private lateinit var mbinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)


        /* mbinding.btnSave.setOnClickListener {

             val store = StoreEntity(name = mbinding.etName.text.toString().trim())

             Thread {
                 StoreApplication.database.storeDao().addStore(store)
             }.start()

             mAdapter.add(store)
         }*/

        mbinding.fab.setOnClickListener { launchEditFragment() }
        setupRecyclerView()
    }


    @SuppressLint("CommitTransaction")
    private fun launchEditFragment() {
        val fragment = EditStoreFragment()// Se instancia la clase EditStoreFragment
        val fragmentManager = supportFragmentManager // Es el que controla los fragmentos
        val fragmentTransaction = fragmentManager.beginTransaction() //Indica como se va a realizar
        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null)// se indica que devuelve a la pantalla anterior de la app
        fragmentTransaction.commit()// Para que se apliquen los cambios.
       // mbinding.fab.hide()//Oculta el fragmentActionBottom
        hideFab()
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 2)
        getStore()
        mbinding.recyclerView.apply {

            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getStore() {

        val queue = LinkedBlockingQueue<MutableList<StoreEntity>>()

        thread {
            val stores = StoreApplication.database.storeDao().getAllStores()
            queue.add(stores)
        }
        mAdapter.setStores(queue.take())

    }

    // OnclickLisener
    override fun onClick(storeEntity: StoreEntity) {

    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        val queue = LinkedBlockingQueue<StoreEntity>()
        thread {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            queue.add(storeEntity)
        }
        mAdapter.updateStore(queue.take())
    }


    override fun onDeleteStore(storeEntity: StoreEntity) {
        val queue = LinkedBlockingQueue<StoreEntity>()

        Thread {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            queue.add(storeEntity)
        }
        mAdapter.delete(queue.take())
    }

    override fun hideFab(isVisible: Boolean) {
        if (isVisible)
            mbinding.fab.show()
        else {
            mbinding.fab.hide()
        }
    }
}

