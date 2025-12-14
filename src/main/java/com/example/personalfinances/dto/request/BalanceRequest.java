package com.example.personalfinances.dto.request;

public class BalanceRequest {
    private String userName;

    public BalanceRequest() {}

    public BalanceRequest(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = userName;
    }
}
