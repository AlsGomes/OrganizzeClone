package com.example.organizzeclone.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Conta implements Serializable {

    private String id;
    private String nome;
    private String usuario;

    private Map<String, Movimentacao> movimentacoes = new HashMap<>();

    public Conta() {
    }

    public Conta(String id, String nome, String usuario) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getSaldo() {
        double sum = 0;
        Set<Map.Entry<String, Movimentacao>> set = movimentacoes.entrySet();
        for (Iterator<Map.Entry<String, Movimentacao>> it = set.iterator(); it.hasNext(); ) {
            sum += it.next().getValue().getValor();
        }

        return sum;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String id) {
        this.usuario = id;
    }

    public Map<String, Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    public Map<String, Movimentacao> getMovimentacoes(int month) {
        Map<String, Movimentacao> movimentacoesByMonth = new HashMap<>();
        Calendar c = Calendar.getInstance();

        Iterator<Map.Entry<String, Movimentacao>> iteradorDeEntries = movimentacoes.entrySet().iterator();
        for (Iterator<Map.Entry<String, Movimentacao>> it = iteradorDeEntries; it.hasNext(); ) {
            Map.Entry<String, Movimentacao> next = it.next();
            c.setTime(next.getValue().getData());
            if (c.get(Calendar.MONTH) == month) {
                movimentacoesByMonth.put(next.getKey(), next.getValue());
            }
        }

        return movimentacoesByMonth;
    }
}
