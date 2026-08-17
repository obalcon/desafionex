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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimentoEstoqueService {

    private final MovimentoEstoqueRepository movimentoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public MovimentoResponseDTO registrarMovimento(MovimentoRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        int estoqueAtual = produto.getQuantidadeEstoque();
        int quantidade = dto.quantidadeMovimentada();

        if (dto.tipoMovimentacao() == TipoMovimentacao.SAIDA) {
            if (estoqueAtual < quantidade) {
                throw new EstoqueInsuficienteException(
                    String.format("Estoque insuficiente para o produto %s. Disponível: %d, Solicitado: %d",
                        produto.getCodigo(), estoqueAtual, quantidade)
                );
            }
            produto.setQuantidadeEstoque(estoqueAtual - quantidade);
        } else {
            produto.setQuantidadeEstoque(estoqueAtual + quantidade);
        }

        produtoRepository.save(produto);

        MovimentoEstoque movimento = MovimentoEstoque.builder()
                .produto(produto)
                .tipoMovimentacao(dto.tipoMovimentacao())
                .valorVenda(dto.tipoMovimentacao() == TipoMovimentacao.ENTRADA
                        ? null
                        : dto.valorVenda())
                .dataVenda(dto.tipoMovimentacao() == TipoMovimentacao.ENTRADA
                        ? null
                        : (dto.dataVenda() != null ? dto.dataVenda() : LocalDate.now(java.time.ZoneId.of("America/Sao_Paulo"))))
                .quantidadeMovimentada(quantidade)
                .build();

        MovimentoEstoque salvo = movimentoRepository.save(movimento);
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<MovimentoResponseDTO> listarPorProduto(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        return movimentoRepository.findByProduto(produto)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private MovimentoResponseDTO toResponseDTO(MovimentoEstoque movimento) {
        return new MovimentoResponseDTO(
                movimento.getId(),
                movimento.getProduto().getId(),
                movimento.getProduto().getDescricao(),
                movimento.getTipoMovimentacao(),
                movimento.getValorVenda(),
                movimento.getDataVenda(),
                movimento.getQuantidadeMovimentada()
        );
    }
}