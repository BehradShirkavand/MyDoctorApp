package com.example.mydoctor.repository;

import java.util.List;
import java.util.Optional;

import com.example.mydoctor.entity.User;
import com.example.mydoctor.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mydoctor.enums.Status;


public interface UserRepository extends JpaRepository<User, Integer> {
        
    List<User> findAllByRoles_NameAndStatus(ERole roleName, Status status);

    Optional<User> findByIdAndRoles_NameAndAndStatus(int id, ERole roleName, Status status);

    Optional<User> findByRoles_NameAndUsernameAndStatus(ERole roleName, String username, Status status);

    Boolean existsByRoles_NameAndUsernameAndStatus(ERole roleName, String username, Status status);

    Optional<User> findByRoles_NameAndPhoneNumberAndStatus(ERole roleName, String phoneNumber, Status status);

    Boolean existsByRoles_NameAndPhoneNumberAndStatus(ERole roleName, String phoneNumber, Status status);
}
