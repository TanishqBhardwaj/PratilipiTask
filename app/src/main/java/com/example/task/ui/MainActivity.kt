package com.example.task.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
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
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddNewData : FloatingActionButton

    private lateinit var viewModel : DataViewModel
    private lateinit var dataAdapter: DataAdapter

    private var dataList: ArrayList<Data> = arrayListOf()
    private var tempDataList: ArrayList<Data> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setView()
        setListeners()
        setDataList()
    }

    override fun onResume() {
        super.onResume()
        setDataList()
    }

    private fun setView() {
        recyclerView = findViewById(R.id.recyclerView)
        fabAddNewData = findViewById(R.id.fabAdd)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempDataList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()) {
                    dataList.forEach {
                        if(it.title.lowercase(Locale.getDefault()).contains(searchText)) {
                            tempDataList.add(it)
                        }
                        if(it.description.lowercase(Locale.getDefault()).contains(searchText)) {
                            tempDataList.add(it)
                        }
                    }
                }
                else {
                    tempDataList.clear()
                    tempDataList.addAll(dataList)
                }
                dataAdapter.differ.submitList(tempDataList.distinct())

                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun setListeners() {

        dataAdapter.setOnItemClickListener {
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra("data", it)
            startActivity(intent)
        }

        dataAdapter.setOnDeleteClick {
            viewModel.deleteData(it)
        }

        fabAddNewData.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java))
        }

    }

    private fun setDataList() {
        viewModel.getAllData().observe(this, Observer { dataList ->
            this.dataList.clear()
            this.tempDataList.clear()
            this.dataList.addAll(dataList.reversed())
            this.tempDataList.addAll(this.dataList)
            dataAdapter.differ.submitList(this.dataList.distinct())
        })
    }
}