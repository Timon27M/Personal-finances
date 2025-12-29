package com.example.personalfinances.repository;

import com.example.personalfinances.entity.Category;
import com.example.personalfinances.entity.enums.TransactionType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  Optional<Category> findByCategoryId(UUID categoryId);

  Optional<Category> findByCategoryName(String categoryName);

  Optional<Category> findByWalletWalletIdAndCategoryNameAndCategoryType(
      UUID walletId, String categoryName, TransactionType type);

  boolean existsByCategoryId(UUID categoryId);

  boolean existsByWalletWalletIdAndCategoryNameAndCategoryType(
      UUID walletId, String categoryName, TransactionType categoryType);
}
