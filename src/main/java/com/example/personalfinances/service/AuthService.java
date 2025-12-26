package com.example.personalfinances.service;

import com.example.personalfinances.dto.auth.requests.LoginRequest;
import com.example.personalfinances.dto.auth.requests.RegisterRequest;
import com.example.personalfinances.entity.User;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public User register(RegisterRequest registerRequest) {
    if (userRepository.existsByLogin(registerRequest.getLogin())) {
      throw new IllegalArgumentException("Login already exists");
    }

    User user =
        new User(registerRequest.getLogin(), passwordEncoder.encode(registerRequest.getPassword()));

    Wallet wallet = new Wallet(user);

    user.setWallet(wallet);

    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public User login(LoginRequest loginRequest) {
    if (!userRepository.existsByLogin(loginRequest.getLogin())) {
      throw new IllegalArgumentException("Login does not exist");
    }

    User user =
        userRepository
            .findByLogin(loginRequest.getLogin())
            .orElseThrow(() -> new IllegalArgumentException("Invalid login"));
    ;

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid password");
    }

    return user;
  }
}
