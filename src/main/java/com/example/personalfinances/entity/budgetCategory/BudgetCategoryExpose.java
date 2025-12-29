package com.example.personalfinances.entity.budgetCategory;

import com.example.personalfinances.entity.Category;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "budget_category_expose")
public class BudgetCategoryExpose extends AbstractBudgetCategory {
  @Getter
  @Setter
  @Column(name = "expose", nullable = false, precision = 19, scale = 2)
  private BigDecimal expose;

  @Setter
  @Getter
  @Column(name = "limit_amount", nullable = true)
  private BigDecimal limitAmount;

  protected BudgetCategoryExpose() {}

  public BudgetCategoryExpose(Category category) {
    super(category);
    this.expose = BigDecimal.ZERO;
    this.limitAmount = null;
  }

  public BudgetCategoryExpose(Category category, BigDecimal limitAmount) {
    super(category);
    this.expose = BigDecimal.ZERO;
    this.limitAmount = limitAmount;
  }
}
