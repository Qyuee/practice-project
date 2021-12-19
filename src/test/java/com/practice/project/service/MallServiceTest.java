package com.practice.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.MallDto.MallCreateReqDto;
import com.practice.project.dto.MallDto.MallResDto;
import com.practice.project.exception.exhandler.ApiBadRequestException;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.repository.AdminRepository;
import com.practice.project.repository.MallRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class MallServiceTest {
    @Autowired
    MallService mallService;

    @Autowired
    MallRepository mallRepository;

    @Autowired
    AdminService adminService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ObjectMapper objectMapper;

    //@BeforeAll
    @Transactional
    void 테스트_데이터_설정() {
        Admin admin1 = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminRepository.save(admin1);

        Admin admin2 = Admin.builder()
                .id("lee33397")
                .name("홍길동")
                .email("lee33397@naver.com")
                .build();
        adminRepository.save(admin2);

        for (Country value : Country.values()) {
            mallRepository.save(Mall.builder().name("admin1 테스트몰 "+value.name())
                    .admin(admin1)
                    .countryType(value)
                    .build()
            );
        }

        for (Country value : Country.values()) {
            mallRepository.save(Mall.builder().name("admin2 테스트몰 "+value.name())
                    .admin(admin2)
                    .countryType(value)
                    .build()
            );
        }
    }

    @Test
    @DisplayName("몰 정보 저장")
    @Transactional
    @Rollback(value = false)
    void 몰_save() throws Exception {
        Admin admin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminService.save(admin);

        MallCreateReqDto req = MallCreateReqDto.builder()
                .admin(admin)
                .mallName("테스트몰")
                .countryType(Country.KR)
                .build();

        MallResDto savedMall = mallService.save(req);
        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(savedMall));
    }

    @Test
    @DisplayName("몰 정보 저장 - 동일국가 2개 이상 생성한 경우")
    @Transactional // @Transactional-> 테스트 케이스에서는 롤백도 함께 수행함
    //@Rollback(value = false) // -> 이 부분이 'Transaction silently rolled back because it has been marked as rollback-only' 발생
    void 몰_save_국가중복() throws Exception {
        Admin admin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminService.save(admin);

        // 몰 1번, 운영자 동일
        MallCreateReqDto req = MallCreateReqDto.builder()
                .admin(admin)
                .mallName("테스트몰")
                .countryType(Country.KR)
                .build();

        mallService.save(req);

        // 몰 2번, 운영자 동일
        MallCreateReqDto req2 = MallCreateReqDto.builder()
                .admin(admin)
                .mallName("테스트몰2")
                .countryType(Country.KR)
                .build();

        ApiResourceConflictException exception = assertThrows(ApiResourceConflictException.class, () -> {
            mallService.save(req2);
        }, "멀티몰 중복 예외 예상");

        String message = exception.getMessage();
        // ->  org.springframework.transaction.UnexpectedRollbackException
        assertEquals("There is already a mall in the country.", message);

        Mall findNotExistsMall = mallRepository.findByName(req2.getMallName());
        Assertions.assertNull(findNotExistsMall);
    }

    @Test
    @DisplayName("몰 정보 저장 - 동일한 몰 이름")
    @Transactional
    void 몰_save_몰_이름_중복() throws Exception {
        Admin admin = Admin.builder()
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();
        adminService.save(admin);

        MallCreateReqDto req = MallCreateReqDto.builder()
                .admin(admin)
                .mallName("테스트몰")
                .countryType(Country.KR)
                .build();
        mallService.save(req);

        MallCreateReqDto req2 = MallCreateReqDto.builder()
                .admin(admin)
                .mallName("테스트몰")
                .countryType(Country.EN)
                .build();

        ApiResourceConflictException exception = assertThrows(ApiResourceConflictException.class, () -> {
            mallService.save(req2);
        }, "멀티 몰 생성 중복 예외 예상");

        String message = exception.getMessage();
        // ->  org.springframework.transaction.UnexpectedRollbackException
        assertEquals("The name of the mall is already in use.", message);
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

        MallCreateReqDto req = MallCreateReqDto.builder()
                .admin(admin)
                .mallName("테스트몰")
                .countryType(Country.KR)
                .build();

        mallService.save(req);
    }

    @Test
    @DisplayName("몰 정보 리스트")
    void 몰_list() {
        int page = 0;
        int size = 5;
        String sortKey = "countryType";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortKey));

        List<MallResDto> mallList = mallService.findAll(pageable);
        for (MallResDto mallResDto : mallList) {
            log.info(mallResDto.toString());
        }

        assertEquals(mallList.size(), 5);
    }

    @Test
    @DisplayName("유효하지 않는 sortKey")
    void 몰_list_유효하지않는_sort_key() {
        int page = 0;
        int size = 5;
        String sortKey = "test, countryType";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortKey));

        ApiBadRequestException exception = assertThrows(ApiBadRequestException.class, () -> {
            mallService.findAll(pageable);
        }, "sort 검색조건 validate 실패");
        String message = exception.getMessage();
        assertEquals(sortKey+" is not supported key. please confirm your sort key.", message);
    }
}