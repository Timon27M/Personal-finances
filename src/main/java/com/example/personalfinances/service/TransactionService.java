package com.example.personalfinances.service;

import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.Transaction;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final CategoryService categoryService;
  private final WalletService walletService;

  private final String categoryNameTransfer = "Перевод";

  public void addIncome(Wallet wallet, String categoryName, BigDecimal amount) {
    Category category = categoryService.findOrCreateIncomeCategory(wallet, categoryName);

    Transaction transaction = new Transaction(wallet, category, amount, TransactionType.INCOME);

    categoryService.addIncomeCategory(category, categoryName, amount);
    walletService.increaseBalance(wallet, amount);

    transactionRepository.save(transaction);
  }

  public void addIncomeTransfer(String categoryName, BigDecimal amount, String login) {
    Wallet wallet = walletService.getWalletByLogin(login);
    addIncome(wallet, categoryName, amount);
  }

  public void addIncomeCurrentWallet(String categoryName, BigDecimal amount) {
    Wallet wallet = walletService.getCurrentWallet();

    addIncome(wallet, categoryName, amount);
  }

  public void addExpense(String categoryName, BigDecimal amount, BigDecimal limitAmount) {
    Wallet wallet = walletService.getCurrentWallet();
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
    walletService.decreaseBalance(wallet, amount);

    transactionRepository.save(transaction);
  }

  public List<Transaction> getAllTransactions() {
    UUID walletId = walletService.getCurrentWallet().getWalletId();
    return transactionRepository.findByWalletWalletId(walletId);
  }

  public String addTransfer(String loginRecipientUser, BigDecimal amount) {
    if (!walletService.isExistWallet(loginRecipientUser)) {
      throw new IllegalStateException("Пользователь не найден");
    }
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException("Введена некоректная сумма");
    }
    addExpense(this.categoryNameTransfer, amount, null);
    addIncomeTransfer(this.categoryNameTransfer, amount, loginRecipientUser);

    return "Перевод выполнен успешно!";
  }
}
