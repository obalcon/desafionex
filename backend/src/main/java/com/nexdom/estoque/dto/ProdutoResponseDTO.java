package com.nexdom.estoque.dto;

import com.nexdom.estoque.enums.TipoProduto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String codigo,
        String descricao,
        TipoProduto tipoProduto,
        BigDecimal valorFornecedor,
        Integer quantidadeEstoque
) {
}