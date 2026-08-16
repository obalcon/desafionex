package com.nexdom.estoque.dto;

import com.nexdom.estoque.enums.TipoMovimentacao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimentoRequestDTO(
        @NotNull(message = "ID do produto é obrigatório")
        Long produtoId,

        @NotNull(message = "Tipo de movimentação é obrigatório")
        TipoMovimentacao tipoMovimentacao,

        @DecimalMin(value = "0.01", message = "Valor de venda deve ser maior que zero")
        BigDecimal valorVenda,

        LocalDate dataVenda,

        @NotNull(message = "Quantidade movimentada é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
        Integer quantidadeMovimentada
) {
}