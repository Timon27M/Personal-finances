package com.example.personalfinances.dto.request;

public class BalanceRequest {
    private String name;

    public BalanceRequest() {}

    public BalanceRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
