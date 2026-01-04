package com.example.personalfinances.dto.transaction.responses;

import com.example.personalfinances.entity.enums.TransactionType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Data
@Getter
@RequiredArgsConstructor
public class GetAllTransactionResponse {
  private final List<TransactionData> transactions;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TransactionData {
    private UUID transactionId;
    private BigDecimal amount;
    private String categoryName;
    private TransactionType type;
  }
}
