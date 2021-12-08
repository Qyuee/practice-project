package com.practice.project.repository;


import com.practice.project.domain.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    Long save(Admin admin);
    Admin findOne(Long no);
    Optional<Admin> findById(String id);
    Admin findByEmail(String id);
    List<Admin> findByIdOrEmail(String id, String email);
    List<Admin> findAll();
    Long remove(Admin admin);
}
