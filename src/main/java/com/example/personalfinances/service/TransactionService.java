package com.example.personalfinances.service;

import com.example.personalfinances.components.RequestGetterComponent;
import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.Transaction;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.entity.budgetCategory.AbstractBudgetCategory;
import com.example.personalfinances.entity.budgetCategory.BudgetCategoryIncome;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.repository.CategoryRepository;
import com.example.personalfinances.repository.TransactionRepository;
import com.example.personalfinances.repository.UserRepository;
import com.example.personalfinances.repository.WalletRepository;
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
  private final UserRepository userRepository;
  private final RequestGetterComponent requestGetterComponent;

  public Transaction addIncome(String categoryName, BigDecimal amount) {
    Wallet wallet = getWallet();
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
          "Category " + categoryName + " has wrong budget type for income transaction");
    }
    BudgetCategoryIncome incomeBudget = (BudgetCategoryIncome) budget;

    incomeBudget.addIncome(amount);

    wallet.increaseBalance(amount);
    wallet.getBudget().addIncome(amount);
    walletRepository.save(wallet);
    categoryRepository.save(category);

    return transactionRepository.save(transaction);
  }

  @Transactional(readOnly = true)
  public Wallet getByUserId(UUID userId) {
    return walletRepository
        .findByUserUserId(userId)
        .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user13 " + userId));
  }

  public Wallet getWallet() {
    UUID userId = requestGetterComponent.getCurrentUserId();
    if (userRepository.existsById(userId)) {
      return getByUserId(userId);
    }

    throw new EntityNotFoundException("Not found for user " + userId);
  }

  private Category getCategory(UUID walletId, String categoryName, TransactionType type) {
    return categoryRepository
        .findByWalletWalletIdAndCategoryNameAndCategoryType(walletId, categoryName, type)
        .orElseThrow(() -> new RuntimeException("Category not found"));
  }
}
