package com.example.personalfinances.repository;

import com.example.personalfinances.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByLogin(String login);

  boolean existsByLogin(String login);
}
