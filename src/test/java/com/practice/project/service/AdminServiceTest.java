package com.practice.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.Admin;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.AdminDto.AdminCreateReqDto;
import com.practice.project.dto.AdminDto.AdminResDto;
import com.practice.project.dto.AdminDto.AdminSimpleResDto;
import com.practice.project.dto.AdminDto.AdminUpdateReqDto;
import com.practice.project.dto.common.Result;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles("mysql")
@Transactional
class AdminServiceTest {
    @Autowired
    AdminService adminService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Rollback(value = false)
    void join_운영자() throws JsonProcessingException {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .id("testAdmin")
                .name("홍길동")
                .email("testAdmin@naver.com")
                .build();

        reqDto.setAddress(Address.builder()
                .country(Country.KR)
                .city("seoul")
                .street("1111")
                .zipcode("000000")
                .detailAddress("11111111")
                .build());

        AdminResDto resDto = adminService.save(reqDto);
        Assertions.assertNotNull(resDto);
        Result<AdminResDto> resultResDto = new Result<>(resDto);

        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultResDto));
    }

    @Test
    void join_운영자_아이디중복() {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();

        // 예외 발생 확인
        ApiResourceConflictException exception = assertThrows(ApiResourceConflictException.class, () -> {
            adminService.save(reqDto);
        }, "회원중복 예외 예상");
        String message = exception.getMessage();
        assertEquals("Admin already exists.", message);
    }

    @Test
    @Rollback(value = false)
    void update_운영자() {
        Long no = 1L;
        // given
        Admin beforeAdmin = adminService.findOne(no);

        // when
        AdminUpdateReqDto reqDto = AdminUpdateReqDto.builder()
                .address(Address.builder()
                        .country(Country.EN)
                        .city("busan")
//                        .street("1111")
//                        .zipcode("000000")
//                        .detailAddress("11111111")
                        .build())
                .phNumber("000-0000-0000")
                .build();

        adminService.update(no, reqDto);

        // then
        Admin afterAdmin = adminService.findOne(no);
        assertEquals(afterAdmin.getAddress().getCountry(), reqDto.getAddress().getCountry());
    }

    @Test
    @DisplayName("ID기반 특정 운영자 정보 조회")
    void 특정_운영자_정보_조회() {
        // given
        String id = "lee33398";

        // when
        AdminResDto resDto = adminService.findById(id);

        // then
        assertEquals(resDto.getId(), id);
    }

    @Test
    @DisplayName("전체 운영자 정보 조회 pageable")
    void 전체_운영자_목록_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 2);

        // when
        List<AdminResDto> resDtoList = adminService.findAdmins(pageable);

        // then
        Assertions.assertEquals(resDtoList.size(), 2);
    }

    @Test
    @DisplayName("운영자 삭제")
    void 운영자_삭제() {
        // given
        Long no = 1L;

        // when
        AdminSimpleResDto resDto = adminService.removeAdmin(no);

        // then
        assertEquals(resDto.getNo(), no);
    }

    @Test
    @DisplayName("운영자 삭제 - 존재하지 않는 운영자")
    void 존재하지않는_운영자_삭제() {
        // given
        Long no = 100L;

        // when
        ApiResourceNotFoundException exception = assertThrows(ApiResourceNotFoundException.class, () -> {
            adminService.removeAdmin(no);
        });

        // then
        String msg = exception.getMessage();
        assertEquals(msg, "Admin not exist.");
    }
}