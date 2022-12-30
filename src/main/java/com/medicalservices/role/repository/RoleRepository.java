package com.medicalservices.role.repository;

import com.medicalservices.role.model.Role;
import com.medicalservices.role.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRole(UserRole role);
}
