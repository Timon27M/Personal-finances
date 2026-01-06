package com.example.personalfinances.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.personalfinances.dto.auth.requests.LoginRequest;
import com.example.personalfinances.dto.auth.requests.RegisterRequest;
import com.example.personalfinances.entity.User;
import com.example.personalfinances.entity.Wallet;
import com.example.personalfinances.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private JwtService jwtService;

  @InjectMocks private AuthService authService;

  private RegisterRequest validRegisterRequest;
  private LoginRequest validLoginRequest;
  private User existingUser;

  @BeforeEach
  void setUp() {
    validRegisterRequest = new RegisterRequest("testUser", "Password123!");
    validLoginRequest = new LoginRequest("testUser", "Password123!");

    existingUser = new User("testUser", "encodedPasswordHash");
    existingUser.setWallet(new Wallet(existingUser));
  }

  @Test
  void register_withValidRequest_shouldCreateUserAndWallet() {
    // Given
    String encodedPassword = "encodedPasswordHash";
    String expectedToken = "jwtToken123";

    when(userRepository.existsByLogin(validRegisterRequest.getLogin())).thenReturn(false);
    when(passwordEncoder.encode(validRegisterRequest.getPassword())).thenReturn(encodedPassword);
    when(userRepository.save(any(User.class)))
        .thenAnswer(
            invocation -> {
              User user = invocation.getArgument(0);
              return user; // возвращаем тот же объект
            });

    // When
    User result = authService.register(validRegisterRequest);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getLogin()).isEqualTo("testUser");
    assertThat(result.getPasswordHash()).isEqualTo(encodedPassword);
    assertThat(result.getWallet()).isNotNull();
    assertThat(result.getWallet().getUser()).isEqualTo(result);

    verify(userRepository, times(1)).existsByLogin("testUser");
    verify(passwordEncoder, times(1)).encode("Password123!");
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void login_withValidCredentials_shouldReturnToken() {
    // Given
    String expectedToken = "jwt.token.here";

    when(userRepository.existsByLogin(validLoginRequest.getLogin())).thenReturn(true);
    when(userRepository.findByLogin(validLoginRequest.getLogin()))
        .thenReturn(Optional.of(existingUser));
    when(passwordEncoder.matches(validLoginRequest.getPassword(), existingUser.getPasswordHash()))
        .thenReturn(true);
    when(jwtService.generateToken(existingUser)).thenReturn(expectedToken);

    // When
    String result = authService.login(validLoginRequest);

    // Then
    assertThat(result).isEqualTo(expectedToken);

    verify(userRepository, times(1)).existsByLogin("testUser");
    verify(userRepository, times(1)).findByLogin("testUser");
    verify(passwordEncoder, times(1)).matches("Password123!", "encodedPasswordHash");
    verify(jwtService, times(1)).generateToken(existingUser);
  }
}
