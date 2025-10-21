package com.example.spendtracker.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendtracker.ds.entity.CalculationResult
import com.example.spendtracker.ds.repository.CalculationRepository
import kotlinx.coroutines.launch

class CalculationViewModel(private val repository: CalculationRepository) : ViewModel() {
    val allCalculations = repository.getAll()

    fun add(calculationResult: CalculationResult) {
        viewModelScope.launch {
            repository.insert(calculationResult)
        }
    }

    fun delete(CalculationResult: CalculationResult) {
        viewModelScope.launch {
            repository.delete(CalculationResult)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}