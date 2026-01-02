package com.example.personalfinances.entity.budgetCategory;

import com.example.personalfinances.entity.Category;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "budget_category_expense")
public class BudgetCategoryExpense extends AbstractBudgetCategory {
  @Getter
  @Setter
  @Column(name = "expense", nullable = false, precision = 19, scale = 2)
  private BigDecimal expense;

  @Setter
  @Getter
  @Column(name = "limit_amount", nullable = true)
  private BigDecimal limitAmount;

  protected BudgetCategoryExpense() {}

  public BudgetCategoryExpense(Category category) {
    super(category);
    this.expense = BigDecimal.ZERO;
    this.limitAmount = null;
  }

  public BudgetCategoryExpense(Category category, BigDecimal limitAmount) {
    super(category);
    this.expense = BigDecimal.ZERO;
    this.limitAmount = limitAmount;
  }

  public BigDecimal addExpense(BigDecimal amount) {
    this.expense = this.expense.add(amount);
    return this.expense;
  }
}
