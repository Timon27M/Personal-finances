package com.example.personalfinances.security;

import com.example.personalfinances.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  public JwtAuthenticationFilter(final JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      if (!jwtService.isTokenExpired(token) || jwtService.isTokenValid(token)) {
        try {
          UUID userId = jwtService.extractUserID(token);

          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(
                  userId, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
          request.setAttribute("authException", new ServletException("Ошибка аутентификации: "));
          throw new ServletException("Невозможно установить аутентификацию пользователя: ", e);
        }
      } else {
        request.setAttribute("authException", new ServletException("Ошибка аутентификации: "));
        throw new ServletException("Невозможно установить аутентификацию пользователя: ");
      }
    }
    filterChain.doFilter(request, response);
  }
}
