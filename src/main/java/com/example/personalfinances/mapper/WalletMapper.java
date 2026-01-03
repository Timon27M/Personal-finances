package com.example.personalfinances.mapper;

import com.example.personalfinances.dto.wallet.responses.WalletInfoResponse;
import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryExpense;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryIncome;
import java.math.BigDecimal;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WalletMapper {

  @Mapping(target = "budget", expression = "java(mapBudgetExpense(category))")
  WalletInfoResponse.CategoryInfoExpense toCategoryInfoExpense(Category category);

  @Mapping(target = "budget", expression = "java(mapBudgetIncome(category))")
  WalletInfoResponse.CategoryInfoIncome toCategoryInfoIncome(Category category);

  default WalletInfoResponse.BudgetExpense mapBudgetExpense(Category category) {
    if (category.getBudget() instanceof BudgetCategoryExpense) {
      BudgetCategoryExpense budget = (BudgetCategoryExpense) category.getBudget();
      BigDecimal remainingAmount = null;
      if (budget.getLimitAmount() != null) {
        remainingAmount = budget.getLimitAmount().subtract(budget.getExpense());
      }
      return new WalletInfoResponse.BudgetExpense(
          budget.getBudgetId(), budget.getExpense(), budget.getLimitAmount(), remainingAmount);
    }
    return null;
  }

  default WalletInfoResponse.BudgetIncome mapBudgetIncome(Category category) {
    if (category.getBudget() instanceof BudgetCategoryIncome) {
      BudgetCategoryIncome budget = (BudgetCategoryIncome) category.getBudget();
      return new WalletInfoResponse.BudgetIncome(budget.getBudgetId(), budget.getIncome());
    }
    return null;
  }

  default WalletInfoResponse toWalletInfoResponse(
      BigDecimal balance,
      BigDecimal walletExpense,
      BigDecimal walletIncome,
      List<Category> expenseCategories,
      List<Category> incomeCategories) {

    List<WalletInfoResponse.CategoryInfoExpense> expenseDtos =
        expenseCategories != null
            ? expenseCategories.stream().map(this::toCategoryInfoExpense).toList()
            : List.of();

    List<WalletInfoResponse.CategoryInfoIncome> incomeDtos =
        incomeCategories != null
            ? incomeCategories.stream().map(this::toCategoryInfoIncome).toList()
            : List.of();

    return new WalletInfoResponse(balance, walletExpense, walletIncome, expenseDtos, incomeDtos);
  }
}
