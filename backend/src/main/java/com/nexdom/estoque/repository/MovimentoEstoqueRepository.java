package com.nexdom.estoque.repository;

import com.nexdom.estoque.enums.TipoMovimentacao;
import com.nexdom.estoque.model.MovimentoEstoque;
import com.nexdom.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {

    List<MovimentoEstoque> findByProduto(Produto produto);

    List<MovimentoEstoque> findByProdutoAndTipoMovimentacao(Produto produto, TipoMovimentacao tipoMovimentacao);

    @Query("SELECT COALESCE(SUM(m.quantidadeMovimentada), 0) FROM MovimentoEstoque m " +
           "WHERE m.produto.id = :produtoId AND m.tipoMovimentacao = 'SAIDA'")
    Long countSaidasByProdutoId(@Param("produtoId") Long produtoId);

    @Query("SELECT COALESCE(SUM(m.valorVenda - (m.produto.valorFornecedor * m.quantidadeMovimentada)), 0) " +
           "FROM MovimentoEstoque m " +
           "WHERE m.produto.id = :produtoId AND m.tipoMovimentacao = 'SAIDA'")
    BigDecimal calcularLucroTotal(@Param("produtoId") Long produtoId);
}