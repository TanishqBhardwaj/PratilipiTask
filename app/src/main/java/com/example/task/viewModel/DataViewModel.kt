package com.example.task.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.task.local.Data
import com.example.task.repository.DataRepository
import kotlinx.coroutines.launch

class DataViewModel (
    private val dataRepository: DataRepository
    ) : ViewModel() {

    val dataList = Pager(
        PagingConfig(
            pageSize = 5,
            enablePlaceholders = true,
            maxSize = 200
        )
    ) {
        dataRepository.getAllData()
    }.flow

    fun saveData(data : Data) = viewModelScope.launch {
        dataRepository.insert(data)
    }

    fun updateData(data: Data) = viewModelScope.launch {
        dataRepository.update(data)
    }

    fun deleteData(data: Data) = viewModelScope.launch {
        dataRepository.delete(data)
    }

    fun getAllData() = dataRepository.getAllData()
}