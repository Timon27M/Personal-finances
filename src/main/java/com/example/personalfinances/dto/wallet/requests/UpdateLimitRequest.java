package com.example.personalfinances.dto.wallet.requests;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateLimitRequest {
  private final BigDecimal limitAmount;
}
