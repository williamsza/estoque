package com.example.estoque.domain;

public class ItemPedido {

    private Long id;
    private Integer qtd;

    public ItemPedido(Long id, Integer qtd) {
        this.id = id;
        this.qtd = qtd;
    }

    public ItemPedido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }
}
