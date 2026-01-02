package com.example.personalfinances.service;

import com.example.personalfinances.entity.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  // prod версия
  // private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

  // dev версия для удобства тестирования
  Dotenv dotenv = Dotenv.configure().load();
  private String key = dotenv.get("JWT_SECRET", "default_secret");
  private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));

  private final long expirationTime = 1000 * 60 * 60 * 24;

  public String generateToken(User user) {
    return Jwts.builder()
        .setSubject(user.getUserId().toString())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(secretKey)
        .compact();
  }

  public UUID extractUserID(String token) {
    String subject =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();

    return UUID.fromString(subject);
  }

  public boolean isTokenValid(String token) throws ServletException {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (MalformedJwtException e) {
      return false;
    }
  }

  public boolean isTokenExpired(String token) {
    try {
      Claims claims =
          Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

      return claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return true;
    }
  }
}
