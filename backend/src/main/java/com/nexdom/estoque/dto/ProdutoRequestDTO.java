package com.nexdom.estoque.dto;

import com.nexdom.estoque.enums.TipoProduto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "Código é obrigatório")
        String codigo,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Tipo do produto é obrigatório")
        TipoProduto tipoProduto,

        @NotNull(message = "Valor do fornecedor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valorFornecedor
) {
}