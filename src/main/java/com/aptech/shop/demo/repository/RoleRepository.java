package com.aptech.shop.demo.repository;

import com.aptech.shop.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByNameIn(List<String> nameList);
}
