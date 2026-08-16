package com.nexdom.estoque.config;

import com.nexdom.estoque.dto.MovimentoRequestDTO;
import com.nexdom.estoque.dto.ProdutoRequestDTO;
import com.nexdom.estoque.enums.TipoMovimentacao;
import com.nexdom.estoque.enums.TipoProduto;
import com.nexdom.estoque.service.MovimentoEstoqueService;
import com.nexdom.estoque.service.ProdutoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProdutoService produtoService;
    private final MovimentoEstoqueService movimentoService;

    public DataLoader(ProdutoService produtoService, MovimentoEstoqueService movimentoService) {
        this.produtoService = produtoService;
        this.movimentoService = movimentoService;
    }

    @Override
    public void run(String... args) {
        // ==================== PRODUTO 1 ====================
        var produto1 = produtoService.criar(new ProdutoRequestDTO(
                "PROD001",
                "Notebook Dell Inspiron",
                TipoProduto.ELETRONICO,
                new BigDecimal("2500.00")
        ));

        // ENTRADA +10 (estoque: 10)
        movimentoService.registrarMovimento(new MovimentoRequestDTO(
                produto1.id(),
                TipoMovimentacao.ENTRADA,
                null,
                null,
                10
        ));

        // ENTRADA +5 (estoque: 15)
        movimentoService.registrarMovimento(new MovimentoRequestDTO(
                produto1.id(),
                TipoMovimentacao.ENTRADA,
                null,
                null,
                5
        ));

        // SAÍDA -3 (estoque: 12)
        movimentoService.registrarMovimento(new MovimentoRequestDTO(
                produto1.id(),
                TipoMovimentacao.SAIDA,
                new BigDecimal("10500.00"),
                java.time.LocalDate.now(),
                3
        ));

        // ==================== PRODUTO 2 ====================
        var produto2 = produtoService.criar(new ProdutoRequestDTO(
                "PROD002",
                "Geladeira Brastemp Frost Free",
                TipoProduto.ELETRODOMESTICO,
                new BigDecimal("1800.00")
        ));

        // ENTRADA +5 (estoque: 5)
        movimentoService.registrarMovimento(new MovimentoRequestDTO(
                produto2.id(),
                TipoMovimentacao.ENTRADA,
                null,
                null,
                5
        ));

        // ENTRADA +2 (estoque: 7)
        movimentoService.registrarMovimento(new MovimentoRequestDTO(
                produto2.id(),
                TipoMovimentacao.ENTRADA,
                null,
                null,
                2
        ));

        // SAÍDA -1 (estoque: 6)
        movimentoService.registrarMovimento(new MovimentoRequestDTO(
                produto2.id(),
                TipoMovimentacao.SAIDA,
                new BigDecimal("2500.00"),
                java.time.LocalDate.now(),
                1
        ));

        System.out.println("✅ Dados iniciais carregados com sucesso!");
    }
}