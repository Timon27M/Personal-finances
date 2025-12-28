package com.example.personalfinances.service;

import com.example.personalfinances.components.RequestGetterComponent;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.repository.UserRepository;
import com.example.personalfinances.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {

  private final WalletRepository walletRepository;
  private final UserRepository userRepository;
  private final RequestGetterComponent requestGetterComponent;

  @Transactional(readOnly = true)
  public Wallet getByUserId(UUID userId) {
    return walletRepository
        .findByUserUserId(userId)
        .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user13 " + userId));
  }

  public Wallet getWallet() {
    UUID userId = requestGetterComponent.getCurrentUserId();
    if (userRepository.existsById(userId)) {
      return getByUserId(userId);
    }

    throw new EntityNotFoundException("Not found for user " + userId);
  }

  public void increaseBalance(UUID userId, BigDecimal amount) {
    Wallet wallet = getByUserId(userId);

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("amount не может быть пустым");
    }

    wallet.increaseBalance(amount);
  }

  public void decreaseBalance(UUID userId, BigDecimal amount) {
    Wallet wallet = getByUserId(userId);
    if (wallet.getBalance().compareTo(amount) < 0
        || wallet.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Не достаточно средств");
    }

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("amount не может быть пустым или равным 0");
    }

    wallet.decreaseBalance(amount);
  }
}
