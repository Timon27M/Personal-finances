package com.example.personalfinances.service;

import com.example.personalfinances.components.RequestGetterComponent;
import com.example.personalfinances.entity.User;
import com.example.personalfinances.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final RequestGetterComponent requestGetterComponent;
  private final UserRepository userRepository;

  public User getUserInfo() {
    UUID userId = requestGetterComponent.getCurrentUserId();
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    return user;
  }
}
