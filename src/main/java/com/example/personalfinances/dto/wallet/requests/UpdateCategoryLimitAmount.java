package com.example.personalfinances.dto.wallet.requests;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateCategoryLimitAmount {
  private final String categoryName;
  private final BigDecimal newLimitAmount;
}
