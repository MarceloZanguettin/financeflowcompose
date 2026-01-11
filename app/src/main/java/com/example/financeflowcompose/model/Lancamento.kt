package com.example.financeflowcompose.model

import com.google.firebase.firestore.DocumentId

data class Lancamento(
    @DocumentId val id: String = "",
    val tipo: String = "",
    val valor: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val data: Long = 0L,
    val formaPagamento: String? = null
)
