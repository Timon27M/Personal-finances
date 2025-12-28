package com.example.personalfinances.dto.wallet.responses;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BalanceResponse {
  private final BigDecimal balance;
}
