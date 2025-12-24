package com.example.personalfinances.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "wallets")
public class Wallet {

  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "wallet_id", updatable = false, nullable = false)
  private UUID walletId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      unique = true,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_wallet_user"))
  private User user;

  @Column(name = "balance", nullable = false, precision = 19, scale = 2)
  private BigDecimal balance;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime created_at;

  @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Category> categories = new ArrayList<>();

  @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Transaction> transactions = new ArrayList<>();

  protected Wallet() {}

  public Wallet(User user) {
    this.user = user;
    this.balance = BigDecimal.ZERO;
    this.created_at = LocalDateTime.now();
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void increaseBalance(BigDecimal amount) {
    this.balance = this.balance.add(amount);
  }

  public void decreaseBalance(BigDecimal amount) {
    this.balance = this.balance.subtract(amount);
  }

  public UUID getWalletId() {
    return walletId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
    transaction.setWallet(this);
  }

  public void addCategory(Category category) {
    categories.add(category);
    category.setWallet(this);
  }
}
