package com.example.personalfinances.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "budgets")
public class Budget {

  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "budget_id", nullable = false, updatable = false)
  private UUID budgetId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "Category_id",
      nullable = false,
      unique = true,
      foreignKey = @ForeignKey(name = "fk_budget_category"))
  private Category category;

  @Column(name = "limit_amount", nullable = false)
  private BigDecimal limitAmount;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public void setCategory(Category category) {
    this.category = category;
  }
}
