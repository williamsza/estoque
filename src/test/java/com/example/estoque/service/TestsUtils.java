package com.example.estoque.service;

import com.example.estoque.domain.ItemPedido;
import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;

import java.util.ArrayList;
import java.util.List;

public class TestsUtils {
    public static Produto criaProduto(Long id, String nome, Integer gtd) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setQtd(gtd);
        return produto;

    }


    public static Pedido CriarPedidoComUmItem(long produtoId, Integer qtd) {
        Pedido pedido = new Pedido();
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(criarItemPedido(produtoId, qtd));
        pedido.setItens(itens);
        return pedido;
    }

    public static Pedido criarPedidoComMultiploItens(Long[] ids, Integer[] quantidades) {
        if (ids.length != quantidades.length) {
            throw new IllegalArgumentException("Arrays de IDs e quantidades devem ter o mesmo tamanho");

        }
        Pedido pedido = new Pedido();
        List<ItemPedido> itens = new ArrayList<>();

        for (int i = 0; i < ids.length; i++) {
            itens.add(criarItemPedido(ids[i], quantidades[i]));

        }
        pedido.setItens(itens);
        return pedido;

    }

    private static ItemPedido criarItemPedido(Long produtoId, Integer quantidade) {
        ItemPedido item = new ItemPedido();
        item.setId(produtoId);
        item.setQtd(quantidade);
        return item;
    }

    public static Pedido criarPedidoVazio() {
        Pedido pedido = new Pedido();
        pedido.setItens(new ArrayList<>());
        return pedido;
    }


}
