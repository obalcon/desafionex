package com.nexdom.estoque.dto;

import java.math.BigDecimal;

public record LucroProdutoDTO(
        Long produtoId,
        String codigo,
        String descricao,
        Long quantidadeTotalSaida,
        BigDecimal lucroTotal
) {
}