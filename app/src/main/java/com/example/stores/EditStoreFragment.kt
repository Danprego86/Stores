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
import com.example.stores.databinding.FragmentEditStoreBinding
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.LinkedBlockingQueue

class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding
    private var mActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)// Se habilita flecha de retroceso
        mActivity?.supportActionBar?.title =
            getString(R.string.edit_store_title_add)// Se cambia el titulo de la tienda en el segundo fragment

        setHasOptionsMenu(true)//Tener acceso al menu
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
                    webSite = mBinding.etWebsite.text.toString().trim()
                )

                val queue = LinkedBlockingQueue<Long?>()

                Thread {
                    val id = StoreApplication.database.storeDao().addStore(store)
                    queue.add(id)
                }.start()

                queue.take().let {

                    mActivity?.addStore(store)// indica al Main Activity la nueva tienda
                    hideKeyboard()
                    Snackbar.make(mBinding.root,
                        getString(
                            R.string.edit_store_message_save_succes),
                            Snackbar.LENGTH_SHORT)
                            .show()
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
        mActivity?.supportActionBar?.title = getString(R.string.app_name)//Indicamos volver a mostrar el nombre de la app
        mActivity?.hideFab(true)
        setHasOptionsMenu(false)
        super.onDestroy()
    }
}

