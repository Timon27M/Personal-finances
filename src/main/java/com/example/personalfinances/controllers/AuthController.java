package com.example.personalfinances.controllers;

import com.example.personalfinances.dto.auth.requests.LoginRequest;
import com.example.personalfinances.dto.auth.requests.RegisterRequest;
import com.example.personalfinances.dto.auth.responses.UserResponse;
import com.example.personalfinances.entity.User;
import com.example.personalfinances.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

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

      User user = authService.login(request);
      return ResponseEntity.ok(new UserResponse(user.getUserId(), user.getLogin()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", "USER_ALREADY_EXISTS", "message", e.getMessage()));
    }
  }
}
