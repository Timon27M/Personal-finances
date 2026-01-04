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

  public void increaseBalance(Wallet wallet, BigDecimal currentAmount) {
    if (currentAmount == null || currentAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("amount не может быть пустым");
    }

    wallet.increaseBalance(currentAmount);
    wallet.getBudget().addIncome(currentAmount);
    walletRepository.save(wallet);
  }

  public void decreaseBalance(Wallet wallet, BigDecimal amount) {
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
    Wallet wallet = getCurrentWallet();
    return wallet.getBudget().getExpense();
  }

  public BigDecimal getIncomeWallet() {
    Wallet wallet = getCurrentWallet();
    return wallet.getBudget().getIncome();
  }

  public BigDecimal getBalance() {
    return getCurrentWallet().getBalance();
  }

  public List<Category> getCategoriesByType(TransactionType type) {
    Wallet wallet = getCurrentWallet();
    UUID walletId = wallet.getWalletId();

    return categoryService.getCategoriesByWalletAndType(walletId, type);
  }

  public String addLimitAmount(BigDecimal limit) {
    getCurrentWallet().getBudget().setLimitAmount(limit);
    return "Лимит кошелька обновлен!";
  }

  public String updateCategoryLimitAmount(String categoryName, BigDecimal newLimit) {
    UUID walletId = getCurrentWallet().getWalletId();

    categoryService.updateLimitAmount(walletId, categoryName, newLimit);
    return "Лимит " + categoryName + " обновлен!";
  }

  public Wallet getWalletByLogin(String login) {
    if (!walletRepository.existsByUserLogin(login)) {
      throw new IllegalStateException("Пользователь не найден");
    }
    return walletRepository.findByUserLogin(login);
  }

  public Wallet getCurrentWallet() {
    return searchCurrentUserData.getWallet();
  }

  public boolean isExistWallet(String login) {
    return walletRepository.existsByUserLogin(login);
  }
}
