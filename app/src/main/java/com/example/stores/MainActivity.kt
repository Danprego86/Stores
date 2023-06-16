package com.example.stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mbinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout:GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        mbinding.btnSave.setOnClickListener {

            val storeEntity = StoreEntity(name = mbinding.etName.text.toString().trim())
            mAdapter.add(storeEntity)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        mAdapter= StoreAdapter(mutableListOf(), this)
        mGridLayout= GridLayoutManager(this,2)

        mbinding.recyclerView.apply {

            setHasFixedSize(true)
            layoutManager= mGridLayout
            adapter = mAdapter
        }

    }

    // OnclickLisener
    override fun onClick(storeEntity: StoreEntity) {

    }

}

