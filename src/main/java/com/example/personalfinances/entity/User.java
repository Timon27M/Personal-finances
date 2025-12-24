package com.example.personalfinances.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users")
public class User {

  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "UUID")
  private UUID userId;

  @Column(nullable = false, length = 100)
  private String login;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  private Wallet wallet;

  protected User() {}

  public User(String login, String passwordHash) {
    this.login = login;
    this.passwordHash = passwordHash;
    this.createdAt = LocalDateTime.now();
  }

  public UUID getId() {
    return userId;
  }

  public String getLogin() {
    return login;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Wallet getWallet() {
    return wallet;
  }

  public void setWallet(Wallet wallet) {
    this.wallet = wallet;
    wallet.setUser(this);
  }
}
