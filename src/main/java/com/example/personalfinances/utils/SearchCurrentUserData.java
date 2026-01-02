package com.example.personalfinances.utils;

import com.example.personalfinances.components.RequestGetterComponent;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.repository.UserRepository;
import com.example.personalfinances.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchCurrentUserData {
  ;
  private final UserRepository userRepository;
  private final WalletRepository walletRepository;
  private final RequestGetterComponent requestGetterComponent;

  @Transactional(readOnly = true)
  public Wallet getByUserId(UUID userId) {
    return walletRepository
        .findByUserUserId(userId)
        .orElseThrow(
            () -> new EntityNotFoundException("Кошелек у пользователя " + userId + " не найден"));
  }

  public Wallet getWallet() {
    UUID userId = requestGetterComponent.getCurrentUserId();
    if (userRepository.existsById(userId)) {
      return getByUserId(userId);
    }

    throw new EntityNotFoundException("Не найден пользователь: " + userId);
  }
}
