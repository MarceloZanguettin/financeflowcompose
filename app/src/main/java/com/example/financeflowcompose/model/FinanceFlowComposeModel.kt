package com.example.financeflowcompose.model

import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FinanceFlowComposeModel: ViewModel() {
    val radioOptions = listOf("Receita", "Despesa")
    var selectedOption by mutableStateOf(radioOptions[0])
        private set
    var valor by mutableStateOf("")
        private set
    var descricao by mutableStateOf("")
        private set

    val categoriaReceita = listOf("Salário", "Freelance", "Outros")
    val categoriaDespesa = listOf("Alimentação", "Transporte", "Educação", "Outros")
    var expanded by mutableStateOf(false)
        private set
    var currentCategoria by mutableStateOf(categoriaReceita)
        private set
    var selectedCategoria by mutableStateOf(currentCategoria[0])
        private set

    var showDataPicker by mutableStateOf(false)
        private set
    var selectedDateMillis by mutableStateOf(Date().time)
        private set

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun onDateSelected(millis: Long?) {
        millis?.let {
            selectedDateMillis = it
        }
    }

    val selectedDateFormatted: String
        get() = dateFormatter.format(Date(selectedDateMillis))
}