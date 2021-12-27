package com.practice.project.repository;

import com.practice.project.domain.Mall;
import com.practice.project.domain.Member;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsMembersById(String id);
    Boolean existsMembersByEmail(String email);
    Optional<Member> findByMallAndId(Mall mall, String id);
    Page<Member> findAllByMall(Mall mall, Pageable pageable);
    Integer deleteAllByMall(Mall mall);
}
