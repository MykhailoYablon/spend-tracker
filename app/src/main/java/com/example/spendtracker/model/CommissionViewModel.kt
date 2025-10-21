package com.example.spendtracker.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendtracker.ds.entity.Commission
import com.example.spendtracker.ds.repository.CommissionRepository
import kotlinx.coroutines.launch

class CommissionViewModel(private val repository: CommissionRepository) : ViewModel(){

    val allCommissions = repository.getAll()

    fun add(commission: Commission) {
        viewModelScope.launch {
            repository.insert(commission)
        }
    }

    fun delete(commission: Commission) {
        viewModelScope.launch {
            repository.delete(commission)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}