package com.agri.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agri.backend.entity.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
}
