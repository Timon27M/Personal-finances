package com.example.personalfinances.entity;

import com.example.personalfinances.entity.enums.TransactionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "transations")
public class Transaction {
  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "transaction_id", nullable = false, updatable = false)
  private UUID transactionId;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wallet_id", nullable = false)
  private Wallet wallet;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = true)
  private Category category;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false, length = 20)
  private TransactionType type;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  protected Transaction() {}

  public Transaction(Wallet wallet, Category category, BigDecimal amount, TransactionType type) {
    this.wallet = wallet;
    this.category = category;
    this.amount = amount;
    this.type = type;
    this.createdAt = LocalDateTime.now();
  }
}
