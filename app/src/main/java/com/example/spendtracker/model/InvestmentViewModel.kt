package com.example.spendtracker.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendtracker.repository.Repository
import com.example.spendtracker.util.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

data class InvestmentScreenState(
    val showDialog: Boolean = false,
    val investmentToEdit: Investment? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class InvestmentViewModel(private val repository: Repository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(InvestmentScreenState())
    val uiState: StateFlow<InvestmentScreenState> = _uiState.asStateFlow()
    
    val investments = repository.getAllInvestments()
    
    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showDialog = true)
    }
    
    fun hideDialog() {
        _uiState.value = _uiState.value.copy(
            showDialog = false,
            investmentToEdit = null,
            errorMessage = null
        )
    }
    
    fun editInvestment(investment: Investment) {
        _uiState.value = _uiState.value.copy(investmentToEdit = investment)
    }
    
    fun addInvestment(name: String, category: String, amount: String) {
        if (!validateInput(name, amount)) {
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.insertInvestment(
                    Investment(
                        name = name.trim(),
                        category = category.trim(),
                        amount = amount.toDouble()
                    )
                )
                hideDialog()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "${AppConstants.ERROR_ADD_INVESTMENT}: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    fun updateInvestment(investment: Investment, name: String, category: String, amount: String) {
        if (!validateInput(name, amount)) {
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val updatedInvestment = investment.copy(
                    name = name.trim(),
                    category = category.trim(),
                    amount = amount.toDouble()
                )
                repository.updateInvestment(updatedInvestment)
                hideDialog()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "${AppConstants.ERROR_UPDATE_INVESTMENT}: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    fun deleteInvestment(investment: Investment) {
        viewModelScope.launch {
            try {
                repository.deleteInvestment(investment)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "${AppConstants.ERROR_DELETE_INVESTMENT}: ${e.message}"
                )
            }
        }
    }
    
    private fun validateInput(name: String, amount: String): Boolean {
        return when {
            name.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = AppConstants.ERROR_EMPTY_NAME)
                false
            }
            amount.toDoubleOrNull() == null -> {
                _uiState.value = _uiState.value.copy(errorMessage = AppConstants.ERROR_INVALID_AMOUNT)
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