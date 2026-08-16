package com.nexdom.estoque.model;

import com.nexdom.estoque.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movimentos_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class MovimentoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @Column(name = "valor_venda", precision = 19, scale = 2)
    private BigDecimal valorVenda;

    @Column(name = "data_venda")
    private LocalDate dataVenda;

    @Column(name = "quantidade_movimentada", nullable = false)
    private Integer quantidadeMovimentada;
}