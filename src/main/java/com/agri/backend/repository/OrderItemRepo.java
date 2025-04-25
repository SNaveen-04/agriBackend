package com.agri.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agri.backend.entity.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{

}
