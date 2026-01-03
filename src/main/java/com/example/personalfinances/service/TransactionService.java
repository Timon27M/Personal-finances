package com.example.personalfinances.service;

import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.Transaction;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.repository.TransactionRepository;
import com.example.personalfinances.utils.SearchCurrentUserData;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final SearchCurrentUserData searchCurrentUserData;
  private final CategoryService categoryService;
  private final WalletService walletService;

  public void addIncome(String categoryName, BigDecimal amount) {
    Wallet wallet = searchCurrentUserData.getWallet();
    Category category = categoryService.findOrCreateIncomeCategory(wallet, categoryName);

    Transaction transaction = new Transaction(wallet, category, amount, TransactionType.INCOME);

    categoryService.addIncomeCategory(category, categoryName, amount);
    walletService.increaseBalance(amount);

    transactionRepository.save(transaction);
  }

  public void addExpense(String categoryName, BigDecimal amount, BigDecimal limitAmount) {
    Wallet wallet = searchCurrentUserData.getWallet();
    if (wallet.getBudget().getLimitAmount() != null
        && wallet
                .getBudget()
                .getLimitAmount()
                .compareTo(amount.add(wallet.getBudget().getExpense()))
            < 0) {
      throw new IllegalStateException("Лимит кошелька превышен!");
    }
    Category category =
        categoryService.findOrCreateExpenseCategory(wallet, categoryName, limitAmount, amount);

    Transaction transaction = new Transaction(wallet, category, amount, TransactionType.EXPENSE);

    categoryService.addExpenseCategory(category, categoryName, amount);
    walletService.decreaseBalance(amount);

    transactionRepository.save(transaction);
  }
}
