package com.example.stores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.stores.databinding.FragmentEditStoreBinding

class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEditStoreBinding.inflate(inflater,container,false)
        return mBinding.root

        /* Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_store, container, false)*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val activity = activity as?  MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)// Se habilita flecha de retroceso
    }


}