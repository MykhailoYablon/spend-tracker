package com.example.spendtracker.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendtracker.repository.BondRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BondViewModel(private val repository: BondRepository) : ViewModel() {
    private val _showDetailScreen = MutableStateFlow(false)
    val showDetailScreen = _showDetailScreen.asStateFlow()

    val bonds = repository.getAllBonds()

    private val _selectedCalculation = MutableStateFlow<Calculation?>(null)
    val selectedBond = _selectedCalculation.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    fun showAddDialog() {
        _showAddDialog.value = true
    }

    fun hideAddDialog() {
        _showAddDialog.value = false
    }

    fun showDetailDialog(Calculation: Calculation) {
        _selectedCalculation.value = Calculation
        _showDetailScreen.value = true
    }

    fun hideDetailScreen() {
        _showDetailScreen.value = false
        _selectedCalculation.value = null
    }

    fun addBond(calculation: Calculation) {
        viewModelScope.launch {
            repository.insertBond(calculation)
        }
    }

    fun deleteBond(Calculation: Calculation) {
        viewModelScope.launch {
            repository.deleteBond(Calculation)
        }
    }
}