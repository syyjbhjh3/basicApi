package com.test.api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.api.entity.User;
import com.test.api.mapping.UserMapping;

@Repository
public interface UserJpaRepo extends JpaRepository<User, String> {
	List<User> findByUserId(String userId);
	UserMapping findByUserNoAndUserId(Long userNo, String userId);
}
