package com.practice.project.repository;

import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Country;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
//@SpringBootTest
//@Transactional
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MallRepositoryTest {
    @Autowired
    MallRepository mallRepository;

    @Autowired
    AdminRepository adminRepository;

    @BeforeAll
    @Transactional
    @Rollback(value = false)
    void 테스트_데이터_설정() {
        for (int i = 0; i < 10; i++) {
            Admin admin = Admin.builder()
                    .adminId("lee3339"+i)
                    .name("이동석")
                    .email("lee3339"+i+"@naver.com")
                    .build();
            adminRepository.save(admin);

            for (int j = 0; j < 10; j++) {
                mallRepository.save(Mall.builder()
                        .name(i+"번 admin_"+j+"번_테스트몰")
                        .admin(admin)
                        .countryType(Country.KR)
                        .build()
                );
            }
        }
    }

    @AfterAll
    @Transactional
    void 테스트_데이터_초기화() {
        log.info("");
    }

    /**
     * Admin -> OneToMany -> EAGER(LAZY가 default)
     * Mall -> ManyToOne -> LAZY (테스트에서 이 부분은 상관없음)
     */
    @Test
    void N_PLUSE_1_TEST() {
        List<Admin> adminList = adminRepository.findAll();
        for (Admin admin : adminList) {
            // 1+N 쿼리 발생
            log.info("admin info: {}", admin.getName());
        }

        /*
         OneToMany의 경우 fetch 전략이 기본적으로 LAZY이기에 1+N 이슈가 없는 것처럼 보인다.
         하지만 실제 연관관계에 있는 mall의 정보를 사용하는 시점에 1+N 이슈가 재발한다.
         JPQL은 연관관계를 무시하고 엔티티만 기준으로 쿼리를 조회한다.
         이를 해결하는 방법은 여러가지가 있다. jpaRepository는 지원하지 않는다.
         */
        List<String> mallNameList = adminList.stream().map(Admin::getMallList).flatMap(Collection::stream)
                .map(Mall::getName)
                .collect(Collectors.toList());

        //List<Stream<String>> collect = adminList.stream().map(Admin::getMallList).map(malls -> malls.stream().map(Mall::getName)).collect(Collectors.toList());
        //List<String> mallNameList2 = adminList.stream().map(Admin::getMallList).flatMap(malls -> malls.stream().map(Mall::getName)).collect(Collectors.toList());

        assertFalse(mallNameList.isEmpty());
    }

    /**
     * fetch join은 inner join으로 동작한다.
     * 단점. 페이징 불가, fetch전략 사용불가(LAZY하지 못함)
     */
    @Test
    void fetch_join_TEST() {
        List<Admin> FetchJoinedAdminList = adminRepository.findAllJoinFetch();
        List<String> mallNameList = FetchJoinedAdminList.stream().flatMap(admin -> admin.getMallList().stream().map(Mall::getName)).collect(Collectors.toList());
        assertFalse(mallNameList.isEmpty());
    }

    @Test
    @Rollback(value = false)
    void 몰_저장_테스트() {
        Mall mall = Mall.builder()
                .name("테스트몰")
                .countryType(Country.KR)
                .build();
        mallRepository.save(mall);
        Mall findMall = mallRepository.getById(mall.getNo());
        assertNull(mall.getNo());
    }

    @Test
    @Rollback(value = false)
    void 몰_저장_테스트_fk_not_null() {
        Admin admin = Admin.builder()
                .adminId("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminRepository.save(admin);

        Mall mall = Mall.builder()
                .name("테스트몰 - 직접입력")
                .countryType(Country.KR)
                .admin(admin)
                .build();

        mallRepository.save(mall);
        Mall findMall = mallRepository.getById(mall.getNo());

        log.info("test:{}", findMall.getName());
        Assertions.assertNotNull(findMall);
    }

    @Test
    @DisplayName("몰 리스트 가져오기")
    void 몰_리스트() {
        List<Mall> mallList = mallRepository.findAll();
        for (Mall mall : mallList) {
            log.info("mall.no:{}, mall.name:{}", mall.getNo(), mall.getName());
        }
    }

    @Test
    @DisplayName("몰 리스트 pagingnation 테스트")
    void 몰_리스트_pageable() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Mall> mallPage = mallRepository.findAll(pageRequest);
        log.info(mallPage.toString());

        List<Mall> mallList = mallPage.get().collect(Collectors.toList());
        for (Mall mall : mallList) {
            log.info(mall.toString());
        }
    }

    @Test
    @DisplayName("몰 name으로 조회")
    void 몰_정보_조회_by_name() {
        mallRepository.findByName("테스트몰").ifPresentOrElse(mall -> {
            Assertions.assertNotNull(mall);
        }, () -> {

        });
    }
}