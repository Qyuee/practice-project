package com.practice.project.repository;


import com.practice.project.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findById(String id);
    Boolean existsByIdOrEmail(String id, String email);

    @Query("select a from Admin a join fetch a.mallList")
    List<Admin> findAllJoinFetch();
}
