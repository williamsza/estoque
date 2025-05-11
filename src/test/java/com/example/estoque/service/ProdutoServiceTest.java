package com.example.estoque.service;

import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock
    private ProdutoRepository repository;
    @InjectMocks
    private ProdutoService service;

    private Produto produto1;
    private Produto produto2;

    public void setup() {
        produto1 = TestsUtils.criaProduto(1L, "Produto Test 1", 10);
        produto1 = TestsUtils.criaProduto(2L, "Produto Test 2", 20);
    }

    public void atualizarEstoque_DeveAtualizarQuantidade_QuandoPedidoValido() throws ForaDeEstoqueException {
        Pedido pedido = TestsUtils.CriarPedidoComUmItem(1L,3);

    }

    @Test
    void cadastrarProduto() {
    }

    @Test
    void encontrarTodos() {
    }

    @Test
    void atualizarEstoque() {
    }

    @Test
    void encontrarPorNome() {
    }
}