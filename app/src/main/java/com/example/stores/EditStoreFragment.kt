package com.example.stores

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.stores.databinding.FragmentEditStoreBinding
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.LinkedBlockingQueue

class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding
    private var mActivity: MainActivity? = null
    private var mIsEditMode: Boolean = false
    private var mStoreEntity: StoreEntity?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong(getString(R.string.arg_id), 0)

        if (id != null && id != 0L){
            mIsEditMode = true
            getStore(id)
        }else{
            Toast.makeText(activity, id.toString(), Toast.LENGTH_SHORT).show()
        }

        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)// Se habilita flecha de retroceso
        mActivity?.supportActionBar?.title =
            getString(R.string.edit_store_title_add)// Se cambia el titulo de la tienda en el segundo fragment

        setHasOptionsMenu(true)//Tener acceso al menu

        mBinding.etPhotoUrl.addTextChangedListener {
            Glide.with(this)
                .load(mBinding.etPhotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mBinding.imgPhoto)
        }
    }

    private fun getStore(id: Long) {
        val queue = LinkedBlockingQueue<StoreEntity?>() // traer un objeto de tipo SoreEntity para rellenar la vista
        Thread{
            mStoreEntity = StoreApplication.database.storeDao().getStoreById(id)
            queue.add(mStoreEntity)
        }.start()
        queue.take()?.let {// si el resultado no es null realice lo de adentro de let
            setUiStore(it)
        }
    }

    private fun setUiStore(it: StoreEntity) {
        with(mBinding){
            etName.setText(it.name)
            etPhone.setText(it.phone)
            etWebsite.setText(it.webSite)
            etPhotoUrl.setText(it.photoUrl)
            Glide.with(requireActivity())
                .load(it.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgPhoto)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)//Se ingres un recuerso
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//Opcion para retroceder y guardar
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressedDispatcher?.onBackPressed()
                true
            }

            R.id.action_save -> {//Indicamos que queremos hacer cuando pinchamos en el icono de check
                val store = StoreEntity(
                    name = mBinding.etName.text.toString().trim(),
                    phone = mBinding.etPhone.text.toString().trim(),
                    webSite = mBinding.etWebsite.text.toString().trim(),
                    photoUrl = mBinding.etPhotoUrl.text.toString().trim()
                )

                val queue = LinkedBlockingQueue<Long?>()

                Thread {
                    store.id = StoreApplication.database.storeDao()
                        .addStore(store)// se añade eñ "store.id" para notificar los cambios de favoritos
                    queue.add(store.id)
                }.start()

                queue.take().let {

                    mActivity?.addStore(store)// indica al Main Activity la nueva tienda
                    hideKeyboard()
//                    Snackbar.make(mBinding.root,
//                        getString(
//                            R.string.edit_store_message_save_succes),
//                            Snackbar.LENGTH_SHORT)
//                            .show()
                    Toast.makeText(
                        mActivity,
                        R.string.edit_store_message_save_succes,
                        Toast.LENGTH_SHORT
                    ).show()
                    mActivity?.onBackPressedDispatcher?.onBackPressed()// Despues de guardar la tienda regresa al home
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {//Metodo para ocultar teclado cuando retrocedemos con la flecha
        hideKeyboard()
        super.onDestroyView()
    }

    private fun hideKeyboard() {//Metodo para ocultar teclado cuando se guarda una tienda

        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title =
            getString(R.string.app_name)//Indicamos volver a mostrar el nombre de la app
        mActivity?.hideFab(true)
        setHasOptionsMenu(false)
        super.onDestroy()
    }
}

