package com.example.spendtracker.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendtracker.ds.entity.Spending
import com.example.spendtracker.ds.repository.Repository
import com.example.spendtracker.util.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SpendingScreenState(
    val showDialog: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SpendingViewModel(private val repository: Repository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SpendingScreenState())
    val uiState: StateFlow<SpendingScreenState> = _uiState.asStateFlow()
    
    val spendings = repository.getAllSpendings()
    
    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showDialog = true)
    }
    
    fun hideDialog() {
        _uiState.value = _uiState.value.copy(
            showDialog = false,
            errorMessage = null
        )
    }
    
    fun addSpending(name: String, category: String, amount: Double) {
        if (!validateInput(name, amount)) {
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.insertSpending(
                    Spending(
                        name = name.trim(),
                        category = category.trim(),
                        amount = amount.toDouble()
                    )
                )
                hideDialog()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "${AppConstants.ERROR_ADD_SPENDING}: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    fun deleteSpending(spending: Spending) {
        viewModelScope.launch {
            try {
                repository.deleteSpending(spending)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "${AppConstants.ERROR_DELETE_SPENDING}: ${e.message}"
                )
            }
        }
    }
    
    private fun validateInput(name: String, amount: Double): Boolean {
        return when {
            name.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = AppConstants.ERROR_EMPTY_NAME)
                false
            }
            amount.toDouble() <= 0 -> {
                _uiState.value = _uiState.value.copy(errorMessage = AppConstants.ERROR_NEGATIVE_AMOUNT)
                false
            }
            else -> true
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}