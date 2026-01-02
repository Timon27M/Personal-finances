package com.example.personalfinances.service;

import com.example.personalfinances.components.RequestGetterComponent;
import com.example.personalfinances.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final RequestGetterComponent requestGetterComponent;
  private final UserRepository userRepository;
}
