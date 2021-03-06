package com.practice.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.AdminDto.AdminCreateReqDto;
import com.practice.project.dto.AdminDto.AdminUpdateReqDto;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("mysql")
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
    void POST_?????????_??????() throws Exception {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .adminId("testAdmin001")
                .name("??????????????????001")
                .email("testAdmin001@naver.com")
                .phNumber("010-0000-1234")
                .build();

        mockMvc.perform(post("/api/admins")
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())    // 201
                //.andExpect(jsonPath("$['data'].no").exists())
                .andExpect(jsonPath("$['data'].admin_id").value("testAdmin001"));
    }

    @Test
    @DisplayName("POST /api/admins ????????? ?????? ?????? ????????? - ID or Email ??????")
    void ?????????_??????_??????_?????????_??????() throws Exception {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .adminId("lee33397")     // ?????? ???????????? ?????????ID
                .name("??????????????????001")
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
    @DisplayName("POST /api/admins ????????? ?????? ?????? ????????? - ????????? ??????")
    void ?????????_??????_?????????_??????() throws Exception {
        AdminCreateReqDto reqDto = AdminCreateReqDto.builder()
                .name("??????????????????001")
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
    @DisplayName("PUT /api/admins/{admin_id}")
    void PUT_???????????????_??????() throws Exception {
        String adminId = "lee33398";
        AdminUpdateReqDto reqDto = AdminUpdateReqDto.builder()
                .address(Address.builder()
                        .country(Country.CH)
                        .city("?????????")
                        .build())
                .phNumber("010-1111-1111")
                .build();

        mockMvc.perform(put("/api/admins/{admin_id}", adminId)
                        .content(objectMapper.writeValueAsString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['data']['address']['country']").value(Country.CH.name()));
    }

    @Test
    @DisplayName("GET /api/admins, +pageable")
    void GET_?????????_?????????() throws Exception {
        mockMvc.perform(get("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$['data']").exists())      // 'data' ????????? ????????????????
                .andExpect(jsonPath("$['data'][0]['admin_id']").value("lee33397"))     // ????????? ??????: data.0.id??? lee33397???????
                .andExpect(jsonPath("$..address[?(@.detail_address == '%s')]", "?????????").exists()) // data[N].address.detail_address??? '?????????' ?????? ????????????????
                .andExpect(jsonPath("$..address[?(@.detail_address == '%s')]", "??????").doesNotExist())  // data[N].address.detail_address??? '????????????' ?????? ???????????? ??????????
                .andExpect(jsonPath("$['data'][0]['admin_id']", startsWith("lee")))   // data.0.id??? "lee"??? ????????????????
                .andExpect(jsonPath("$['data'][%s].name", 0).value("?????????"))  // data.0.name??? '?????????'???????
                .andExpect(jsonPath("$..['admin_id']").exists());     // 'id' ????????? ????????? ????????????????
        //.andExpect(jsonPath("$['data'][1].id", "lee33398").exists());
    }

    @Test
    @DisplayName("GET /api/admins/{admin_id}")
    void GET_?????????_??????_by_id() throws Exception {
        mockMvc.perform(get("/api/admins/{admin_id}", "lee33397")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(result -> {
                    String responseBody = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result.getResponse().getContentAsString());
                    log.info(responseBody);
                })
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$['data'].admin_id", "lee33397").exists());
    }

    @Test
    @DisplayName("DELETE /api/admins/{admin_id}")
    void DELETE_?????????() throws Exception {
        String adminId = "lee33398";
        mockMvc.perform(delete("/api/admins/{admin_id}", adminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['data'].admin_id").value(adminId));
    }

    @Test
    @DisplayName("DELETE /api/admins/{admin_id} - ?????????????????? ???")
    void DELETE_?????????_??????????????????_??????() throws Exception {
        mockMvc.perform(delete("/api/admins/{admin_id}", "s")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiResourceNotFoundException));
    }
}