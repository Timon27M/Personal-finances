package com.example.personalfinances.entity.budgetCategory;

import com.example.personalfinances.entity.Category;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "budget_category_income")
public class BudgetCategoryIncome extends AbstractBudgetCategory {

  @Getter
  @Setter
  @Column(name = "income", nullable = false, precision = 19, scale = 2)
  private BigDecimal income;

  protected BudgetCategoryIncome() {}

  public BudgetCategoryIncome(Category category) {
    super(category);
    this.income = BigDecimal.ZERO;
  }

  public BigDecimal addIncome(BigDecimal amount) {
    this.income = this.income.add(amount);
    return this.income;
  }
}
