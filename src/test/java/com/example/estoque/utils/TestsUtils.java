package com.example.estoque.utils;

import com.example.estoque.domain.Produto;

public class TestsUtils {
    public static Produto criaProduto(Long id, String nome, Integer qtd) {
        // Use the constructor that takes all parameters if available
        return new Produto(id, nome, qtd);
        
        // Alternatively, if you need to use setters:
        /*
        Produto produto = new Produto();
        // If Produto has an id setter
        produto.setId(id);
        produto.setNome(nome);
        produto.setQtd(qtd);
        return produto;
        */
    }
}