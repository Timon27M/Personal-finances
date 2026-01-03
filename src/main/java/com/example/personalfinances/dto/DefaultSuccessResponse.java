package com.example.personalfinances.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultSuccessResponse {
  private int status;
  private String message;
  private LocalDateTime timestamp;

  public DefaultSuccessResponse(String message) {
    this.status = 200;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }
}
