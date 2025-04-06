package com.agri.backend.repository;

import com.agri.backend.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
	   List<Order> findByUserId(Long userId);
}
