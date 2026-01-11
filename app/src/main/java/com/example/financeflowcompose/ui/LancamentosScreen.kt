package com.example.financeflowcompose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financeflowcompose.model.FinanceFlowComposeModel
import com.example.financeflowcompose.model.Lancamento
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LancamentosScreen(model: FinanceFlowComposeModel) {
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedCategoria by remember { mutableStateOf(false) }
    var showDataPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDataPicker) {
        DatePickerDialog(
            onDismissRequest = { showDataPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDataPicker = false
                    model.onFiltroDataChange(datePickerState.selectedDateMillis)
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDataPicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Meus Lançamentos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sumário
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Receitas", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(model.totalReceitasFiltradas),
                    color = Color(0xFF008000),
                    fontWeight = FontWeight.Bold
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Despesas", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(model.totalDespesasFiltradas),
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Filtros
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Filtro por Tipo
            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = model.filtroTipo,
                    onValueChange = {},
                    label = { Text("Tipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false },
                ) {
                    (listOf("Todos") + model.radioOptions).forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                model.onFiltroTipoChange(selectionOption)
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            // Filtro por Categoria
            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = model.filtroCategoria,
                    onValueChange = {},
                    label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                )
                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false },
                ) {
                    (listOf("Todas") + model.todasCategorias).forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                model.onFiltroCategoriaChange(selectionOption)
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }
        }

        // Filtro por Data
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val formattedDate = remember(model.filtroData) {
                model.filtroData?.let {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                } ?: "Todas as datas"
            }
            OutlinedTextField(
                value = formattedDate,
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.weight(1f),
                label = { Text("Data") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.DateRange,
                        "Selecionar data",
                        modifier = Modifier.clickable { showDataPicker = true }
                    ) 
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { model.onFiltroDataChange(null) }) {
                Icon(Icons.Default.Clear, contentDescription = "Limpar filtro de data")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(model.lancamentosFiltrados) { lancamento ->
                LancamentoItem(
                    lancamento = lancamento,
                    onDelete = { model.deleteLancamento(lancamento.id) }
                )
            }
        }
    }
}

@Composable
fun LancamentoItem(lancamento: Lancamento, onDelete: () -> Unit) {
    val valorColor = if (lancamento.tipo == "Receita") Color(0xFF008000) else Color.Red
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(lancamento.data))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = lancamento.descricao, fontWeight = FontWeight.Bold)
                Text(text = formattedDate, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text =  NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(lancamento.valor.replace(',', '.').toDoubleOrNull() ?: 0.0),
                color = valorColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar Lançamento")
            }
        }
    }
}
