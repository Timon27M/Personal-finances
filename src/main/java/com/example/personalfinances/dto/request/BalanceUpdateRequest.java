package com.example.personalfinances.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BalanceUpdateRequest {

    @NotBlank(message = "Имя пользователя обязательно")
    private String userName;

    @NotNull(message = "Баланс обязателен")
    private BigDecimal balance;

    // Конструктор по умолчанию
    public BalanceUpdateRequest() {}

    // Конструктор с параметрами
    public BalanceUpdateRequest(String userName, BigDecimal balance) {
        this.userName = userName;
        this.balance = balance;
    }

    // Геттеры и сеттеры
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
