package com.example.organizzeclone.model.enums;

public enum Tipo {

    RECEITA(1),
    DESPESA(2);

    private int id;

    Tipo(int id) {
        this.id = id;
    }

    public static Tipo toEnum(int id) {
        for (Tipo x : Tipo.values()) {
            if (x.getId() == id) {
                return x;
            }
        }

        throw new IllegalArgumentException("Arguamento inválido");
    }

    public int getId() {
        return id;
    }
}
