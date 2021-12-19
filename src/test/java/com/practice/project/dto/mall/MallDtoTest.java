package com.practice.project.dto.mall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.MallDto.MallCreateReqDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.practice.project.dto.MallDto.MallCreateReqDto.*;

@Slf4j
class MallDtoTest {
    @Test
    @DisplayName("Mall entity -> dto 테스트")
    void 몰_entity_to_dto() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Admin admin = Admin.builder()
                .no(1L)
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();

        Address address = Address.builder()
                .country(Country.KR)
                .city("seoul")
                .zipcode("10000")
                .build();

        Mall mall = Mall.builder()
                .no(3L)
                .name("테스트몰")
                .admin(admin)
                .countryType(Country.KR)
                .address(address)
                .build();

        MallCreateReqDto test = of(mall);
        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(test));
    }

    @Test
    @DisplayName("Mall dto -> entity 테스트")
    void 몰_dto_to_entity() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Admin admin = Admin.builder()
                .no(1L)
                .id("lee33398")
                .name("이동석")
                .email("lee33398@naver.com")
                .build();

        MallCreateReqDto mallDto = builder()
                .admin(admin)
                .mallName("테스트몰")
                .build();

        log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(toEntity(mallDto)));

    }
}