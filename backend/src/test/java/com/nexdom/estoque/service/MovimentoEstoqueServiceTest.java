package com.nexdom.estoque.service;

import com.nexdom.estoque.dto.MovimentoRequestDTO;
import com.nexdom.estoque.dto.MovimentoResponseDTO;
import com.nexdom.estoque.enums.TipoMovimentacao;
import com.nexdom.estoque.exception.EstoqueInsuficienteException;
import com.nexdom.estoque.model.MovimentoEstoque;
import com.nexdom.estoque.model.Produto;
import com.nexdom.estoque.repository.MovimentoEstoqueRepository;
import com.nexdom.estoque.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MovimentoEstoqueServiceTest {

    @Mock
    private MovimentoEstoqueRepository movimentoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private MovimentoEstoqueService movimentoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .id(1L)
                .codigo("PROD001")
                .descricao("Notebook Dell")
                .valorFornecedor(new BigDecimal("2500.00"))
                .quantidadeEstoque(10)
                .build();
    }

    @Test
    void deveRegistrarEntradaNoEstoque() {
        MovimentoRequestDTO dto = new MovimentoRequestDTO(1L, TipoMovimentacao.ENTRADA, null, null, 5);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);
        Mockito.when(movimentoRepository.save(Mockito.any(MovimentoEstoque.class))).thenAnswer(i -> i.getArgument(0));

        MovimentoResponseDTO response = movimentoService.registrarMovimento(dto);

        Assertions.assertEquals(15, produto.getQuantidadeEstoque());
        Assertions.assertEquals(TipoMovimentacao.ENTRADA, response.tipoMovimentacao());
        Mockito.verify(produtoRepository).save(produto);
    }

    @Test
    void deveRegistrarSaidaNoEstoque() {
        MovimentoRequestDTO dto = new MovimentoRequestDTO(1L, TipoMovimentacao.SAIDA, new BigDecimal("10500.00"), java.time.LocalDate.now(), 3);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);
        Mockito.when(movimentoRepository.save(Mockito.any(MovimentoEstoque.class))).thenAnswer(i -> i.getArgument(0));

        MovimentoResponseDTO response = movimentoService.registrarMovimento(dto);

        Assertions.assertEquals(7, produto.getQuantidadeEstoque());
        Assertions.assertEquals(TipoMovimentacao.SAIDA, response.tipoMovimentacao());
        Mockito.verify(produtoRepository).save(produto);
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        MovimentoRequestDTO dto = new MovimentoRequestDTO(1L, TipoMovimentacao.SAIDA, new BigDecimal("1000.00"), java.time.LocalDate.now(), 999);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        EstoqueInsuficienteException exception = Assertions.assertThrows(EstoqueInsuficienteException.class, () -> movimentoService.registrarMovimento(dto));

        Assertions.assertTrue(exception.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        MovimentoRequestDTO dto = new MovimentoRequestDTO(99L, TipoMovimentacao.ENTRADA, null, null, 5);

        Mockito.when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> movimentoService.registrarMovimento(dto));
    }

    @Test
    void deveListarMovimentosPorProduto() {
        MovimentoEstoque movimento = MovimentoEstoque.builder()
                .id(1L)
                .produto(produto)
                .tipoMovimentacao(TipoMovimentacao.ENTRADA)
                .quantidadeMovimentada(5)
                .build();

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(movimentoRepository.findByProduto(produto)).thenReturn(List.of(movimento));

        List<MovimentoResponseDTO> resultado = movimentoService.listarPorProduto(1L);

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Notebook Dell", resultado.get(0).produtoDescricao());
    }
}