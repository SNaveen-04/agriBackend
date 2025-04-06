package com.agri.backend.repository;

import com.agri.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
	Transaction findByOrderId(Long orderId);
}
