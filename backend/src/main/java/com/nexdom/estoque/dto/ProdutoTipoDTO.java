package com.nexdom.estoque.dto;

import com.nexdom.estoque.enums.TipoProduto;

public record ProdutoTipoDTO(
        Long id,
        String codigo,
        String descricao,
        TipoProduto tipoProduto,
        Integer quantidadeDisponivel,
        Long quantidadeSaida
) {
}