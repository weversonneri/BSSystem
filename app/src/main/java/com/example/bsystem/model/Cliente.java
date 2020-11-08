package com.example.bsystem.model;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nome;
    private String telefone;
    private String nascimento;
    private String email;

    public Cliente() {
    }

    public Cliente(String nome, String telefone, String nascimento, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
        this.email = email;
    }

    public Cliente(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return " Cliente" +
                "\n" + " Nome " + nome +
                "\n" + " Telefone " + telefone +
                "\n" + " Data nascimento " + nascimento +
                "\n" + " Email " + email;
    }
}
