package com.raulcg.blog.repositories;

import com.raulcg.blog.UserRole;
import com.raulcg.blog.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(UserRole roleName);
}
