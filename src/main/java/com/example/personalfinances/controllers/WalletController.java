package com.example.personalfinances.controllers;

import com.example.personalfinances.dto.wallet.responses.BalanceResponse;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;

  @GetMapping("/balance")
  public ResponseEntity<?> getBalance() {
    Wallet currentWallet = walletService.getWallet();

    return ResponseEntity.ok().body(new BalanceResponse(currentWallet.getBalance()));
  }
}
