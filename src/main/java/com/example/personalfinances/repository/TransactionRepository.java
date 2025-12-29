package com.example.personalfinances.repository;

import com.example.personalfinances.entity.Transaction;
import com.example.personalfinances.entity.enums.TransactionType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
  List<Transaction> findByWalletWalletId(UUID walletId);

  List<Transaction> findByWalletWalletIdAndType(UUID walletId, TransactionType type);

  @Query(
      """
        select coalesce(sum(t.amount), 0)
        from Transaction t
        where t.wallet.walletId = :walletId
        and t.type = :type
    """)
  BigDecimal sumByWalletAndType(
      @Param("walletId") UUID walletId, @Param("type") TransactionType type);
}
