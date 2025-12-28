package com.example.personalfinances.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "budget_wallet")
public class BudgetWallet {

  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "budget_id", nullable = false, updatable = false)
  private UUID budgetId;

  @Setter
  @Getter
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "wallet_id",
      nullable = false,
      unique = true,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_budget_wallet"))
  private Wallet wallet;

  @Getter
  @Setter
  @Column(name = "income", nullable = false, precision = 19, scale = 2)
  private BigDecimal income;

  @Getter
  @Setter
  @Column(name = "expense", nullable = false, precision = 19, scale = 2)
  private BigDecimal expense;

  @Column(name = "limit_amount", nullable = true)
  private BigDecimal limitAmount;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  protected BudgetWallet() {}

  public BudgetWallet(Wallet wallet) {
    this.wallet = wallet;
    this.income = BigDecimal.ZERO;
    this.expense = BigDecimal.ZERO;
    this.createdAt = LocalDateTime.now();
  }
}
