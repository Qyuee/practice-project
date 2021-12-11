package com.practice.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.Admin;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.mall.MallDto.MallCreateReq;
import com.practice.project.dto.mall.MallDto.Response;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
class MallServiceTest {
    @Autowired
    MallService mallService;

    @Autowired
    AdminService adminService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("몰 정보 저장")
    @Rollback(value = false)
    void 몰_save() throws Exception {
        Admin admin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminService.save(admin);

        MallCreateReq req = MallCreateReq.builder()
                .admin(admin)
                .mallName("테스트몰")
                .country(Country.KR)
                .build();

        Response savedMall = mallService.save(req);
        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(savedMall));
    }

    @Test
    @DisplayName("몰 정보 저장 - 동일국가 2개 이상 생성한 경우")
    @Transactional // @Transactional-> 테스트 케이스에서는 롤백도 함께 수행함
    @Rollback(value = true)
    void 몰_save_국가중복() throws Exception {
        Admin admin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminService.save(admin);

        // 몰 1번, 운영자 동일
        MallCreateReq req = MallCreateReq.builder()
                .admin(admin)
                .mallName("테스트몰")
                .country(Country.KR)
                .build();

        mallService.save(req);  // 여기까지는 성공.

        // 몰 2번, 운영자 동일
        MallCreateReq req2 = MallCreateReq.builder()
                .admin(admin)
                .mallName("테스트몰2")
                .country(Country.KR)
                .build();

        ApiResourceConflictException exception = assertThrows(ApiResourceConflictException.class, () -> {
            mallService.save(req2); // ???
        }, "멀티몰 중복 예외 예상");

        String message = exception.getMessage();
        // ->  org.springframework.transaction.UnexpectedRollbackException

        assertEquals("There is already a mall in the country.", message);
    }

    @Test
    @DisplayName("몰 정보 저장 - 운영자 정보 없음")
    @Rollback(value = false)
    void 몰_save_admin_없음() {
        Admin admin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminService.save(admin);

        MallCreateReq req = MallCreateReq.builder()
                .admin(admin)
                .mallName("테스트몰")
                .country(Country.KR)
                .build();

        mallService.save(req);
    }
}