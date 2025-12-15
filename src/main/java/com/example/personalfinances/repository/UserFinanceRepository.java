package com.example.personalfinances.repository;

import com.example.personalfinances.entity.UserFinance;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFinanceRepository extends JpaRepository<UserFinance, UUID> {
  Optional<UserFinance> findByUserName(String userName);

  boolean existsByUserName(String userName);
}
