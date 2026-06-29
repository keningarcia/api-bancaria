package com.keningarcia.api_bancaria.repository;

import com.keningarcia.api_bancaria.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountIdOrTargetAccountIdOrderByCreatedAtDesc(Long sourceId, Long targetId);
    List<Transaction> findBySourceAccountIdOrderByCreatedAtDesc(Long accountId);
    List<Transaction> findByTargetAccountIdOrderByCreatedAtDesc(Long accountId);
}
