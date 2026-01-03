package com.example.personalfinances.service;

import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.entity.budgetCategory.AbstractBudgetCategory;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryExpense;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryIncome;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public Category findOrCreateExpenseCategory(
      Wallet wallet, String categoryName, BigDecimal limitAmount, BigDecimal currentAmount) {
    return findOrCreateCategoryInternal(
        wallet, categoryName, TransactionType.EXPENSE, limitAmount, currentAmount);
  }

  public Category findOrCreateIncomeCategory(Wallet wallet, String categoryName) {
    return findOrCreateCategoryInternal(wallet, categoryName, TransactionType.INCOME, null, null);
  }

  public Category findOrCreateCategoryInternal(
      Wallet wallet,
      String categoryName,
      TransactionType type,
      BigDecimal limitAmount,
      BigDecimal currentAmount) {
    Category category;
    UUID walletId = wallet.getWalletId();

    if (categoryRepository.existsByWalletWalletIdAndCategoryNameAndCategoryType(
        walletId, categoryName, type)) {
      category = getCategory(walletId, categoryName, type);
    } else {
      if (type == TransactionType.EXPENSE
          && limitAmount != null
          && limitAmount.compareTo(currentAmount) < 0) {
        throw new IllegalStateException("limit не может быть меньше amount");
      }
      category = new Category(wallet, categoryName, type);
    }

    return categoryRepository.save(category);
  }

  public void addIncomeCategory(Category category, String categoryName, BigDecimal currentAmount) {
    AbstractBudgetCategory budget = category.getBudget();
    if (!(budget instanceof BudgetCategoryIncome)) {
      throw new IllegalStateException(
          "Категория "
              + categoryName
              + " имеет неправильный тип бюджета для транзакции поступления.");
    }
    BudgetCategoryIncome incomeBudget = (BudgetCategoryIncome) budget;

    incomeBudget.addIncome(currentAmount);
    categoryRepository.save(category);
  }

  public void addExpenseCategory(Category category, String categoryName, BigDecimal currentAmount) {
    AbstractBudgetCategory budget = category.getBudget();
    if (!(budget instanceof BudgetCategoryExpense)) {
      throw new IllegalStateException(
          "Категория "
              + categoryName
              + " имеет неправильный тип бюджета для транзакции поступления.");
    }
    BudgetCategoryExpense expenseBudget = (BudgetCategoryExpense) budget;

    if (expenseBudget.getLimitAmount() != null
        && expenseBudget.getLimitAmount().compareTo(currentAmount.add(expenseBudget.getExpense()))
            < 0) {
      throw new IllegalStateException("Лимит на данную категорию превышен!");
    }

    expenseBudget.addExpense(currentAmount);
    categoryRepository.save(category);
  }

  public List<Category> getCategoriesByWalletAndType(UUID walletId, TransactionType type) {
    return categoryRepository.findAllByWalletWalletIdAndCategoryType(walletId, type);
  }

  private Category getCategory(UUID walletId, String categoryName, TransactionType type) {
    return categoryRepository
        .findByWalletWalletIdAndCategoryNameAndCategoryType(walletId, categoryName, type)
        .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));
  }
}
