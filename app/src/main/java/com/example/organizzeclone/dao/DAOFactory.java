package com.example.organizzeclone.dao;

import com.example.organizzeclone.model.Movimentacao;

public class DAOFactory {

    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOImpl();
    }

    public static ContaDAO getContaDAO() {
        return new ContaDAOImpl();
    }

}
