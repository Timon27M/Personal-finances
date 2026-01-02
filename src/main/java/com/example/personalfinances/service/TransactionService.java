package com.example.personalfinances.service;

import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.Transaction;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.entity.budgetCategory.AbstractBudgetCategory;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryExpense;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryIncome;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.repository.CategoryRepository;
import com.example.personalfinances.repository.TransactionRepository;
import com.example.personalfinances.repository.WalletRepository;
import com.example.personalfinances.utils.SearchCurrentUserData;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final WalletRepository walletRepository;
  private final CategoryRepository categoryRepository;
  private final SearchCurrentUserData searchCurrentUserData;
  private final WalletService walletService;

  public Transaction addIncome(String categoryName, BigDecimal amount) {
    Wallet wallet = searchCurrentUserData.getWallet();
    UUID walletId = wallet.getWalletId();
    Category category;

    if (categoryRepository.existsByWalletWalletIdAndCategoryNameAndCategoryType(
        walletId, categoryName, TransactionType.INCOME)) {
      category = getCategory(walletId, categoryName, TransactionType.INCOME);
    } else {
      category = new Category(wallet, categoryName, TransactionType.INCOME);
    }

    Transaction transaction = new Transaction(wallet, category, amount, TransactionType.INCOME);

    AbstractBudgetCategory budget = category.getBudget();
    if (!(budget instanceof BudgetCategoryIncome)) {
      throw new IllegalStateException(
          "Категория "
              + categoryName
              + " имеет неправильный тип бюджета для транзакции поступления.");
    }
    BudgetCategoryIncome incomeBudget = (BudgetCategoryIncome) budget;

    incomeBudget.addIncome(amount);

    wallet.increaseBalance(amount);
    wallet.getBudget().addIncome(amount);
    walletRepository.save(wallet);
    categoryRepository.save(category);

    return transactionRepository.save(transaction);
  }

  public Transaction addExpense(String categoryName, BigDecimal amount, BigDecimal limitAmount) {
    Wallet wallet = searchCurrentUserData.getWallet();
    if (wallet.getBudget().getLimitAmount() != null
        && wallet
                .getBudget()
                .getLimitAmount()
                .compareTo(amount.add(wallet.getBudget().getExpense()))
            < 0) {
      throw new IllegalStateException("Лимит кошелька превышен!");
    }
    UUID walletId = wallet.getWalletId();
    Category category;

    if (categoryRepository.existsByWalletWalletIdAndCategoryNameAndCategoryType(
        walletId, categoryName, TransactionType.EXPENSE)) {
      category = getCategory(walletId, categoryName, TransactionType.EXPENSE);
    } else {
      if (limitAmount != null && limitAmount.compareTo(amount) < 0) {
        throw new IllegalStateException("limit не может быть меньше amount");
      }
      category = new Category(wallet, categoryName, TransactionType.EXPENSE, limitAmount);
    }

    Transaction transaction = new Transaction(wallet, category, amount, TransactionType.EXPENSE);

    AbstractBudgetCategory budget = category.getBudget();
    if (!(budget instanceof BudgetCategoryExpense)) {
      throw new IllegalStateException(
          "Категория "
              + categoryName
              + " имеет неправильный тип бюджета для транзакции поступления.");
    }
    BudgetCategoryExpense expenseBudget = (BudgetCategoryExpense) budget;

    if (expenseBudget.getLimitAmount() != null
        && expenseBudget.getLimitAmount().compareTo(amount.add(expenseBudget.getExpense())) < 0) {
      throw new IllegalStateException("Лимит на данную категорию превышен!");
    }

    expenseBudget.addExpense(amount);

    walletService.decreaseBalance(amount);
    categoryRepository.save(category);

    return transactionRepository.save(transaction);
  }

  private Category getCategory(UUID walletId, String categoryName, TransactionType type) {
    return categoryRepository
        .findByWalletWalletIdAndCategoryNameAndCategoryType(walletId, categoryName, type)
        .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));
  }
}
