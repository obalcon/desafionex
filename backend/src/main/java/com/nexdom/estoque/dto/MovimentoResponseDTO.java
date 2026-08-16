package com.nexdom.estoque.dto;

import com.nexdom.estoque.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimentoResponseDTO(
        Long id,
        Long produtoId,
        String produtoDescricao,
        TipoMovimentacao tipoMovimentacao,
        BigDecimal valorVenda,
        LocalDate dataVenda,
        Integer quantidadeMovimentada
) {
}