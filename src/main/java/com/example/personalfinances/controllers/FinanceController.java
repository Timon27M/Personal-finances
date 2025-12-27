// package com.example.personalfinances.controllers;
//
// import com.example.personalfinances.dto.auth.requests.LoginRequest;
// import com.example.personalfinances.dto.auth.requests.RegisterRequest;
// import com.example.personalfinances.dto.auth.responses.LoginResponse;
// import com.example.personalfinances.dto.auth.responses.UserResponse;
// import com.example.personalfinances.entity.User;
// import com.example.personalfinances.repository.UserRepository;
// import com.example.personalfinances.service.AuthService;
// import java.util.Map;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
//
// @RestController
// @RequestMapping("/user")
// @RequiredArgsConstructor
// public class FinanceController {
//
//    private final AuthService authService;
//    private final UserRepository userRepository;
//
//    @PostMapping("/user")
//    public ResponseEntity<?> getUser(@RequestBody LoginRequest request) {
//        User user = userRepository
//                .findByLogin(request.getLogin())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid login"));
//
//        return ResponseEntity.ok().body(new UserResponse(user.getUserId(), user.getLogin()));
//    }
// }
