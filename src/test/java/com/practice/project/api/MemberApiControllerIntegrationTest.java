package com.practice.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.dto.MemberDto.MemberCreateReqDto;
import com.practice.project.dto.MemberDto.MemberSearchResDto;
import com.practice.project.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("mysql")
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberApiControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("POST /api/malls/{mall_no}/members")
    @Rollback(value = false)
    void 쇼핑몰_회원생성() throws Exception {
        // given
        Long mallNo = 1L;
        MemberCreateReqDto reqDto = MemberCreateReqDto.builder()
                .memberId("testMember001")
                .name("회원1")
                .email("회원1@naver.com")
                .build();

        // then
        mockMvc.perform(post("/api/malls/{mall_no}/members", mallNo)
                        .content(objectMapper.writeValueAsString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['data'].member_id").value(reqDto.getMemberId()));

        MemberSearchResDto test = memberService.findByMallNoAndMemberId(mallNo, reqDto.getMemberId());
        log.info("result:{}", test.toString());
    }

    @Test
    @Order(2)
    void 쇼핑몰_회원_조회() throws Exception {
        mockMvc.perform(get("/api/malls/{mall_no}/members/{member_id}", 1L, "testMember001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}