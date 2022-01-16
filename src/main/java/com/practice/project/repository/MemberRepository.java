package com.practice.project.repository;

import com.practice.project.domain.Mall;
import com.practice.project.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMembersByMemberId(String memberId);
    boolean existsMembersByEmail(String email);
    Optional<Member> findByMallAndMemberId(Mall mall, String memberId);
    Optional<Member> findByMemberId(String memberId);
    Page<Member> findAllByMall(Mall mall, Pageable pageable);
    Integer deleteAllByMall(Mall mall);
}
