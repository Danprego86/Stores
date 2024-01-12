package com.example.stores.editModule

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.stores.R
import com.example.stores.common.entities.StoreEntity
import com.example.stores.databinding.FragmentEditStoreBinding
import com.example.stores.editModule.viewModel.editStoreViewModel
import com.example.stores.mainModule.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout


class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding
    //MVVM
    private lateinit var mEditStoreViewModel: editStoreViewModel

    private var mActivity: MainActivity? = null
    private var mIsEditMode: Boolean = false
    private lateinit var mStoreEntity: StoreEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mEditStoreViewModel = ViewModelProvider(requireActivity()).get(editStoreViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //MVVM
        setupViewModel()
        setupTexfiles()
    }

    private fun setupViewModel() {
        mEditStoreViewModel.getStoreSelected().observe(viewLifecycleOwner) {

            mStoreEntity = (it ?: StoreEntity())
            if (it != null) {
                mIsEditMode = true
                //getStore(it.id)
                setUiStore(it)
            } else {
                mIsEditMode = false
            }
            setupActionBar()
        }

        mEditStoreViewModel.getResult().observe(viewLifecycleOwner) { result ->

            hideKeyboard()

            when (result) {

                is Long -> {
                    // mActivity?.addStore(this)// indica al Main Activity la nueva tienda
                    mStoreEntity.id = result
                    mEditStoreViewModel.setStoreSelected(mStoreEntity)
                    Toast.makeText(
                        mActivity,
                        R.string.edit_store_message_save_succes,
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()// Despues de guardar la tienda regresa al home
                }

                is StoreEntity -> {
                    //mActivity?.updateStore(mStoreEntity!!)
                    mEditStoreViewModel.setStoreSelected(mStoreEntity)
                    Snackbar.make(
                        mBinding.root,
                        R.string.edit_store_message_update_succes,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)// Se habilita flecha de retroceso
        mActivity?.supportActionBar?.title =
            if (mIsEditMode) {
                getString(R.string.edit_store_title_edit)// Se edita el titulo de la tienda en el segundo fragment
            } else {
                getString(R.string.edit_store_title_add)// Se añade nueva tienda en el segundo fragment
            }

        setHasOptionsMenu(true)//Tener acceso al menu
    }

    private fun setupTexfiles() {

        with(mBinding) {

            // mBinding.etPhotoUrl.addTextChangedListener {loadImagen(it.toString().trim())} En este punto unimos los addtextChangeLisener con el de la linea de abajo
            etName.addTextChangedListener { validateFiles(tilName) } // Lo que pongamos en este espacio se ejecutara despues de que el texto haya sido cambiado
            etPhone.addTextChangedListener { validateFiles(tilPhone) } // Lo que pongamos en este espacio se ejecutara despues de que el texto haya sido cambiado
            etPhotoUrl.addTextChangedListener {
                validateFiles(tilPhotoUrl)
                loadImagen(it.toString().trim())
            } // Lo que pongamos en este espacio se ejecutara despues de que el texto haya sido cambiado
        }
    }

    private fun loadImagen(url: String) {

        Glide.with(this)
            //.load(mBinding.etPhotoUrl.text.toString())
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imgPhoto)
    }


//    private fun getStore(id: Long) {
//        val queue =
//            LinkedBlockingQueue<StoreEntity?>() // traer un objeto de tipo SoreEntity para rellenar la vista
//        Thread {
//            mStoreEntity = StoreApplication.database.storeDao().getStoreById(id)
//            queue.add(mStoreEntity)
//        }.start()
//        queue.take()?.let {// si el resultado no es null realice lo de adentro de let
//            setUiStore(it)
//        }
//    }

    private fun setUiStore(it: StoreEntity) {
        with(mBinding) {
            etName.text = it.name.editable()
            //etPhone.setText(it.phone)
            etPhone.text = it.phone.editable()
            etWebsite.text = it.webSite.editable()
            etPhotoUrl.text = it.photoUrl.editable()

        }
    }

    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onAttach(context: Context) {// Este se ejecuta cuando el fragmento se enlaza con la actividad
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    MaterialAlertDialogBuilder(requireContext())//indica si el cliente quiere regresar o seguir en la misma pantalla
                        .setTitle(R.string.dialog_exit_title)
                        .setMessage(R.string.dialog_exit_message)
                        .setPositiveButton(R.string.dialog_exit_ok) { _, _ ->
                            if (isEnabled) {
                                isEnabled = false
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }
                        }
                        .setNegativeButton(R.string.dialog_delete_cancel, null)
                        .show()
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)//Se ingres un recuerso
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//Opcion para retroceder y guardar
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_save -> {//Indicamos que queremos hacer cuando pinchamos en el icono de check
                if (validateFiles(
                        mBinding.tilPhotoUrl,
                        mBinding.tilPhone,
                        mBinding.tilName
                    )
                ) {
                    with(mStoreEntity) {
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        webSite = mBinding.etWebsite.text.toString().trim()
                        photoUrl = mBinding.etPhotoUrl.text.toString().trim()
                    }

                    if (mIsEditMode) {
                        //StoreApplication.database.storeDao().updateStore(mStoreEntity!!)
                        mEditStoreViewModel.updateStores(mStoreEntity)
                    } else {
                        //mStoreEntity!!.id = StoreApplication.database.storeDao().addStore(mStoreEntity!!)// se añade eñ "store.id" para notificar los cambios de favoritos
                        mEditStoreViewModel.saveStore(mStoreEntity)
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateFiles(vararg texFiles: TextInputLayout): Boolean {// validacion de campos con recursividad
        var isvalid = true

        for (texFile in texFiles) {
            if (texFile.editText?.text.toString().trim().isEmpty()) {
                texFile.error = getString(R.string.helper_required)
                isvalid = false
            } else {
                texFile.error = null
            }
        }

        if (!isvalid) {
            Snackbar.make(
                mBinding.root,
                R.string.edit_store_message_valid,
                Snackbar.LENGTH_SHORT
            ).show()
        }

        return isvalid
    }

    /* private fun validateFiles(): Boolean {// validacion de los campos ya que hasta el momento los guarda en blanco
         var isValid = true

         if (mBinding.etPhotoUrl.text.toString().trim().isEmpty()) {
             mBinding.tilPhotoUrl.error = getString(R.string.helper_required)
             mBinding.etPhotoUrl.requestFocus() // RequestFocus() indica donde hace falta el campo por llenar
             isValid = false
         }
         if (mBinding.etPhone.text.toString().trim().isEmpty()) {
             mBinding.tilPhone.error = getString(R.string.helper_required)
             mBinding.etPhone.requestFocus()
             isValid = false
         }
         if (mBinding.etName.text.toString().trim().isEmpty()) {
             mBinding.tilName.error = getString(R.string.helper_required)
             mBinding.etName.requestFocus()
             isValid = false
         }
         return isValid
     }*/

    override fun onDestroyView() {//Metodo para ocultar teclado cuando retrocedemos con la flecha
        hideKeyboard()
        super.onDestroyView()
    }

    private fun hideKeyboard() {//Metodo para ocultar teclado cuando se guarda una tienda

        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (view != null){
            imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
        }

    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title =
            getString(R.string.app_name)//Indicamos volver a mostrar el nombre de la app
        // mActivity?.hideFab(true)
        mEditStoreViewModel.setShowFab(true)
        mEditStoreViewModel.setResult(Any())// Se limpiara lo que esta en vewModel
        setHasOptionsMenu(false)
        super.onDestroy()
    }
}

