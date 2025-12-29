package com.example.personalfinances.entity.budgetCategory;

import com.example.personalfinances.entity.Category;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface BudgetCategory {
  UUID getBudgetId();

  Category getCategory();

  BigDecimal getLimitAmount();

  LocalDateTime getCreatedAt();

  void setLimitAmount(BigDecimal limitAmount);
}
