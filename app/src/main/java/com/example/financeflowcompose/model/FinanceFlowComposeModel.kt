package com.example.financeflowcompose.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.financeflowcompose.data.datasource.LocalDatasource

class FinanceFlowComposeModel: ViewModel() {
    val radioOptions = LocalDatasource.getTipos()

    var selectedOption by mutableStateOf(radioOptions[0])
        private set
    var valor by mutableStateOf("")
        private set
    var descricao by mutableStateOf("")
        private set

    private val categoriaReceita = LocalDatasource.getCategoriasReceita()
    private val categoriaDespesa = LocalDatasource.getCategoriasDespesa()

    var currentCategoria by mutableStateOf(categoriaReceita)
        private set
    var selectedCategoria by mutableStateOf(currentCategoria[0])
        private set

    val formasPagamento = LocalDatasource.getFormasPagamento()
    var selectedFormaPagamento by mutableStateOf(formasPagamento[0])
        private set

    var selectedDateMillis by mutableStateOf(System.currentTimeMillis())
        private set

    fun onTipoChange(novoTipo: String) {
        selectedOption = novoTipo
        currentCategoria = if (selectedOption == "Receita") {
            categoriaReceita
        } else {
            categoriaDespesa
        }
        selectedCategoria = currentCategoria[0]
    }

    fun onValorChange(novoValor: String) {
        valor = novoValor
    }

    fun onDescricaoChange(novaDescricao: String) {
        descricao = novaDescricao
    }

    fun onCategoriaChange(novaCategoria: String) {
        selectedCategoria = novaCategoria
    }

    fun onFormaPagamentoChange(novaForma: String) {
        selectedFormaPagamento = novaForma

    }

    fun onDateChange(novoMillis: Long) {
        selectedDateMillis = novoMillis
    }

    fun salvarLancamento() {

        val formaPagamentoFinal = if (selectedOption == "Despesa") selectedFormaPagamento else null

    }
}