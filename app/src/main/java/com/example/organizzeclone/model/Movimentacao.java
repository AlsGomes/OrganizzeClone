package com.example.organizzeclone.model;

import com.example.organizzeclone.model.enums.Categoria;
import com.example.organizzeclone.model.enums.Tipo;

import java.io.Serializable;
import java.util.Date;

public class Movimentacao implements Serializable {

    private String id;
    private Date data;
    private Double valor;
    private String descricao;
    private Categoria categoria;
    private Tipo tipo;

    public Movimentacao() {
    }

    public Movimentacao(String id, Date data, Double valor, String descricao, Categoria categoria, Tipo tipo) {
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}
