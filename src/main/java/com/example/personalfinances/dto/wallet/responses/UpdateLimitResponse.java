package com.example.personalfinances.dto.wallet.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateLimitResponse {
  private final int status = 200;
  private final String message;
}
