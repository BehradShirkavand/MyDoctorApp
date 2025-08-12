package com.example.mydoctor.repository;

import com.example.mydoctor.entity.Role;
import com.example.mydoctor.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
