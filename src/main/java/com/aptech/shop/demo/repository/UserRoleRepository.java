package com.aptech.shop.demo.repository;

import com.aptech.shop.demo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
