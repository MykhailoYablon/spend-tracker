package com.example.spendtracker.model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendtracker.ds.entity.FundDepositResult
import com.example.spendtracker.ds.repository.FundDepositRepository
import com.example.spendtracker.service.CsvExporter
import kotlinx.coroutines.launch

class FundDepositViewModel(private val repository: FundDepositRepository) : ViewModel() {

    val allFundDeposits = repository.getAll()

    fun add(fundDepositResult: FundDepositResult) {
        viewModelScope.launch {
            repository.insert(fundDepositResult)
        }
    }

    fun delete(fundDepositResult: FundDepositResult) {
        viewModelScope.launch {
            repository.delete(fundDepositResult)
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
                val calculations = repository.getAllFundDeposits()
                val csvExporter = CsvExporter(context)
                val uri = csvExporter.exportFundDepositsToCSV(calculations)
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