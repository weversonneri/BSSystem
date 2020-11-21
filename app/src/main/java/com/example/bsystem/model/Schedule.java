package com.example.bsystem.model;

import java.io.Serializable;

public class Schedule implements Serializable {

    private String id;
    private String name;
    private String phone;
    private String time;
    private String service;

    public Schedule() {
    }

    public Schedule( String id, String name, String phone, String time, String service) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.time = time;
        this.service = service;
    }

    public Schedule(String name, String time) {
        this.name = name;
        this.time = time;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "name='" + name + '\'' +
                ", phone=" + phone +
                ", time='" + time + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
