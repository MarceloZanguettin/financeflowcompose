package com.example.financeflowcompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financeflowcompose.model.FinanceFlowComposeModel
import com.example.financeflowcompose.model.Lancamento
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LancamentosScreen(model: FinanceFlowComposeModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Meus Lançamentos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(model.lancamentos) { lancamento ->
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
                text = "R$ ${lancamento.valor}",
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
