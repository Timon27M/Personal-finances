package com.example.personalfinances.service;

import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.repository.WalletRepository;
import com.example.personalfinances.utils.SearchCurrentUserData;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {
  private final SearchCurrentUserData searchCurrentUserData;
  private final WalletRepository walletRepository;
  private final CategoryService categoryService;

  public void increaseBalance(BigDecimal currentAmount) {
    Wallet wallet = searchCurrentUserData.getWallet();

    if (currentAmount == null || currentAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("amount не может быть пустым");
    }

    wallet.increaseBalance(currentAmount);
    wallet.getBudget().addIncome(currentAmount);
    walletRepository.save(wallet);
  }

  public void decreaseBalance(BigDecimal amount) {
    Wallet wallet = searchCurrentUserData.getWallet();
    if (wallet.getBalance().compareTo(amount) < 0
        || wallet.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException("Не достаточно средств");
    }

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException("amount не может быть пустым или равным 0");
    }

    wallet.decreaseBalance(amount);
    wallet.getBudget().addExpense(amount);
    walletRepository.save(wallet);
  }

  public BigDecimal getExpenseWallet() {
    Wallet wallet = searchCurrentUserData.getWallet();
    return wallet.getBudget().getExpense();
  }

  public BigDecimal getIncomeWallet() {
    Wallet wallet = searchCurrentUserData.getWallet();
    return wallet.getBudget().getIncome();
  }

  public BigDecimal getBalance() {
    return searchCurrentUserData.getWallet().getBalance();
  }

  public List<Category> getCategoriesByType(TransactionType type) {
    Wallet wallet = searchCurrentUserData.getWallet();
    UUID walletId = wallet.getWalletId();

    return categoryService.getCategoriesByWalletAndType(walletId, type);
  }

  public String addLimitAmount(BigDecimal limit) {
    searchCurrentUserData.getWallet().getBudget().setLimitAmount(limit);
    return "Лимит кошелька обновлен!";
  }
}
