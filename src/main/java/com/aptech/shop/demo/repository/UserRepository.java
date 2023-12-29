package com.aptech.shop.demo.repository;

import com.aptech.shop.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    boolean existsUserByEmail(String email);
}
