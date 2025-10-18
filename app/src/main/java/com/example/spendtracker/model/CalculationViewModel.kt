package com.example.spendtracker.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendtracker.repository.CalculationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculationViewModel(private val repository: CalculationRepository) : ViewModel() {
    private val _showDetailScreen = MutableStateFlow(false)
    val showDetailScreen = _showDetailScreen.asStateFlow()

    val allCalculations = repository.getAll()

    private val _selectedCalculationResult = MutableStateFlow<CalculationResult?>(null)
    val selectedBond = _selectedCalculationResult.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    fun showAddDialog() {
        _showAddDialog.value = true
    }

    fun hideAddDialog() {
        _showAddDialog.value = false
    }

    fun showDetailDialog(CalculationResult: CalculationResult) {
        _selectedCalculationResult.value = CalculationResult
        _showDetailScreen.value = true
    }

    fun hideDetailScreen() {
        _showDetailScreen.value = false
        _selectedCalculationResult.value = null
    }

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