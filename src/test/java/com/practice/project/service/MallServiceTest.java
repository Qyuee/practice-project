package com.practice.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.domain.statusinfo.MallStatus;
import com.practice.project.dto.AdminDto.AdminCreateReqDto;
import com.practice.project.dto.MallDto;
import com.practice.project.dto.MallDto.MallCreateReqDto;
import com.practice.project.dto.MallDto.MallResDto;
import com.practice.project.dto.MallDto.MallStatusResDto;
import com.practice.project.dto.MallDto.MallUpdateReqDto;
import com.practice.project.exception.exhandler.ApiBadRequestException;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles("mysql")
class MallServiceTest {
    @Autowired
    MallService mallService;

    @Autowired
    AdminService adminService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("몰 정보 저장")
    @Transactional
    //@Rollback(value = false)
    void 몰_save() throws Exception {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .adminId("test001")
                .name("이동석")
                .email("test001@naver.com")
                .build();
        adminService.save(reqDto);

        MallCreateReqDto req = MallCreateReqDto.builder()
                .adminId(reqDto.getAdminId())
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
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .adminId("test001")
                .name("이동석")
                .email("test001@naver.com")
                .build();
        adminService.save(reqDto);

        MallCreateReqDto req = MallCreateReqDto.builder()
                .adminId(reqDto.getAdminId())
                .mallName("테스트몰")
                .countryType(Country.KR)
                .build();
        mallService.save(req);

        // 몰 2번, 운영자 동일
        MallCreateReqDto req2 = MallCreateReqDto.builder()
                .adminId(reqDto.getAdminId())
                .mallName("테스트몰2")
                .countryType(Country.KR)
                .build();

        ApiResourceConflictException exception = assertThrows(ApiResourceConflictException.class, () -> {
            mallService.save(req2);
        }, "멀티몰 중복 예외 예상");

        String message = exception.getMessage();
        // ->  org.springframework.transaction.UnexpectedRollbackException
        assertEquals("There is already a mall in the country.", message);
    }

    @Test
    @DisplayName("몰 정보 저장 - 동일한 몰 이름")
    @Transactional
    void 몰_save_몰_이름_중복() throws Exception {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .adminId("test001")
                .name("이동석")
                .email("test001@naver.com")
                .build();
        adminService.save(reqDto);

        MallCreateReqDto req1 = MallCreateReqDto.builder()
                .adminId(reqDto.getAdminId())
                .mallName("테스트몰")
                .countryType(Country.EN)
                .build();
        mallService.save(req1);

        MallCreateReqDto req2 = MallCreateReqDto.builder()
                .adminId(reqDto.getAdminId())
                .mallName("테스트몰")
                .countryType(Country.KR)
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
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .adminId("unknown")
                .name("존재하지않는 계정")
                .email("unknown@naver.com")
                .build();

        MallCreateReqDto req = MallCreateReqDto.builder()
                .admin(AdminCreateReqDto.toEntity(reqDto))
                .mallName("테스트몰")
                .countryType(Country.KR)
                .build();

        ApiResourceNotFoundException exception = Assertions.assertThrows(ApiResourceNotFoundException.class, () -> {
            mallService.save(req);
        });

        String message = exception.getMessage();
        assertEquals(message, "Admin not exist.");
    }

    @Test
    @DisplayName("몰 정보 리스트")
    @Order(6)
    void 몰_list() {
        int page = 0;
        int size = 5;
        String sortKey = "countryType";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortKey));

        List<MallResDto> mallList = mallService.findAll(pageable);
        for (MallResDto mallResDto : mallList) {
            log.info(mallResDto.toString());
        }

        assertEquals(2, mallList.size());
    }

    @Test
    @DisplayName("유효하지 않는 sortKey")
    @Order(7)
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

    @Test
    @DisplayName("몰 정보 수정")
    @Order(8)
    void 몰_정보_수정() {
        // given
        String adminId = "lee33397";

        Long mallNo = 1L;
        MallUpdateReqDto reqDto = MallUpdateReqDto.builder()
                .adminId(adminId)
                .mallName("수정되는 몰 이름")
                .address(Address.builder()
                        .country(Country.EN)
                        .build())
                .build();

        MallResDto exMallInfo = mallService.findByNo(adminId, mallNo);

        // when
        mallService.update(mallNo, reqDto);

        // then
        MallResDto afterMallInfo = mallService.findByNo(adminId, mallNo);
        assertNotEquals(exMallInfo.getAddress().getCountry(), afterMallInfo.getAddress().getCountry());
        assertEquals(reqDto.getAddress().getCountry(), afterMallInfo.getAddress().getCountry());

    }

    @Test
    @DisplayName("몰 정보 수정 - 실패케이스")
    @Order(9)
    void 몰_정보_수정_실패() {
        // given
        String adminId = "lee33398";
        Long mallNo = 100L; // 존재하지 않는 몰no 정보

        // when
        MallUpdateReqDto reqDto = MallUpdateReqDto.builder()
                .adminId(adminId)
                .mallName("수정되는 몰 이름")
                .address(Address.builder()
                        .country(Country.EN)
                        .build())
                .build();

        // then
        ApiResourceNotFoundException exception = Assertions.assertThrows(ApiResourceNotFoundException.class, () -> {
            mallService.update(mallNo, reqDto);
        }, "몰 정보 존재하지 않기에 에러 발생");
        String message = exception.getMessage();
        assertEquals("Mall not exist.",message);
    }

    @Test
    @DisplayName("몰 상태 변경")
    @Order(10)
    @Rollback(value = false)
    void 몰_상태_변경() throws JsonProcessingException {
        String adminId = "lee33397";
        Long mallNo = 1L;
        MallStatus status = MallStatus.DORMANT;
        MallStatusResDto mallStatusResDto = mallService.updateStatus(adminId, mallNo, status);

        MallResDto afterMallInfo = mallService.findByNo(adminId, mallNo);
        assertEquals(afterMallInfo.getStatus(), status);
        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mallStatusResDto));
    }

    @Test
    @DisplayName("몰 삭제")
    @Order(1000)
    void 몰_정보_삭제() {
        // given
        String adminId = "lee33397";
        Long mallNo = 1L;

        // when
        mallService.delete(adminId, mallNo);

        // then
        ApiResourceNotFoundException exception = Assertions.assertThrows(ApiResourceNotFoundException.class, () -> {
            mallService.findByNo(adminId, mallNo);
        });
        String message = exception.getMessage();
        assertEquals(message, "Mall not exist.");
    }
}