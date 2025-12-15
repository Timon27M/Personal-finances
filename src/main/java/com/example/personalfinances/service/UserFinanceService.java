package com.example.personalfinances.service;

import com.example.personalfinances.dto.response.BalanceResponse;
import com.example.personalfinances.entity.UserFinance;
import com.example.personalfinances.repository.UserFinanceRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserFinanceService {

  private final UserFinanceRepository userFinanceRepository;

  public UserFinanceService(UserFinanceRepository userFinanceRepository) {
    this.userFinanceRepository = userFinanceRepository;
  }

  @Transactional
  public BalanceResponse getOrCreateUserBalance(String userName) {
    Optional<UserFinance> existingUser = userFinanceRepository.findByUserName(userName);

    if (existingUser.isPresent()) {
      UserFinance user = existingUser.get();
      return new BalanceResponse(
          user.getUserId(),
          user.getUserName(),
          user.getBalance(),
          "Баланс пользователя найден",
          "success");
    } else {
      // Создаем нового пользователя с начальным балансом 0
      UserFinance newUser = new UserFinance(userName, BigDecimal.ZERO);
      userFinanceRepository.save(newUser);

      return new BalanceResponse(
          newUser.getUserId(),
          userName,
          BigDecimal.ZERO,
          "Пользователь создан с начальным балансом 0",
          "created");
    }
  }

  @Transactional
  public BalanceResponse updateBalance(String userName, BigDecimal newBalance) {
    UserFinance user =
        userFinanceRepository
            .findByUserName(userName)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

    user.setBalance(newBalance);
    userFinanceRepository.save(user);

    return new BalanceResponse(
        user.getUserId(), userName, newBalance, "Баланс обновлен", "updated");
  }
}
