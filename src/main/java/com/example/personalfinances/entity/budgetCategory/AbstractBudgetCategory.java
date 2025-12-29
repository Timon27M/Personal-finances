package com.example.personalfinances.entity.budgetCategory;

import com.example.personalfinances.entity.Category;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractBudgetCategory {

  @Getter
  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "budget_id", nullable = false, updatable = false)
  private UUID budgetId;

  @Getter
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "category_id",
      nullable = false,
      unique = true,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_budget_category"))
  private Category category;

  @Getter
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  protected AbstractBudgetCategory() {}

  public AbstractBudgetCategory(Category category) {
    this.category = category;
    this.createdAt = LocalDateTime.now();
  }
}
