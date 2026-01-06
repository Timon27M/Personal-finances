package com.example.personalfinances.dto.auth.requests;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginRequest {
  private final String login;
  private final String password;

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }
}
