package com.practice.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.MallDto.MallCreateReqDto;
import com.practice.project.repository.AdminRepository;
import com.practice.project.repository.MallRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MallApiControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    MallRepository mallRepository;

    @BeforeAll
    @Transactional
    @Rollback(value = false)
    void 테스트_데이터_설정() {
        for (int i = 0; i < 10; i++) {
            Admin admin = Admin.builder()
                    .id("lee3339"+i)
                    .name("이동석")
                    .email("lee3339"+i+"@naver.com")
                    .build();
            adminRepository.save(admin);

            for (int j = 0; j < 10; j++) {
                mallRepository.save(Mall.builder()
                        .name(i+"번 admin_"+j+"번_테스트몰")
                        .admin(admin)
                        .countryType(Country.KR)
                        .build()
                );
            }
        }
    }

    @Test
    @DisplayName("POST /api/admin/{id}/malls")
    void POST_특정_운영자_몰() throws Exception {
        Admin admin = Admin.builder()
                .id("testAdmin")
                .email("testAdmin@naver.com")
                .name("테스터")
                .build();
        adminRepository.save(admin);

        MallCreateReqDto request = MallCreateReqDto.builder()
                .mallName("테스트몰")
                .countryType(Country.JP)
                .build();

        String body = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/admin/{id}/malls", admin.getId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("GET /api/malls")
    void GET_몰_리스트() throws Exception {
        mockMvc.perform(get("/api/malls")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andDo(result -> {
                    String responseBody = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result.getResponse().getContentAsString());
                    log.info(responseBody);
                })
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.['data']", hasSize(5)));
    }

    @Test
    @DisplayName("GET /api/admin/{id}/malls")
    void GET_특정_운영자_몰_리스트() throws Exception {
        mockMvc.perform(get("/api/admin/{id}/malls", "lee33398")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(result -> {
                    String responseBody = objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(result.getResponse().getContentAsString());
                    log.info(responseBody);
                })
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.['data']", hasSize(10)));
    }
}