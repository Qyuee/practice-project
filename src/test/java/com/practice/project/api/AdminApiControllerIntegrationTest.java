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

    @BeforeAll
    @Transactional
    void 테스트_데이터_설정() {
        for (int i = 1; i < 30; i++) {
            adminRepository.save(Admin.builder()
                    .id("tester_"+i)
                    .name("테스트 운영자"+i)
                    .email("lee33398"+i+"@naver.com")
                    .build()
            );
        }
    }

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
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("page", String.valueOf(0))
                    .param("size", String.valueOf(5)))
                .andDo(print())
                .andDo(result -> {
                    String responseBody = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result.getResponse().getContentAsString());
                    log.info(responseBody);
                })
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$['data']", hasSize(5)))
                .andExpect(jsonPath("$['data'][0].id", "lee33398").exists());
    }

    @Test
    @DisplayName("GET /api/admins/id/{id}")
    void GET_운영자_조회_by_id() throws Exception {
        mockMvc.perform(get("/api/admins/id/tester_10")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(result -> {
                    String responseBody = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result.getResponse().getContentAsString());
                    log.info(responseBody);
                })
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$['data'].id", "tester_10").exists());
    }

    @Test
    @DisplayName("GET /api/admins/{no}")
    void GET_운영자_조회_by_no() throws Exception {
        mockMvc.perform(get("/api/admins/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(result -> {
                    String responseBody = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result.getResponse().getContentAsString());
                    log.info(responseBody);
                })
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$['data'].no", "10").exists());
    }
}