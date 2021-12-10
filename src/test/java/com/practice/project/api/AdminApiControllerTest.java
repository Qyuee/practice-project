package com.practice.project.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.Admin;
import com.practice.project.repository.AdminRepository;
import com.practice.project.service.AdminService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest는 @Controller, @ControllerAdvice와 같은 웹과 관련된 bean만 주입해준다.
// @Service와 같은 @Component는 주입되지 않는다.
@WebMvcTest(AdminApiController.class)

// 통합테스트의 경우에는 전체 컨텍스트를 로드하는 JPA를 포함한 모든 bean을 주입받기에 문제되지 않지만,
// @WebMvcTest와 같은 슬라이스 테스트는 JPA와 관련된 Bean을 로드하지 않는다. 설정파일을 분리하거나 직접 @Mockbean을 통해서 주입해준다.
@MockBean(JpaMetamodelMappingContext.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @BeforeAll
    void setup() {
        AdminApiController controller = new AdminApiController(adminService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("GET /api/admins (MockBean을 사용한 경우)")
    void GET_운영자_리스트() throws Exception {
        List<Admin> admins = new ArrayList<>();
        admins.add(Admin.builder()
                .no(1L)
                .id("lee33398")
                .name("이동석")
                .email("lee33398")
                .build());

        Pageable pageable = PageRequest.of(0, 5);

        given(adminService.findAdmins(pageable)).willReturn(admins);

        mockMvc.perform(
                get("/api/admins")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$['data'][0].id", "lee33398").exists());
                /*.andExpect(jsonPath("$.[?(@.id == '%s')]", "lee33398").exists());*/
    }
}