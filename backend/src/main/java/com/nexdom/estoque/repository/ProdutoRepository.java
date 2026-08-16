package com.nexdom.estoque.repository;

import com.nexdom.estoque.enums.TipoProduto;
import com.nexdom.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigo(String codigo);

    List<Produto> findByTipoProduto(TipoProduto tipoProduto);

    boolean existsByCodigo(String codigo);
}