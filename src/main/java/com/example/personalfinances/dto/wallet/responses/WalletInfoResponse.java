package com.example.personalfinances.dto.wallet.responses;

import com.example.personalfinances.entity.enums.TransactionType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Data
@Getter
@RequiredArgsConstructor
public class WalletInfoResponse {
  private final int status = 200;
  private final BigDecimal balance;
  private final BigDecimal walletExpense;
  private final BigDecimal walletIncome;
  private final List<CategoryInfoExpense> expenseCategories;
  private final List<CategoryInfoIncome> incomeCategories;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CategoryInfoExpense {
    private UUID categoryId;
    private String categoryName;
    private TransactionType categoryType;
    private BudgetExpense budget;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CategoryInfoIncome {
    private UUID categoryId;
    private String categoryName;
    private TransactionType categoryType;
    private BudgetIncome budget;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BudgetExpense {
    private UUID budgetId;
    private BigDecimal expense;
    private BigDecimal limitAmount;
    private BigDecimal remainingAmount;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BudgetIncome {
    private UUID budgetId;
    private BigDecimal income;
  }
}
