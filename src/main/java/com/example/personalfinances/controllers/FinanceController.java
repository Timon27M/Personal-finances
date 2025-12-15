package com.example.personalfinances.controllers;

import com.example.personalfinances.dto.request.BalanceRequest;
import com.example.personalfinances.dto.request.BalanceUpdateRequest;
import com.example.personalfinances.dto.response.BalanceResponse;
import com.example.personalfinances.service.UserFinanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

  private final UserFinanceService userFinanceService;

  public FinanceController(UserFinanceService userFinanceService) {
    this.userFinanceService = userFinanceService;
  }

  @PostMapping("/balance")
  public ResponseEntity<BalanceResponse> getBalance(@Valid @RequestBody BalanceRequest request) {
    if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new BalanceResponse(null, null, null, "Имя пользователя обязательно", "error"));
    }

    BalanceResponse response = userFinanceService.getOrCreateUserBalance(request.getUserName());
    return ResponseEntity.ok(response);
  }

  // Дополнительный эндпоинт для обновления баланса
  @PostMapping("/update-balance")
  public ResponseEntity<BalanceResponse> updateBalance(
      @Valid @RequestBody BalanceUpdateRequest request) {
    BalanceResponse response =
        userFinanceService.updateBalance(request.getUserName(), request.getBalance());
    return ResponseEntity.ok(response);
  }
}
