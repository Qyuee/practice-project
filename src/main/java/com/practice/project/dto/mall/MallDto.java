package com.practice.project.dto.mall;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.dto.DtoImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@Component
@RequiredArgsConstructor
public class MallDto {
    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * Create 요청 DTO
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MallCreateReq {
        // required
        @NotNull
        @ApiModelProperty(position = 1, notes = "운영자 고유번호", example = "1")
        private Long adminNo;

        @NotNull
        @ApiModelProperty(position = 2, notes = "몰 이름", example = "테스트 쇼핑몰")
        private String mallName;

        @NotNull
        @ApiModelProperty(position = 3, notes = "지원 국가", example = "KR")
        private Country country;

        @ApiModelProperty(position = 4, notes = "사업장 주소정보")
        private Address address;

        @JsonIgnore
        private Admin admin;

        public static MallCreateReq of(Mall mall) {
            return modelMapper.map(mall, MallCreateReq.class);
        }

        public static Mall toEntity(MallCreateReq createRequest) {
            return Mall.builder()
                    .admin(createRequest.getAdmin())
                    .name(createRequest.getMallName())
                    .countryType(createRequest.getCountry())
                    .build();
        }
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        @NotNull
        @ApiModelProperty(position = 1, notes = "몰 고유번호", example = "1")
        private Long mallNo;

        @NotNull
        @ApiModelProperty(position = 2, notes = "몰 이름", example = "테스트 쇼핑몰")
        private String mallName;

        @NotNull
        @ApiModelProperty(position = 3, notes = "지원 국가", example = "KR")
        private Country country;

        @ApiModelProperty(position = 4, notes = "사업장 주소정보")
        private Address address;

        public static Response of(Mall mall) {
            return modelMapper.map(mall, Response.class);
        }

        public static Mall toEntity(Response response) {
            return Mall.builder()
                    .no(response.getMallNo())
                    .name(response.getMallName())
                    .countryType(response.getCountry())
                    .address(response.getAddress())
                    .build();
        }
    }
}
