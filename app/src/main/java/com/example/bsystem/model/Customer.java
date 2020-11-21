package com.example.bsystem.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String birthDate;
    private String email;

    public Customer() {
    }

    public Customer(String id, String name, String phone, String birthDate, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.email = email;
    }

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return " Customer" +
                "\n" + " Nome " + name +
                "\n" + " Telefone " + phone +
                "\n" + " Data nascimento " + birthDate +
                "\n" + " Email " + email;
    }
}
