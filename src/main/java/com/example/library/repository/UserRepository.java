package com.example.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library.entity.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    java.util.Optional<AppUser> findByUsername(String username);
}
