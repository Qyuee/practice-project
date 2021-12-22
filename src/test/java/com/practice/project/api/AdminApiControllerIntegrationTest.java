package com.practice.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.AdminDto.AdminCreateReqDto;
import com.practice.project.dto.AdminDto.AdminUpdateReqDto;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminApiControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/admins")
    void POST_운영자_등록() throws Exception {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .id("testAdmin001")
                .name("테스트어드민001")
                .email("testAdmin001@naver.com")
                .phNumber("010-0000-1234")
                .build();

        mockMvc.perform(post("/api/admins")
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())    // 201
                .andExpect(jsonPath("$['data'].no").exists())
                .andExpect(jsonPath("$['data'].id").value("testAdmin001"));
    }

    @Test
    @DisplayName("POST /api/admins 운영자 등록 예외 테스트 - ID or Email 중복")
    void 운영자_등록_이름_이메일_중복() throws Exception {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .id("lee33397")     // 이미 존재하는 운영자ID
                .name("테스트어드민001")
                .email("testAdmin001@naver.com")
                .phNumber("010-0000-1234")
                .build();

        mockMvc.perform(post("/api/admins")
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiResourceConflictException));
    }

    @Test
    @DisplayName("POST /api/admins 운영자 등록 예외 테스트 - 필수값 누락")
    void 운영자_둥록_필수값_누락() throws Exception {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .name("테스트어드민001")
                .email("testAdmin001@naver.com")
                .phNumber("010-0000-1234")
                .build();

        mockMvc.perform(post("/api/admins")
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    @DisplayName("PUT /api/admins/{no}")
    void PUT_운영자정보_수정() throws Exception {
        AdminUpdateReqDto reqDto = AdminUpdateReqDto.builder()
                .address(Address.builder()
                        .country(Country.CH)
                        .city("베이징")
                        .build())
                .phNumber("010-1111-1111")
                .build();

        mockMvc.perform(put("/api/admins/{no}", "1")
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['data']['address']['country']").value(Country.CH.name()));
    }

    @Test
    @DisplayName("GET /api/admins, +pageable")
    void GET_운영자_리스트() throws Exception {
        mockMvc.perform(get("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$['data']").exists())      // 'data' 요소가 존재하는지?
                .andExpect(jsonPath("$['data'][0]['id']").value("lee33397"))     // 동일성 비교: data.0.id가 lee33397인지?
                .andExpect(jsonPath("$..address[?(@.detail_address == '%s')]", "강서구").exists()) // data[N].address.detail_address에 '강서구' 값이 존재하는가?
                .andExpect(jsonPath("$..address[?(@.detail_address == '%s')]", "중구").doesNotExist())  // data[N].address.detail_address에 '동대문구' 값이 존재하지 않는가?
                .andExpect(jsonPath("$['data'][0]['id']", startsWith("lee")))   // data.0.id가 "lee"로 시작하는가?
                .andExpect(jsonPath("$['data'][%s].name", 0).value("이동석"))  // data.0.name이 '이동석'인가?
                .andExpect(jsonPath("$..['id']").exists());     // 'id' 요소가 결과에 존재하는지?
        //.andExpect(jsonPath("$['data'][1].id", "lee33398").exists());
    }

    @Test
    @DisplayName("GET /api/admins/{id}")
    void GET_운영자_조회_by_id() throws Exception {
        mockMvc.perform(get("/api/admins/{id}", "lee33397")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(result -> {
                    String responseBody = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result.getResponse().getContentAsString());
                    log.info(responseBody);
                })
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$['data'].id", "lee33397").exists());
    }

    @Test
    @DisplayName("DELETE /api/admins/{no}")
    void DELETE_운영자() throws Exception {
        Long no = 1L;
        mockMvc.perform(delete("/api/admins/{no}", no)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['data']['no']").value(no));
    }

    @Test
    @DisplayName("DELETE /api/admins/{no} - 유효하지않은 값")
    void DELETE_운영자_유효하지않는_경우() throws Exception {
        mockMvc.perform(delete("/api/admins/{no}", "s")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }
}