package com.practice.project.service;

import com.practice.project.domain.Admin;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.admin.AdminUpdateReqDto;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class AdminServiceTest {
    @Autowired
    AdminService adminService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    void join_운영자() {
        Admin firstAdmin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();

        firstAdmin.changeAddress(Address.builder()
                .country(Country.KR)
                .city("seoul")
                .street("1111")
                .zipcode("000000")
                .detailAddress("11111111")
                .build());

        adminService.save(firstAdmin);
    }

    @Test
    @Rollback
    void join_운영자_아이디중복() {
        Admin firstAdmin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();

        // 예외 발생 확인
        ApiResourceConflictException exception = assertThrows(ApiResourceConflictException.class, () -> {
            adminService.save(firstAdmin);
        }, "회원중복 예외 예상");
        String message = exception.getMessage();
        assertEquals("Admin already exists.", message);
    }

    @Test
    @Rollback(value = false)
    void update_운영자() {
        Admin admin = Admin.builder()
                .id("test")
                .name("테스터")
                .email("lee33398@namver.com")
                .phNumber("0000")
                .address(Address.builder()
                        .country(Country.KR)
                        .city("seoul")
                        .street("1111")
                        .zipcode("000000")
                        .detailAddress("11111111")
                        .build())
                .build();

        Long no = adminService.save(admin);
        em.flush();
        //em.clear();

        Admin beforeAdmin = adminService.findOne(no);

        AdminUpdateReqDto request = new AdminUpdateReqDto("0000-0",
                Address.builder()
                        .country(Country.EN)
                        .city("seoul")
                        .street("1111")
                        .zipcode("000000")
                        .detailAddress("11111111")
                        .build());

        if (! beforeAdmin.getAddress().equals(request.getAddress())) {
            System.out.println("다름");
            beforeAdmin.changeAddress(request.getAddress());
            beforeAdmin.changePhNumber(request.getPhNumber());

        } else {
            System.out.println("같음");
        }

        //adminService.update(no, request);
        Admin afterAdmin = adminService.findOne(no);
        assertEquals(afterAdmin.getAddress().getCountry(), request.getAddress().getCountry());
    }

    @Test
    void 이메일기준_운영자_조회() {
        Admin admin = Admin.builder()
                .id("test")
                .name("테스터")
                .email("lee33398@namver.com")
                .phNumber("0000")
                .address(Address.builder()
                        .country(Country.KR)
                        .city("seoul")
                        .street("1111")
                        .zipcode("000000")
                        .detailAddress("11111111")
                        .build())
                .build();
        adminService.save(admin);
        Admin findAdmin = adminService.findByEmail(admin.getEmail());

        log.info("admin.name: {}", findAdmin.getName());
        assertNotNull(findAdmin);
    }

}