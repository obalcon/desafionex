package com.nexdom.estoque.controller;

import com.nexdom.estoque.dto.MovimentoRequestDTO;
import com.nexdom.estoque.dto.MovimentoResponseDTO;
import com.nexdom.estoque.service.MovimentoEstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentos")
public class MovimentoEstoqueController {

    private final MovimentoEstoqueService movimentoService;

    @Autowired
    public MovimentoEstoqueController(MovimentoEstoqueService movimentoService) {
        this.movimentoService = movimentoService;
    }

    @PostMapping
    public ResponseEntity<MovimentoResponseDTO> registrar(@Valid @RequestBody MovimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentoService.registrarMovimento(dto));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<MovimentoResponseDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(movimentoService.listarPorProduto(produtoId));
    }
}