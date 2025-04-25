package com.agri.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agri.backend.entity.User;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	 Optional<User> findByEmail(String email);	
	 Optional<User> findByNumber(String number);
	 List<User> findByIdIn(List<Long> ids);
}
