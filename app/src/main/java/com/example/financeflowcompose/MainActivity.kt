package com.example.financeflowcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.saveable.rememberSaveable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.example.financeflowcompose.ui.theme.FinanceflowcomposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceflowcomposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlowFinanceScreen(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowFinanceScreen(name: String, modifier: Modifier = Modifier) {
    val radioOptions = listOf("Receita", "Despesa")
    var selectedOption by rememberSaveable { mutableStateOf(radioOptions[0]) }
    var valor by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }

    val categoriaReceita = listOf("Salário", "Freelance", "Outros")
    val categoriaDespesa = listOf("Alimentação", "Transporte", "Educação", "Outros")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var currentCategoria by rememberSaveable { mutableStateOf(categoriaReceita) }
    var selectedCategoria by rememberSaveable { mutableStateOf(currentCategoria[0]) }

    var showDataPicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Date().time)
    val selectedDate = datePickerState.selectedDateMillis?.let {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
    } ?: "Selecione a data"



    LaunchedEffect(selectedOption) {
        currentCategoria = if (selectedOption == "Receita") {
            categoriaReceita
        } else {
            categoriaDespesa
        }
        selectedCategoria = currentCategoria[0]
    }

    if (showDataPicker) {
        DatePickerDialog(
            onDismissRequest = { showDataPicker = false },
            confirmButton = {
                TextButton(onClick = { showDataPicker = false }) {
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
        return
    }

    Column (modifier = modifier) {
        Row (
            Modifier
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tipo:",
            )
            Row(
                Modifier
                    .selectableGroup()
                    .padding(horizontal = 16.dp)
            ) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { selectedOption = text },
                                role = Role.RadioButton
                            )
                            .padding(all = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
        if (selectedOption == "Despesa") { PanelDespesa() }
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Valor:",
                modifier = Modifier.padding(all = 16.dp)
            )
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("R$ 0,00") },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            )
        }
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Descrição:",
                modifier = Modifier.padding(all = 16.dp)
            )
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Ex.: Salário, Mercado") },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categoria:",
                modifier = Modifier.padding(end = 16.dp)
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()

            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    value = selectedCategoria,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ){
                    currentCategoria.forEach { selectionOptions ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOptions) },
                            onClick = {
                                selectedCategoria = selectionOptions
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Data Lanç.:",
                modifier = Modifier.padding(end = 16.dp)
            )
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Data de Lançamento") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.DateRange,
                        "Selecionar data",
                        modifier = Modifier.clickable { showDataPicker = true }
                    ) },
            )
        }
        PanelButtons()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun FlowFinanceScreenPreview() {
    FinanceflowcomposeTheme {
        FlowFinanceScreen("Android")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelDespesa( modifier: Modifier = Modifier) {
    val formasPagamento = listOf("Cartão de Crédito", "Cartão de Débito", "Dinheiro", "PIX", "Transferencia Bancaria")
    var expandedPagamento by rememberSaveable { mutableStateOf(false) }
    var selectedFormaPagamento by rememberSaveable { mutableStateOf(formasPagamento[0]) }
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pagamento:",
            modifier = Modifier.padding(end = 16.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expandedPagamento,
            onExpandedChange = { expandedPagamento = !expandedPagamento },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                value = selectedFormaPagamento,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPagamento) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expandedPagamento,
                onDismissRequest = { expandedPagamento = false },
            ){
                formasPagamento.forEach { selectionOptions ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOptions) },
                        onClick = {
                            selectedFormaPagamento = selectionOptions
                            expandedPagamento = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PanelDespesaPreview() {
    FinanceflowcomposeTheme {
        PanelDespesa()
    }
}

@Composable
fun PanelButtons(modifier: Modifier = Modifier) {
    Row (Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Button(
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .padding(all = 8.dp)
        ){
            Text(text = "Ver Lançamentos")
        }
        Button(
            onClick = { },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(text = "Lançar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PanelButtonsPreview() {
    FinanceflowcomposeTheme {
        PanelButtons()
    }
}