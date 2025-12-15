package com.example.personalfinances.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

  @GetMapping("/greeting")
  public String greeting(@RequestParam(required = false, defaultValue = "World") String name) {
    return "Hello, " + name + "!";
  }

  @PostMapping("/save-name")
  public Map<String, String> saveName(@RequestBody Map<String, String> request) {
    String name = request.get("name");

    if (name == null || name.trim().isEmpty()) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "Name is required");
      return errorResponse;
    }

    Map<String, String> response = new HashMap<>();
    response.put("message", "Ваше имя '" + name + "' сохранено");
    response.put("savedName", name);
    response.put("status", "success");

    return response;
  }
}
