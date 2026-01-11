package com.example.financeflowcompose.model

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.financeflowcompose.data.datasource.LocalDatasource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import java.util.Calendar

class FinanceFlowComposeModel: ViewModel() {
    // Form states
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

    // List and filter states
    private var lancamentos by mutableStateOf<List<Lancamento>>(emptyList())
    var filtroTipo by mutableStateOf("Todos")
        private set
    val todasCategorias = (categoriaReceita + categoriaDespesa).distinct().sorted()
    var filtroCategoria by mutableStateOf("Todas")
        private set
    var filtroData by mutableStateOf<Long?>(null)
        private set

    val lancamentosFiltrados by derivedStateOf {
        lancamentos.filter { lancamento ->
            val tipoOk = (filtroTipo == "Todos" || lancamento.tipo == filtroTipo)
            val categoriaOk = (filtroCategoria == "Todas" || lancamento.categoria == filtroCategoria)
            val dataOk = filtroData == null || run {
                val calendarFiltro = Calendar.getInstance().apply { timeInMillis = filtroData!! }
                val calendarLancamento = Calendar.getInstance().apply { timeInMillis = lancamento.data }
                calendarFiltro.get(Calendar.YEAR) == calendarLancamento.get(Calendar.YEAR) &&
                calendarFiltro.get(Calendar.DAY_OF_YEAR) == calendarLancamento.get(Calendar.DAY_OF_YEAR)
            }
            tipoOk && categoriaOk && dataOk
        }
    }

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

    // Form actions
    fun onTipoChange(novoTipo: String) {
        selectedOption = novoTipo
        currentCategoria = if (selectedOption == "Receita") categoriaReceita else categoriaDespesa
        selectedCategoria = currentCategoria[0]
    }
    fun onValorChange(novoValor: String) { valor = novoValor }
    fun onDescricaoChange(novaDescricao: String) { descricao = novaDescricao }
    fun onCategoriaChange(novaCategoria: String) { selectedCategoria = novaCategoria }
    fun onFormaPagamentoChange(novaForma: String) { selectedFormaPagamento = novaForma }
    fun onDateChange(novoMillis: Long) { selectedDateMillis = novoMillis }

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

        db.collection("lancamentos").add(lancamento)
            .addOnSuccessListener {
                Log.d("FinanceFlowComposeModel", "Documento salvo com ID: ${it.id}")
                onSuccess()
            }
            .addOnFailureListener { e -> Log.w("FinanceFlowComposeModel", "Erro ao salvar documento", e) }
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
        db.collection("lancamentos").document(lancamentoId).delete()
            .addOnSuccessListener { Log.d("FinanceFlowComposeModel", "Documento deletado com sucesso!") }
            .addOnFailureListener { e -> Log.w("FinanceFlowComposeModel", "Erro ao deletar documento", e) }
    }

    // Filter actions
    fun onFiltroTipoChange(novoFiltro: String) {
        filtroTipo = novoFiltro
    }

    fun onFiltroCategoriaChange(novaCategoria: String) {
        filtroCategoria = novaCategoria
    }

    fun onFiltroDataChange(novaData: Long?) {
        filtroData = novaData
    }
}