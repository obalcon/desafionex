package com.nexdom.estoque.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexdom.estoque.dto.LucroProdutoDTO;
import com.nexdom.estoque.dto.ProdutoRequestDTO;
import com.nexdom.estoque.dto.ProdutoResponseDTO;
import com.nexdom.estoque.dto.ProdutoTipoDTO;
import com.nexdom.estoque.enums.TipoProduto;
import com.nexdom.estoque.model.MovimentoEstoque;
import com.nexdom.estoque.model.Produto;
import com.nexdom.estoque.repository.MovimentoEstoqueRepository;
import com.nexdom.estoque.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentoEstoqueRepository movimentoRepository;

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        if (produtoRepository.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException("Já existe produto com este código");
        }

        Produto produto = Produto.builder()
                .codigo(dto.codigo())
                .descricao(dto.descricao())
                .tipoProduto(dto.tipoProduto())
                .valorFornecedor(dto.valorFornecedor())
                .quantidadeEstoque(0)
                .build();

        Produto salvo = produtoRepository.save(produto);
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        return toResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        produto.setDescricao(dto.descricao());
        produto.setTipoProduto(dto.tipoProduto());
        produto.setValorFornecedor(dto.valorFornecedor());

        Produto atualizado = produtoRepository.save(produto);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        List<MovimentoEstoque> movimentos = movimentoRepository.findByProduto(produto);
        if (!movimentos.isEmpty()) {
            throw new IllegalArgumentException(
                "Não é possível excluir o produto '" + produto.getCodigo() + 
                "' pois existem movimentações de estoque vinculadas a ele."
            );
        }

        produtoRepository.delete(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoTipoDTO> consultarPorTipo(TipoProduto tipo) {
        return produtoRepository.findByTipoProduto(tipo)
                .stream()
                .map(p -> new ProdutoTipoDTO(
                        p.getId(),
                        p.getCodigo(),
                        p.getDescricao(),
                        p.getTipoProduto(),
                        p.getQuantidadeEstoque(),
                        movimentoRepository.countSaidasByProdutoId(p.getId())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public LucroProdutoDTO consultarLucro(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        Long quantidadeSaida = movimentoRepository.countSaidasByProdutoId(produtoId);
        BigDecimal lucroTotal = movimentoRepository.calcularLucroTotal(produtoId);

        return new LucroProdutoDTO(
                produto.getId(),
                produto.getCodigo(),
                produto.getDescricao(),
                quantidadeSaida,
                lucroTotal
        );
    }

    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getCodigo(),
                produto.getDescricao(),
                produto.getTipoProduto(),
                produto.getValorFornecedor(),
                produto.getQuantidadeEstoque()
        );
    }
}