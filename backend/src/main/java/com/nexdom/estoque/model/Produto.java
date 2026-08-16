package com.nexdom.estoque.model;

import com.nexdom.estoque.enums.TipoProduto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProduto tipoProduto;

    @Column(name = "valor_fornecedor", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorFornecedor;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;
}