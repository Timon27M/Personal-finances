package com.example.personalfinances.service;

import com.example.personalfinances.utils.SearchCurrentUserData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
  private final SearchCurrentUserData searchCurrentUserData;
}
