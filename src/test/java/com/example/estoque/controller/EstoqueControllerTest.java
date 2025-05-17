package com.example.estoque.controller;

import com.example.estoque.domain.ItemPedido;
import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstoqueController.class)
@AutoConfigureMockMvc
class EstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProdutoService produtoService;
    
    private Produto produtoMock;
    private Pedido pedidoMock;

    @BeforeEach
    void setUp() {
        produtoMock = new Produto("1", "Produto 1", "Descrição 1", 10.0, 10);
        
        pedidoMock = new Pedido();
        ItemPedido item = new ItemPedido(1L, 2);
        pedidoMock.setItens(List.of(item));
    }

    @Test
    public void aoListarTodosDeveRetornar200() throws Exception {
        // Arrange
        List<Produto> produtos = List.of(produtoMock);
        when(produtoService.encontrarTodos()).thenReturn(produtos);

        // Act & Assert
        mockMvc.perform(get("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Produto 1")));
    }

    @Test
    void buscarTodos_DeveRetornarListaDeProdutos() throws Exception {
        // Arrange
        Produto produto2 = new Produto("2", "Produto 2", "Descrição 2", 20.0, 20);
        List<Produto> produtos = Arrays.asList(produtoMock, produto2);
        when(produtoService.encontrarTodos()).thenReturn(produtos);

        // Act & Assert
        mockMvc.perform(get("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("Produto 1")))
                .andExpect(jsonPath("$[1].nome", is("Produto 2")));
    }

    @Test
    void buscarPorId_DeveRetornarProduto_QuandoExiste() throws Exception {
        // Arrange
        when(produtoService.buscarPorId(1L)).thenReturn(produtoMock);

        // Act & Assert
        mockMvc.perform(get("/api/produtos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Produto 1")))
                .andExpect(jsonPath("$.qtd", is(10)));
    }

    @Test
    void buscarPorId_DeveRetornarNotFound_QuandoNaoExiste() throws Exception {
        // Arrange
        when(produtoService.buscarPorId(1L)).thenThrow(new ProdutoNaoEncontradoException(1L));

        // Act & Assert
        mockMvc.perform(get("/api/produtos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void cadastrarProduto_DeveRetornarCreated() throws Exception {
        // Arrange
        doNothing().when(produtoService).cadastrarProduto(any(Produto.class));

        // Act & Assert
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoMock)))
                .andExpect(status().isCreated());
    }

    @Test
    void cadastrarProduto_DeveRetornarBadRequest_QuandoProdutoInvalido() throws Exception {
        // Arrange
        Produto produtoInvalido = new Produto();
        produtoInvalido.setNome(null); // Nome inválido
        produtoInvalido.setQtd(0);     // Quantidade inválida
        produtoInvalido.setDescricao(""); // Descrição inválida
        
        doThrow(new DadosProdutoInvalidosException("nome"))
                .when(produtoService).cadastrarProduto(any(Produto.class));

        // Act & Assert
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cadastrarProduto_DeveRetornarBadRequest_QuandoProdutoJaExiste() throws Exception {
        // Arrange
        doThrow(new ProdutoJaExistenteException("Produto 1"))
                .when(produtoService).cadastrarProduto(any(Produto.class));

        // Act & Assert
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoMock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizarEstoque_DeveRetornarOk_QuandoPedidoValido() throws Exception {
        // Arrange
        doNothing().when(produtoService).atualizarEstoque(any(Pedido.class));

        // Act & Assert
        mockMvc.perform(post("/api/estoque/atualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoMock)))
                .andExpect(status().isOk());
    }

    @Test
    void atualizarEstoque_DeveRetornarBadRequest_QuandoForaDeEstoque() throws Exception {
        // Arrange
        doThrow(new ForaDeEstoqueException("Produto fora de estoque"))
                .when(produtoService).atualizarEstoque(any(Pedido.class));

        // Act & Assert
        mockMvc.perform(post("/api/estoque/atualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoMock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizarEstoque_DeveRetornarNotFound_QuandoProdutoNaoEncontrado() throws Exception {
        // Arrange
        doThrow(new ProdutoNaoEncontradoException(1L))
                .when(produtoService).atualizarEstoque(any(Pedido.class));

        // Act & Assert
        mockMvc.perform(post("/api/estoque/atualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoMock)))
                .andExpect(status().isNotFound());
    }

    // Classes de exceção movidas para arquivos separados
    public static class ProdutoNaoEncontradoException extends RuntimeException {
        public ProdutoNaoEncontradoException(Long id) {
            super("Produto com ID " + id + " não encontrado");
        }
        public ProdutoNaoEncontradoException(String nome) {
            super("Produto com nome " + nome + " não encontrado");
        }
    }

    public static class ProdutoJaExistenteException extends RuntimeException {
        public ProdutoJaExistenteException(String nome) {
            super("Já existe um produto com o nome: " + nome);
        }
    }

    public static class DadosProdutoInvalidosException extends RuntimeException {
        public DadosProdutoInvalidosException(String campo) {
            super("Campo inválido: " + campo);
        }
    }
}