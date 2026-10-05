package com.example.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library.entity.Branches;

@Repository
public interface BranchesRepository extends JpaRepository<Branches, Long> {
     List<Branches> findByActiveTrueAndDeletedFalse();
     java.util.Optional<Branches> findByName(String name);
}
