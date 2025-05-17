package com.example.estoque.service;

import com.example.estoque.domain.ItemPedido;
import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;
import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    private ProdutoRepository produtoRepository;
    private ProdutoService produtoService;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setup() {
        produtoRepository = mock(ProdutoRepository.class);
        produtoService = new ProdutoService(produtoRepository);

        produto1 = new Produto(1L,"",5);
        produto1.setNome("Produto 1");
        produto1.setQtd(10);
        
        produto2 = new Produto(5L,"",5);
        produto2.setNome("Produto 2");
        produto2.setQtd(20);
    }

    @Test
    void atualizarEstoque_DeveAtualizarQuantidade_QuandoPedidoValido() {
        Pedido pedido = new Pedido();
        pedido.setItens(List.of(new ItemPedido(1L, 5)));
        
        ProdutoEntity produtoEntity = new ProdutoEntity(produto1);
        
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoEntity));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenAnswer(i -> i.getArgument(0));

        produtoService.atualizarEstoque(pedido);

        // Verify that the repository was called correctly
        verify(produtoRepository).findById(1L);
        verify(produtoRepository).save(argThat(entity -> entity.getQtd() == 5));
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void atualizarEstoque_DeveLancarExcecao_QuandoProdutoNaoEncontrado() {
        Pedido pedido = new Pedido();
        pedido.setItens(List.of(new ItemPedido(1L, 5)));

        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> produtoService.atualizarEstoque(pedido));
        
        verify(produtoRepository).findById(1L);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void atualizarEstoque_DeveLancarForaDeEstoqueException_QuandoEstoqueInsuficiente() {
        Pedido pedido = new Pedido();
        pedido.setItens(List.of(new ItemPedido(1L, 15))); // Requesting 15 when only 10 available
        
        ProdutoEntity produtoEntity = new ProdutoEntity(produto1); // Has 10 items in stock
        
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoEntity));

        assertThrows(ForaDeEstoqueException.class, () -> produtoService.atualizarEstoque(pedido));

        verify(produtoRepository).findById(1L);
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void cadastrarProduto_DeveAtualizarEstoque_QuandoProdutoJaExiste() {
        ProdutoEntity produtoEntity = new ProdutoEntity(produto1);
        
        when(produtoRepository.findByNome(produto1.getNome())).thenReturn(produtoEntity);

        produtoService.cadastrarProduto(produto1);

        verify(produtoRepository).save(produtoEntity);
        verify(produtoRepository).findByNome(produto1.getNome());
        verifyNoMoreInteractions(produtoRepository);
    }

    @Test
    void cadastrarProduto_DeveSalvarNovoProduto_QuandoProdutoNaoExiste() {
        when(produtoRepository.findByNome(produto2.getNome())).thenReturn(null);

        produtoService.cadastrarProduto(produto2);

        verify(produtoRepository).save(any(ProdutoEntity.class));
        verify(produtoRepository).findByNome(produto2.getNome());
        verifyNoMoreInteractions(produtoRepository);
    }
}