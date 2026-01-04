package com.example.personalfinances.mapper;

import com.example.personalfinances.dto.transaction.responses.GetAllTransactionResponse;
import com.example.personalfinances.entity.Transaction;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
  default GetAllTransactionResponse.TransactionData mapTransaction(Transaction transaction) {
    return new GetAllTransactionResponse.TransactionData(
        transaction.getTransactionId(),
        transaction.getAmount(),
        transaction.getCategory() != null ? transaction.getCategory().getCategoryName() : null,
        transaction.getType());
  }

  default GetAllTransactionResponse toGetAllTransactionResponse(List<Transaction> transactions) {
    List<GetAllTransactionResponse.TransactionData> transactionDtos =
        transactions != null ? transactions.stream().map(this::mapTransaction).toList() : List.of();

    return new GetAllTransactionResponse(transactionDtos);
  }
}
