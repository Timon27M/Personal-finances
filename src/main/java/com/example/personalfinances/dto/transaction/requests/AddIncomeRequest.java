package com.example.personalfinances.dto.transaction.requests;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddIncomeRequest {
  private final String categoryName;
  private final BigDecimal amount;
}
