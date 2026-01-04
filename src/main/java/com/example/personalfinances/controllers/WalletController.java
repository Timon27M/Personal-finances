package com.example.personalfinances.controllers;

import com.example.personalfinances.dto.DefaultSuccessResponse;
import com.example.personalfinances.dto.wallet.requests.UpdateCategoryLimitAmount;
import com.example.personalfinances.dto.wallet.requests.UpdateLimitRequest;
import com.example.personalfinances.dto.wallet.responses.WalletInfoResponse;
import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.enums.TransactionType;
import com.example.personalfinances.mapper.WalletMapper;
import com.example.personalfinances.service.WalletService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;
  private final WalletMapper walletMapper;

  @GetMapping("/info")
  public ResponseEntity<WalletInfoResponse> getBalance() {
    BigDecimal currentWalletBalance = walletService.getBalance();
    BigDecimal currentWalletIncome = walletService.getIncomeWallet();
    BigDecimal currentWalletExpense = walletService.getExpenseWallet();

    List<Category> categoriesExpense = walletService.getCategoriesByType(TransactionType.EXPENSE);
    List<Category> categoriesIncome = walletService.getCategoriesByType(TransactionType.INCOME);

    WalletInfoResponse response =
        walletMapper.toWalletInfoResponse(
            currentWalletBalance,
            currentWalletExpense,
            currentWalletIncome,
            categoriesExpense,
            categoriesIncome);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/limit-amount/wallet")
  public ResponseEntity<DefaultSuccessResponse> limitAmount(
      @RequestBody UpdateLimitRequest request) {
    String message = walletService.addLimitAmount(request.getLimitAmount());

    return ResponseEntity.ok().body(new DefaultSuccessResponse(message));
  }

  @PostMapping("/limit-amount/category")
  public ResponseEntity<DefaultSuccessResponse> limitAmountCategory(
      @RequestBody UpdateCategoryLimitAmount request) {
    String message =
        walletService.updateCategoryLimitAmount(
            request.getCategoryName(), request.getNewLimitAmount());

    return ResponseEntity.ok().body(new DefaultSuccessResponse(message));
  }
}
