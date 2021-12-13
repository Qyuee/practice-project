package com.practice.project.repository;

import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MallRepository extends JpaRepository<Mall, Long> {
    /*@Query("select 1 from mall m where m.admin_no = :adminNo and m.country = :coutry limit 1")
    boolean existsByCountryType(@Param("adminNo") Long adminNo, @Param("country") Country country);*/

    boolean existsMallByAdminAndCountryType(Admin admin, Country country);
    Mall findByName(String name);
}
