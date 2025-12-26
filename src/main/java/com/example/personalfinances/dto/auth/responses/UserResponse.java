package com.example.personalfinances.dto.auth.responses;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserResponse {

  @Getter private final UUID userId;
  @Getter private final String login;
}
