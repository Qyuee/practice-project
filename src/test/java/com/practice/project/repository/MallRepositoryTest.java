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

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    void 테스트_데이터_설정() {
        Admin admin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminRepository.save(admin);

        for (int i = 0; i < 30; i++) {
            mallRepository.save(Mall.builder().name("테스트몰"+i)
                    .admin(admin)
                    .countryType(Country.KR)
                    .build()
            );
        }
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
                .id("lee33398")
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
        assertEquals(mallList.size(), 30);
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
        Mall findNotExistsMall = mallRepository.findByName("테스트몰");
        Assertions.assertNull(findNotExistsMall);
    }
}