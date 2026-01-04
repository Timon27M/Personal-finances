package com.example.personalfinances.dto.transaction.requests;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TransferAmountRequest {
  private final String loginRecipient;
  private final BigDecimal amount;
}
