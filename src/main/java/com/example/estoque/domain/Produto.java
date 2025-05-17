package com.example.estoque.domain;

import com.example.estoque.entity.ProdutoEntity;

public class Produto {

    private String id;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer qtd;

    public Produto(long l, String string, int i) {
    }

    public Produto(String id,String nome, String descricao, Double preco, Integer qtd) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qtd = qtd;
    }

    public Produto(ProdutoEntity entity) {
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
        this.preco = entity.getPreco();
        this.qtd = entity.getQtd();
    }

    public Produto() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
