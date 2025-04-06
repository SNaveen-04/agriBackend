package com.agri.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agri.backend.entity.Category;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Long>{
	@Query("SELECT MAX(c.id) FROM Category c") // Use entity name, not table name
    Long findLatestCategoryId();
}
