package com.example.personalfinances.repository;

import com.example.personalfinances.entity.UserFinance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserFinanceRepository extends JpaRepository<UserFinance, Long> {
    Optional<UserFinance> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
