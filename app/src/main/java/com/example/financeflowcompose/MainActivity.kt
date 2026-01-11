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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeflowcompose.model.FinanceFlowComposeModel
import com.example.financeflowcompose.ui.theme.FinanceflowcomposeTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceflowcomposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlowFinanceScreen(
                        model = viewModel(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowFinanceScreen(modifier: Modifier = Modifier, model: FinanceFlowComposeModel) {
    var expandedCategoria by rememberSaveable { mutableStateOf(false) }
    var showDataPicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = model.selectedDateMillis)

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            model.onDateChange(millis)
        }
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
                model.radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .selectable(
                                selected = (text == model.selectedOption),
                                onClick = { model.onTipoChange(text)},
                                role = Role.RadioButton
                            )
                            .padding(all = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == model.selectedOption),
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
        if (model.selectedOption == "Despesa") {
            PanelDespesa(
                formasPagamento = model.formasPagamento,
                selectedForma = model.selectedFormaPagamento,
                onFormaChange = { novaForma -> model.onFormaPagamentoChange(novaForma) }

            )
        }
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Valor:",
                modifier = Modifier.padding(all = 16.dp)
            )
            OutlinedTextField(
                value = model.valor,
                onValueChange = { model.onValorChange(it)},
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
                value = model.descricao,
                onValueChange = { model.onDescricaoChange(it)},
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
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria },
                modifier = Modifier.fillMaxWidth()

            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    value = model.selectedCategoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false },
                ){
                    model.currentCategoria.forEach { selectionOptions ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOptions) },
                            onClick = {
                                model.onCategoriaChange(selectionOptions)
                                expandedCategoria = false
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
            val formattedDate = remember(model.selectedDateMillis) {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(model.selectedDateMillis))
            }
            Text(
                text = "Data Lanç.:",
                modifier = Modifier.padding(end = 16.dp)
            )
            OutlinedTextField(
                value = formattedDate,
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
        PanelButtons(onSalvarClick = { model.salvarLancamento()})
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun FlowFinanceScreenPreview() {
    FinanceflowcomposeTheme {
        FlowFinanceScreen(model = FinanceFlowComposeModel())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelDespesa(
    formasPagamento: List<String>,
    selectedForma: String,
    onFormaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedPagamento by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Pagamento:", modifier = Modifier.padding(end = 16.dp))
        ExposedDropdownMenuBox(
            expanded = expandedPagamento,
            onExpandedChange = { expandedPagamento = !expandedPagamento },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                value = selectedForma,
                onValueChange = {},
                readOnly = true,
                label = { Text("Forma de Pagamento") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPagamento) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expandedPagamento,
                onDismissRequest = { expandedPagamento = false },
            ) {
                formasPagamento.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            onFormaChange(selectionOption) // Notifica a função recebida
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
        PanelDespesa(
            formasPagamento = listOf("Dinheiro", "PIX"),
            selectedForma = "Dinheiro",
            onFormaChange = {}
        )
    }
}

@Composable
fun PanelButtons(
    onSalvarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { /* TODO: Ação de Ver Lançamentos */ },
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        ) {
            Text(text = "Ver Lançamentos")
        }
        Button(
            onClick = onSalvarClick, // Chama a função recebida
            modifier = Modifier.weight(1f).padding(start = 8.dp)
        ) {
            Text(text = "Lançar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PanelButtonsPreview() {
    FinanceflowcomposeTheme {
        PanelButtons(onSalvarClick = {})
    }
}