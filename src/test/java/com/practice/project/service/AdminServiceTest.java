package com.practice.project.service;

import com.practice.project.domain.Admin;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.admin.AdminCreateResponse;
import com.practice.project.dto.admin.AdminUpdateRequest;
import com.practice.project.exception.exhandler.ApiResourceDuplicateException;
import com.practice.project.repository.AdminRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        ApiResourceDuplicateException exception = assertThrows(ApiResourceDuplicateException.class, () -> {
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

        AdminUpdateRequest request = new AdminUpdateRequest("0000-0",
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

}