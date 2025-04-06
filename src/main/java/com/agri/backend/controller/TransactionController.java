package com.agri.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.agri.backend.entity.Transaction;
import com.agri.backend.service.TransactionService;

import java.util.List;
import java.util.Optional;

//@RestController
//@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/process")
    public Transaction processTransaction(@RequestParam Long orderId, @RequestParam String paymentMethod, @RequestParam double amount) {
        return transactionService.processTransaction(orderId, paymentMethod, amount);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Optional<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PutMapping("/{transactionId}/status")
    public String updateTransactionStatus(@PathVariable Long transactionId, @RequestParam Transaction.PaymentStatus status) {
        transactionService.updateTransactionStatus(transactionId, status);
        return "Transaction status updated successfully";
    }
}
