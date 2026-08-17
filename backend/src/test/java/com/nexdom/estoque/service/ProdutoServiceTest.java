package com.nexdom.estoque.service;

import com.nexdom.estoque.dto.LucroProdutoDTO;
import com.nexdom.estoque.dto.ProdutoRequestDTO;
import com.nexdom.estoque.dto.ProdutoResponseDTO;
import com.nexdom.estoque.dto.ProdutoTipoDTO;
import com.nexdom.estoque.enums.TipoProduto;
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
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MovimentoEstoqueRepository movimentoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .id(1L)
                .codigo("PROD001")
                .descricao("Notebook Dell")
                .tipoProduto(TipoProduto.ELETRONICO)
                .valorFornecedor(new BigDecimal("2500.00"))
                .quantidadeEstoque(10)
                .build();
    }

    @Test
    void deveCriarProdutoComSucesso() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO("PROD002", "Geladeira", TipoProduto.ELETRODOMESTICO, new BigDecimal("1800.00"));

        Mockito.when(produtoRepository.existsByCodigo("PROD002")).thenReturn(false);
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);

        ProdutoResponseDTO response = produtoService.criar(dto);

        Assertions.assertNotNull(response);
        Mockito.verify(produtoRepository).save(Mockito.any(Produto.class));
    }

    @Test
    void deveLancarExcecaoQuandoCodigoJaExiste() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO("PROD001", "Notebook", TipoProduto.ELETRONICO, new BigDecimal("2500.00"));

        Mockito.when(produtoRepository.existsByCodigo("PROD001")).thenReturn(true);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> produtoService.criar(dto));
        Assertions.assertEquals("Já existe produto com este código", exception.getMessage());
    }

    @Test
    void deveListarTodosOsProdutos() {
        Mockito.when(produtoRepository.findAll()).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> resultado = produtoService.listarTodos();

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("PROD001", resultado.get(0).codigo());
    }

    @Test
    void deveBuscarProdutoPorId() {
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoResponseDTO response = produtoService.buscarPorId(1L);

        Assertions.assertEquals("PROD001", response.codigo());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        Mockito.when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> produtoService.buscarPorId(99L));
    }

    @Test
    void deveAtualizarProduto() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO("PROD001", "Notebook Atualizado", TipoProduto.ELETRONICO, new BigDecimal("2700.00"));

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);

        ProdutoResponseDTO response = produtoService.atualizar(1L, dto);

        Assertions.assertEquals("Notebook Atualizado", response.descricao());
    }

    @Test
    void deveDeletarProdutoSemMovimentos() {
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(movimentoRepository.findByProduto(produto)).thenReturn(List.of());

        produtoService.deletar(1L);

        Mockito.verify(produtoRepository).delete(produto);
    }

    @Test
    void deveConsultarLucroDoProduto() {
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(movimentoRepository.countSaidasByProdutoId(1L)).thenReturn(5L);
        Mockito.when(movimentoRepository.calcularLucroTotal(1L)).thenReturn(new BigDecimal("3500.00"));

        LucroProdutoDTO lucro = produtoService.consultarLucro(1L);

        Assertions.assertEquals(5L, lucro.quantidadeTotalSaida());
        Assertions.assertEquals(new BigDecimal("3500.00"), lucro.lucroTotal());
    }

    @Test
    void deveConsultarProdutosPorTipo() {
        Mockito.when(produtoRepository.findByTipoProduto(TipoProduto.ELETRONICO)).thenReturn(List.of(produto));
        Mockito.when(movimentoRepository.countSaidasByProdutoId(1L)).thenReturn(3L);

        List<ProdutoTipoDTO> resultado = produtoService.consultarPorTipo(TipoProduto.ELETRONICO);

        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("PROD001", resultado.get(0).codigo());
        Assertions.assertEquals(3L, resultado.get(0).quantidadeSaida());
    }
}