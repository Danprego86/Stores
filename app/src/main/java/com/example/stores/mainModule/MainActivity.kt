package com.example.stores.mainModule

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.editModule.EditStoreFragment
import com.example.stores.R
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.databinding.ActivityMainBinding
import com.example.stores.common.utils.mainAux
import com.example.stores.editModule.viewModel.editStoreViewModel
import com.example.stores.mainModule.adapter.OnClickListener
import com.example.stores.mainModule.adapter.StoreAdapter
import com.example.stores.mainModule.viewModel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mbinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    //MVVM
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mEditStoreViewModel: editStoreViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)


//         mbinding.btnSave.setOnClickListener {
//
//             val store = StoreEntity(name = mbinding.etName.text.toString().trim())
//
//             Thread {
//                 StoreApplication.database.storeDao().addStore(store)
//             }.start()
//
//             mAdapter.add(store)
//         }

        mbinding.fab.setOnClickListener { launchEditFragment() }

        setupViewModel()
        setupRecyclerView()
    }

    private fun setupViewModel() {//Inicializar el mMianViewModel para ver las propiedades del MianViewmodel dentro de nuestra vista

        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mEditStoreViewModel = ViewModelProvider(this)[editStoreViewModel::class.java]

        mMainViewModel.getStores().observe(this) { stores ->

            mAdapter.setStores(stores as MutableList<StoreEntity>)
        }

        mEditStoreViewModel.getShowFab().observe(this) { isVisible ->

            if (isVisible)
                mbinding.fab.show()
            else {
                mbinding.fab.hide()
            }
        }
        mEditStoreViewModel.getStoreSelected().observe(this) {

            mAdapter.add(it)
        }


    }

    @SuppressLint("CommitTransaction")
    private fun launchEditFragment(storeEntity: StoreEntity= StoreEntity()) {

        mEditStoreViewModel.setShowFab(false)
        mEditStoreViewModel.setStoreSelected(storeEntity)
        val fragment = EditStoreFragment()// Se instancia la clase EditStoreFragment
        val fragmentManager = supportFragmentManager // Es el que controla los fragmentos
        val fragmentTransaction = fragmentManager.beginTransaction() //Indica como se va a realizar
        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null)// se indica que devuelve a la pantalla anterior de la app
        fragmentTransaction.commit()// Para que se apliquen los cambios.

        //hideFab()// mbinding.fab.hide()//Oculta el fragmentActionBottom
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))
        //getStore()
        mbinding.recyclerView.apply {

            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

//    @SuppressLint("SuspiciousIndentation")// se ajusta mejora en MainViewModel
//    private fun getStore() {
//
//        val queue = LinkedBlockingQueue<MutableList<StoreEntity>>()
//
//        thread {
//            val stores = StoreApplication.database.storeDao().getAllStores()
//            queue.add(stores)
//        }
//        mAdapter.setStores(queue.take())
//
//    }

    // OnclickLisener
    override fun onClick(storeEntity: StoreEntity) {
        launchEditFragment(storeEntity)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {

        mMainViewModel.updateStore(storeEntity)
    }


    override fun onDeleteStore(storeEntity: StoreEntity) {
        //val items = arrayOf("Eliminar", "Llamar", "Ir al sitio web")
        val items =
            resources.getStringArray(R.array.array_options_items)// Mejores formas de implememtacion de array de string

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_options_title)
            .setItems(items) { dialog, i ->
                when (i) {
                    0 -> confirmDelete(storeEntity)

                    //1 -> Toast.makeText(this, "Llamar...", Toast.LENGTH_SHORT).show()
                    1 -> dial(storeEntity.phone)

                    //2 -> Toast.makeText(this, "Sitio web...", Toast.LENGTH_SHORT).show()
                    2 -> toGoWebSite(storeEntity.webSite)
                }
            }.show()
    }

    private fun confirmDelete(storeEntity: StoreEntity) {

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setPositiveButton(
                R.string.dialog_delete_confirm,
                DialogInterface.OnClickListener { dialog, which ->
                    mMainViewModel.deleteStore(storeEntity)
                })
            .setNegativeButton(R.string.dialog_delete_cancel, null)
            .show()
    }


    private fun dial(phone: String) {

        val callIntent = Intent().apply {

            action = Intent.ACTION_DIAL// accion a realizar con el numero de telefono
            data =
                Uri.parse("tel:$phone")//pasamos el numero guardado en la tienda para llamar con el indicativo
        }
        starIntent(callIntent)
    }


    private fun toGoWebSite(website: String) {

        if (website.isEmpty()) {// validacion de sitio web

            Toast.makeText(this, R.string.main_error_no_website, Toast.LENGTH_SHORT).show()

        } else {

            val webSiteIntent = Intent().apply {

                action = Intent.ACTION_VIEW
                data = Uri.parse(website)
            }
            starIntent(webSiteIntent)
        }
    }

    private fun starIntent(intent: Intent) {

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)//Inicia el intent
        } else {
            Toast.makeText(this, R.string.main_error_no_resolve, Toast.LENGTH_SHORT).show()
        }

    }


//    override fun hideFab(isVisible: Boolean) {
//        if (isVisible)
//            mbinding.fab.show()
//        else {
//            mbinding.fab.hide()
//        }
//    }
//
//    override fun addStore(storeEntity: StoreEntity) {// indica al adaptador la nueva tienda añadida
//        mAdapter.add(storeEntity)
//
//    }
//
//    override fun updateStore(storeEntity: StoreEntity) {
//        mAdapter.updateStore(storeEntity)
//    }
}

