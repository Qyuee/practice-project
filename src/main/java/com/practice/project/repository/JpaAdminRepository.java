/*
package com.practice.project.repository;

import com.practice.project.domain.Admin;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class JpaAdminRepository implements AdminRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Long save(Admin admin) {
        em.persist(admin);
        return admin.getNo();
    }

    @Override
    public Admin findOne(Long no) {
        return em.find(Admin.class, no);
    }

    @Override
    public Optional<Admin> findById(String id) {
        try {
            return Optional.ofNullable(
                    em.createQuery("select a from Admin a where a.id = :id", Admin.class)
                            .setParameter("id", id)
                            .getSingleResult());

        } catch (NoResultException e) {
            throw new ApiResourceNotFoundException("Admin not exist.");
        }
    }

    @Override
    public Admin findByEmail(String email) {
        return em.createQuery("select a from Admin a where a.email = :email", Admin.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public List<Admin> findByIdOrEmail(String id, String email) {
        return em.createQuery("select a from Admin a where a.id = :id Or a.email = :email", Admin.class)
                .setParameter("id", id)
                .setParameter("email", email)
                .getResultList();
    }

    //@TODO 페이지네이션 추가 필요 (default: 10개)
    @Override
    public List<Admin> findAll() {
        return em.createQuery("select a from Admin a", Admin.class)
                .getResultList();
    }

    @Override
    public Long remove(Admin admin) {
        em.remove(admin);
        return admin.getNo();
    }
}
*/
