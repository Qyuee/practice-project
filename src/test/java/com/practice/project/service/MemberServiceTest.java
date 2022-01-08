package com.practice.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.common.Gender;
import com.practice.project.dto.MemberDto.MemberCreateReqDto;
import com.practice.project.dto.MemberDto.MemberCreateResDto;
import com.practice.project.dto.MemberDto.MemberSearchResDto;
import com.practice.project.dto.MemberDto.MemberUpdateReqDto;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("mysql")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 목록 리스트 조회")
    void 전체_회원_목록_조회() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "birthdate"));
        List<MemberSearchResDto> memberList = memberService.findAll(pageable);
        for (MemberSearchResDto dto : memberList) {
            log.info("{}", dto.toString());
        }
    }

    @Test
    @DisplayName("특정몰 회원 목록 리스트 조회")
    void 특정몰_회원_목록_리스트_조회() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "birthdate"));
        List<MemberSearchResDto> memberList = memberService.findAllByMall(1L, pageable);
        for (MemberSearchResDto dto : memberList) {
            log.info("{}", dto.toString());
        }
    }

    @Test
    @DisplayName("특정 몰 회원 정보 조회")
    void 특정몰_회원_정보_조회() {
        MemberCreateReqDto dto = MemberCreateReqDto.builder()
                .id("custom100")
                .name("회원100")
                .email("custom100@naver.com")
                .mallNo(1L)
                .gender(Gender.F)
                .birthdate(LocalDate.of(1990, 1, 1))
                .build();
        MemberCreateResDto saveMember = memberService.save(dto);
        MemberSearchResDto searchMember = memberService.findByMallNoAndId(saveMember.getMallNo(), saveMember.getId());
        log.info("{}", searchMember);
    }

    @Test
    @DisplayName("회원 생성 서비스 로직 검증")
    void 회원_생성() throws JsonProcessingException {
        MemberCreateReqDto dto = MemberCreateReqDto.builder()
                .id("testCustom002")
                .name("회원002")
                .email("testCustom002@naver.com")
                .mallNo(1L)
                .gender(Gender.F)
                .birthdate(LocalDate.of(1992, 12, 29))
                .build();

        MemberCreateResDto createResDto = memberService.save(dto);
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createResDto);
        log.info(result);
    }

    @Test
    @DisplayName("회원 생성 서비스 실패 로직 검증")
    void 회원_생성_실패() {
        MemberCreateReqDto dto = MemberCreateReqDto.builder()
                .id("testCustom001")
                .name("회원001")
                .email("testCustom001@naver.com")
                .mallNo(1L)
                .gender(Gender.F)
                .birthdate(LocalDate.of(1992, 12, 29))
                .build();
        memberService.save(dto);

        MemberCreateReqDto dto2 = MemberCreateReqDto.builder()
                .id("testCustom001")
                .name("회원001")
                .email("testCustom001@naver.com")
                .mallNo(1L)
                .gender(Gender.F)
                .birthdate(LocalDate.of(1992, 12, 29))
                .build();

        // 예외 발생 확인
        ApiResourceConflictException exception = assertThrows(ApiResourceConflictException.class, () -> {
            memberService.save(dto2);
        }, "회원 정보 중복 예외 예상");
        String message = exception.getMessage();
        log.error("msg:{}", message);
        Assertions.assertEquals("'testCustom001' was already used.", message);
    }

    @Test
    @DisplayName("회원 정보 수정 서비스")
    void 회원정보_수정() throws JsonProcessingException {
        Long mallNo = 1L;
        String memberId = "custom001";

        MemberUpdateReqDto updateDto = MemberUpdateReqDto
                .builder()
                .id(memberId)
                .mallNo(mallNo)
                .phNumber("010-9999-9999")
                .birthdate(LocalDate.of(2000, 10, 10))
                .build();

        memberService.update(updateDto);
        MemberSearchResDto updatedMall = memberService.findByMallNoAndId(mallNo, memberId);
        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updatedMall));
    }

    @Test
    @DisplayName("회원정보 수정 - 몰 정보가 없는 경우")
    void 회원정보_수정_몰_정보없음() {
        Long mallNo = 100L;
        String memberId = "custom001";

        MemberUpdateReqDto updateDto = MemberUpdateReqDto
                .builder()
                .id(memberId)
                .mallNo(mallNo)
                .phNumber("010-9999-9999")
                .birthdate(LocalDate.of(2000, 10, 10))
                .build();

        ApiResourceNotFoundException exception = Assertions.assertThrows(ApiResourceNotFoundException.class, () -> {
            memberService.update(updateDto);
        }, "몰 정보 없음");
        String message = exception.getMessage();
        log.info("message:{}", message);
        Assertions.assertEquals("Mall not exist.", message);
    }

    @Test
    @DisplayName("회원정보 수정 - 회원 정보가 없는 경우")
    void 회원정보_수정_회원_정보없음() {
        Long mallNo = 1L;
        String memberId = "custom100";

        MemberUpdateReqDto updateDto = MemberUpdateReqDto
                .builder()
                .id(memberId)
                .mallNo(mallNo)
                .phNumber("010-9999-9999")
                .birthdate(LocalDate.of(2000, 10, 10))
                .build();

        ApiResourceNotFoundException exception = Assertions.assertThrows(ApiResourceNotFoundException.class, () -> {
            memberService.update(updateDto);
        }, "회원 정보 없음");
        String message = exception.getMessage();
        log.info("message:{}", message);
        Assertions.assertEquals("Member not exist.", message);
    }

    @Test
    @DisplayName("회원 정보 삭제")
    void 회원정보_삭제() {

    }
}