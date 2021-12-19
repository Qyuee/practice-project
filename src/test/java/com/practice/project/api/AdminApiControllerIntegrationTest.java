package com.practice.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Country;
import com.practice.project.repository.AdminRepository;
import com.practice.project.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private AdminRepository adminRepository;

    @Test
    @DisplayName("POST /api/admins")
    void POST_운영자_등록() throws Exception {
        //@Todo
    }

    @Test
    @DisplayName("PUT /api/admins/{no}")
    void PUT_운영자정보_수정() throws Exception {
        //@Todo
    }

    @Test
    @DisplayName("GET /api/admins, +pageable")
    void GET_운영자_리스트() throws Exception {
        mockMvc.perform(get("/api/admins")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("page", String.valueOf(0))
                    .param("size", String.valueOf(5)))
                .andDo(print())
                .andDo(result -> {
                    String responseBody = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result.getResponse().getContentAsString());
                    log.info(responseBody);
                })
                .andExpect(status().is2xxSuccessful());
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
}