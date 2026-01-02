package com.example.personalfinances.dto.transaction.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddExpenseRequest {
  private final String categoryName;
  private final BigDecimal amount;

  @JsonProperty(required = false)
  private BigDecimal limitAmount; // теперь опциональный
}
