package com.example.personalfinances.dto.wallet.requests;

import com.example.personalfinances.entity.enums.TransactionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteCategoryRequest {
  private final String categoryName;
  private final TransactionType type;
}
