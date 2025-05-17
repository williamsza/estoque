package com.example.estoque.controller;

import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EstoqueController {

    ProdutoService service;

    public EstoqueController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping("/produtos")
    public ResponseEntity<String> cadastraProduto(@RequestBody Produto produto) {
        service.cadastrarProduto(produto);
        return ResponseEntity.status(201).body("Cadastrado com Sucesso");
    }

    @GetMapping("produtos/{id}")
    public ResponseEntity<List<Produto>> listarProdutos() {
        return ResponseEntity.ok().body(service.encontrarTodos());
    }

    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        try {
            Produto produto = service.buscarPorId(id);
            return ResponseEntity.ok(produto);
        } catch (ForaDeEstoqueException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/produtos")
    public ResponseEntity<Produto> buscaProduto(@PathVariable String nome) {
        return ResponseEntity.ok().body(service.encontrarPorNome(nome));
    }

    @PostMapping("/estoque/atualizar")
    public ResponseEntity<String> atualizarEstoque(@RequestBody Pedido pedido) {
        try {
            service.atualizarEstoque(pedido);
            return ResponseEntity.ok("Estoque atualizado com sucesso");
        } catch (ForaDeEstoqueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
                    //.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
