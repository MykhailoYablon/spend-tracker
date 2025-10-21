package com.example.spendtracker.model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendtracker.ds.entity.CalculationResult
import com.example.spendtracker.ds.repository.CalculationRepository
import com.example.spendtracker.service.CsvExporter
import kotlinx.coroutines.launch

class CalculationViewModel(private val repository: CalculationRepository) : ViewModel() {
    val allCalculations = repository.getAll()

    fun add(calculationResult: CalculationResult) {
        viewModelScope.launch {
            repository.insert(calculationResult)
        }
    }

    fun delete(calculationResult: CalculationResult) {
        viewModelScope.launch {
            repository.delete(calculationResult)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    private val _exportEvent = MutableLiveData<Uri?>()
    val exportEvent: LiveData<Uri?> = _exportEvent

    fun exportCalculations(context: Context) {
        viewModelScope.launch {
            try {
                val calculations = repository.getAllCalculations()
                val csvExporter = CsvExporter(context)
                val uri = csvExporter.exportCalculationsToCSV(calculations)
                _exportEvent.value = uri
            } catch (e: Exception) {
                e.printStackTrace()
                _exportEvent.value = null
            }
        }
    }

    fun onExportHandled() {
        _exportEvent.value = null
    }
}