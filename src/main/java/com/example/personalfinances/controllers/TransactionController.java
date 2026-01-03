package com.example.personalfinances.controllers;

import com.example.personalfinances.dto.DefaultSuccessResponse;
import com.example.personalfinances.dto.transaction.requests.AddExpenseRequest;
import com.example.personalfinances.dto.transaction.requests.AddIncomeRequest;
import com.example.personalfinances.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;

  @PostMapping("/add-income")
  public ResponseEntity<DefaultSuccessResponse> addIncome(@RequestBody AddIncomeRequest request) {
    transactionService.addIncome(request.getCategoryName(), request.getAmount());

    return ResponseEntity.ok().body(new DefaultSuccessResponse("Операция прошла успешно!"));
  }

  @PostMapping("/add-expense")
  public ResponseEntity<DefaultSuccessResponse> addExpense(@RequestBody AddExpenseRequest request) {
    transactionService.addExpense(
        request.getCategoryName(), request.getAmount(), request.getLimitAmount());

    return ResponseEntity.ok().body(new DefaultSuccessResponse("Операция прошла успешно!"));
  }
}
