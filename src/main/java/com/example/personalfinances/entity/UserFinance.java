package com.example.personalfinances.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "user_finances")
public class UserFinance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false)
    @NotBlank(message = "Имя пользователя обязательно")
    private String userName;

    @Column(name = "balance", nullable = false)
    @NotNull(message = "Баланс обязателен")
    private BigDecimal balance;

    // Конструкторы
    public UserFinance() {}

    public UserFinance(String userName, BigDecimal balance) {
        this.userName = userName;
        this.balance = balance;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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