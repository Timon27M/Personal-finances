package com.example.personalfinances.controllers;

import com.example.personalfinances.dto.DefaultSuccessResponse;
import com.example.personalfinances.dto.transaction.requests.AddExpenseRequest;
import com.example.personalfinances.dto.transaction.requests.AddIncomeRequest;
// import com.example.personalfinances.dto.transaction.requests.TransferAmountRequest;
import com.example.personalfinances.dto.transaction.requests.TransferAmountRequest;
import com.example.personalfinances.dto.transaction.responses.GetAllTransactionResponse;
import com.example.personalfinances.entity.Transaction;
import com.example.personalfinances.mapper.TransactionMapper;
import com.example.personalfinances.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;
  private final TransactionMapper transactionMapper;

  @PostMapping("/add-income")
  public ResponseEntity<DefaultSuccessResponse> addIncome(@RequestBody AddIncomeRequest request) {
    transactionService.addIncomeCurrentWallet(request.getCategoryName(), request.getAmount());

    return ResponseEntity.ok().body(new DefaultSuccessResponse("Операция прошла успешно!"));
  }

  @PostMapping("/add-expense")
  public ResponseEntity<DefaultSuccessResponse> addExpense(@RequestBody AddExpenseRequest request) {
    transactionService.addExpense(
        request.getCategoryName(), request.getAmount(), request.getLimitAmount());

    return ResponseEntity.ok().body(new DefaultSuccessResponse("Операция прошла успешно!"));
  }

  @GetMapping("/history")
  public ResponseEntity<GetAllTransactionResponse> getTransaction() {
    List<Transaction> transactions = transactionService.getAllTransactions();

    GetAllTransactionResponse response =
        transactionMapper.toGetAllTransactionResponse(transactions);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/transfer")
  public ResponseEntity<DefaultSuccessResponse> transferAmount(
      @RequestBody TransferAmountRequest request) {
    String message =
        transactionService.addTransfer(request.getLoginRecipient(), request.getAmount());

    return ResponseEntity.ok(new DefaultSuccessResponse(message));
  }
}
