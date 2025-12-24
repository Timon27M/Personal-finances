package com.example.personalfinances.entity;

import com.example.personalfinances.entity.enums.CategoryType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wallet_id", nullable = false)
  private Wallet wallet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = true)
  private Category category;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "description", length = 255)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false, length = 20)
  private CategoryType type;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public void setCategory(Category category) {
    this.category = category;
  }

  public void setWallet(Wallet wallet) {
    this.wallet = wallet;
  }
}
