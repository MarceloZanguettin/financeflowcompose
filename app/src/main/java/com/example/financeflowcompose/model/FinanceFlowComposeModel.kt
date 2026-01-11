package com.example.financeflowcompose.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.financeflowcompose.data.datasource.LocalDatasource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

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

    var lancamentos by mutableStateOf<List<Lancamento>>(emptyList())
        private set

    init {
        fetchLancamentos()
    }

    private fun fetchLancamentos() {
        val db = FirebaseFirestore.getInstance()
        db.collection("lancamentos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("FinanceFlowComposeModel", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                lancamentos = snapshot.toObjects<Lancamento>()
                Log.d("FinanceFlowComposeModel", "Fetched ${lancamentos.size} lancamentos")
            }
        }
    }

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

    fun salvarLancamento(onSuccess: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val formaPagamentoFinal = if (selectedOption == "Despesa") selectedFormaPagamento else null

        val lancamento = hashMapOf(
            "tipo" to selectedOption,
            "valor" to valor,
            "descricao" to descricao,
            "categoria" to selectedCategoria,
            "data" to selectedDateMillis,
            "formaPagamento" to formaPagamentoFinal
        )

        db.collection("lancamentos")
            .add(lancamento)
            .addOnSuccessListener { documentReference ->
                Log.d("FinanceFlowComposeModel", "Documento salvo com ID: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("FinanceFlowComposeModel", "Erro ao salvar documento", e)
            }
    }

    fun limparCampos() {
        valor = ""
        descricao = ""
        selectedOption = radioOptions[0]
        currentCategoria = categoriaReceita
        selectedCategoria = currentCategoria[0]
        selectedFormaPagamento = formasPagamento[0]
        selectedDateMillis = System.currentTimeMillis()
    }

    fun deleteLancamento(lancamentoId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("lancamentos").document(lancamentoId)
            .delete()
            .addOnSuccessListener { Log.d("FinanceFlowComposeModel", "Documento deletado com sucesso!") }
            .addOnFailureListener { e -> Log.w("FinanceFlowComposeModel", "Erro ao deletar documento", e) }
    }
}