package com.example.organizzeclone.model.enums;

public enum Categoria {

    CASA(1, "Casa"),
    ALIMENTACAO(2, "Alimentação"),
    RECEITA(3, "Receita");

    private int id;
    private String nome;

    Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static Categoria toEnum(int id) {
        for (Categoria x : Categoria.values()) {
            if (x.getId() == id) {
                return x;
            }
        }

        throw new IllegalArgumentException("Arguamento inválido");
    }

    public static Categoria toEnum(String nome) {
        for (Categoria x : Categoria.values()) {
            if (x.getNome().equals(nome)) {
                return x;
            }
        }

        throw new IllegalArgumentException("Arguamento inválido");
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
