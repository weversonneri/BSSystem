package com.example.bsystem.model;

import java.io.Serializable;

public class Servico implements Serializable {

    private Integer codigo;
    private String descricao;
    private String tipo;
    private Double valor;
    private String observacao;

    public Servico() {
    }

    public Servico(Integer codigo, String descricao, String tipo, Double valor, String observacao) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
        this.observacao = observacao;
    }

    public Servico(String descricao, Double valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return " Servico" +
                "\n" + " Codigo " + codigo +
                "\n" + " Descricao " + descricao +
                "\n" + " Tipo " + tipo +
                "\n" + " Valor " + valor +
                "\n" + " Observacao " + observacao;
    }
}
