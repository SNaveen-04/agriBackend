package com.agri.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agri.backend.entity.CartItem;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
}
