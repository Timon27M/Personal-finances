package com.example.personalfinances.controllers;

import com.example.personalfinances.dto.auth.requests.LoginRequest;
import com.example.personalfinances.dto.auth.requests.RegisterRequest;
import com.example.personalfinances.dto.auth.responses.LoginResponse;
import com.example.personalfinances.dto.auth.responses.UserResponse;
import com.example.personalfinances.entity.User;
import com.example.personalfinances.repository.UserRepository;
import com.example.personalfinances.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserRepository userRepository;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
      User user = authService.register(request);
      return ResponseEntity.ok().body(new UserResponse(user.getUserId(), user.getLogin()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", "USER_ALREADY_EXISTS", "message", e.getMessage()));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
      String token = authService.login(request);
      return ResponseEntity.ok(new LoginResponse(token));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", "USER_ALREADY_EXISTS", "message", e.getMessage()));
    }
  }

  @PostMapping("/user")
  public ResponseEntity<?> getUser(@RequestBody LoginRequest request) {
    User user =
        userRepository
            .findByLogin(request.getLogin())
            .orElseThrow(() -> new IllegalArgumentException("Invalid login"));

    return ResponseEntity.ok().body(new UserResponse(user.getUserId(), user.getLogin()));
  }
}
