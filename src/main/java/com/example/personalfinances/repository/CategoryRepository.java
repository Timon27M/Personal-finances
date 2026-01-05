package com.example.personalfinances.repository;

import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.enums.TransactionType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  Optional<Category> findByWalletWalletIdAndCategoryNameAndCategoryType(
      UUID walletId, String categoryName, TransactionType type);

  void deleteByWalletWalletIdAndCategoryNameAndCategoryType(
      UUID walletId, String categoryName, TransactionType type);

  List<Category> findAllByWalletWalletIdAndCategoryType(UUID walletId, TransactionType type);

  boolean existsByWalletWalletIdAndCategoryNameAndCategoryType(
      UUID walletId, String categoryName, TransactionType categoryType);
}
