package com.devon.building.repository;

import com.devon.building.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerIdAndCodeAndActiveTrue(Long customerId, String code);
    Optional<Transaction> findByIdAndActiveTrue(Long id);
}
