package com.example.personalfinances.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "user_finances")
public class UserFinance {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "user_name", unique = true, nullable = false)
    @NotBlank(message = "Имя пользователя обзаятельно")
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
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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