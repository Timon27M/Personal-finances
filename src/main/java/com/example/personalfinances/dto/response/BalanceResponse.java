package com.example.personalfinances.dto.response;

import java.math.BigDecimal;

public class BalanceResponse {
    private String userName;
    private BigDecimal balance;
    private String message;
    private String status;

    public BalanceResponse() {}

    public BalanceResponse(String userName, BigDecimal balance, String message, String status) {
        this.userName = userName;
        this.balance = balance;
        this.message = message;
        this.status = status;
    }

    // Геттеры и сеттеры
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}