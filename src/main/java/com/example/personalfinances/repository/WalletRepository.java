package com.example.personalfinances.repository;

import com.example.personalfinances.entity.Wallet;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

  Optional<Wallet> findByUserUserId(UUID userId);

  Wallet findByUserLogin(String userLogin);

  Optional<Wallet> findByWalletId(UUID walletId);

  boolean existsByUserUserId(UUID userId);

  boolean existsByUserLogin(String userLogin);
}
