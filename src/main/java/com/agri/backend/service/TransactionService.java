package com.agri.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agri.backend.entity.Order;
import com.agri.backend.entity.Transaction;
import com.agri.backend.repository.OrderRepo;
import com.agri.backend.repository.TransactionRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepository;

    @Autowired
    private OrderRepo orderRepository;

    public Transaction processTransaction(Long orderId, String paymentMethod, double amount) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Order order = orderOptional.get();
        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setAmount(amount);
        transaction.setStatus(Transaction.PaymentStatus.PENDING);
        transaction.setTransactionTimestamp(LocalDateTime.now());



        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions;
    }

    public Optional<Transaction> getTransactionById(Long id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        return transactionOptional;
    }

    public void updateTransactionStatus(Long transactionId, Transaction.PaymentStatus status) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            transaction.setStatus(status);
            transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Transaction not found");
        }
    }

}
