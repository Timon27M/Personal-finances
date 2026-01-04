package com.example.personalfinances.controllers;

import com.example.personalfinances.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategotyController {
  private final CategoryService categoryService;
}
