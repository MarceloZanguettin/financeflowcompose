package com.example.financeflowcompose.data.datasource

object LocalDatasource {

    fun getTipos(): List<String> {
        return listOf("Receita", "Despesa")
    }

    fun getCategoriasReceita(): List<String> {
        return listOf("Salário", "Freelance", "Vendas", "Investimentos", "Outros")
    }

    fun getCategoriasDespesa(): List<String> {
        return listOf("Alimentação", "Transporte", "Moradia", "Lazer", "Saúde", "Educação", "Outros")
    }

    fun getFormasPagamento(): List<String> {
        return listOf("Dinheiro", "Pix", "Cartão de Crédito", "Cartão de Débito", "Transferência Bancária")
    }
}
