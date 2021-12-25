package com.example.task.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.adapter.DataAdapter
import com.example.task.local.Data
import com.example.task.local.Database
import com.example.task.repository.DataRepository
import com.example.task.viewModel.DataViewModel
import com.example.task.viewModel.DataViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddNewData : FloatingActionButton
    private lateinit var searchView : SearchView

    private lateinit var viewModel : DataViewModel
    private lateinit var dataAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setView()
        setListeners()
        setDataList()
    }

    private fun setView() {
        recyclerView = findViewById(R.id.recyclerView)
        fabAddNewData = findViewById(R.id.fabAdd)
        searchView = findViewById(R.id.searchView)

        setupRecyclerView()
        setViewModel()
    }

    private fun setupRecyclerView() {
        dataAdapter = DataAdapter()
        recyclerView.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setViewModel() {
        val dataRepository = DataRepository(Database(this))
        val viewModelProviderFactory = DataViewModelFactory(dataRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(DataViewModel::class.java)
    }

    private fun setListeners() {

        dataAdapter.setOnItemClickListener {
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra(InputActivity.TITLE, it.title)
            intent.putExtra(InputActivity.DESCRIPTION, it.description)
            intent.putExtra(InputActivity.IMAGE, it.image)
            intent.putExtra(InputActivity.ID, it.id)
            startActivity(intent)
        }

        fabAddNewData.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java))
        }

    }

    private fun setDataList() {
        viewModel.getAllData().observe(this, Observer { dataList ->
            dataAdapter.differ.submitList(dataList.reversed())
        })
    }
}