package com.example.personalfinances.controllers;

import com.example.personalfinances.dto.wallet.requests.UpdateLimitRequest;
import com.example.personalfinances.dto.wallet.responses.BalanceResponse;
import com.example.personalfinances.dto.wallet.responses.UpdateLimitResponse;
import com.example.personalfinances.service.WalletService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;

  @GetMapping("/balance")
  public ResponseEntity<BalanceResponse> getBalance() {
    BigDecimal currentWalletBalance = walletService.getBalance();

    return ResponseEntity.ok().body(new BalanceResponse(currentWalletBalance));
  }

  @PostMapping("/limit-amount")
  public ResponseEntity<UpdateLimitResponse> limitAmount(@RequestBody UpdateLimitRequest request) {
    String message = walletService.addLimitAmount(request.getLimitAmount());

    return ResponseEntity.ok().body(new UpdateLimitResponse(message));
  }
}
