package com.example.bsystem.model;

import java.io.Serializable;

public class Service implements Serializable {

    private String id;
    private String name;
    private String type;
    private String price;
    private String observation;

    public Service() {
    }

    public Service(String id, String name, String type, String price, String observation) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.observation = observation;
    }

    public Service(String name, String price) {
        this.name = name;
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public String toString() {
        return " Service" +
                "\n" + " Descricao " + name +
                "\n" + " Tipo " + type +
                "\n" + " Valor " + price +
                "\n" + " Observacao " + observation;
    }
}
