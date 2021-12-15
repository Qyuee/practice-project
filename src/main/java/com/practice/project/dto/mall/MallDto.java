package com.practice.project.dto.mall;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
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
    public static class MallCreateReqDto {
        // required
        @NotNull
        private Long adminNo;
        @NotNull
        private String mallName;
        @NotNull
        private Country country;
        private Address address;
        @JsonIgnore
        private Admin admin;

        public static MallCreateReqDto of(Mall mall) {
            return modelMapper.map(mall, MallCreateReqDto.class);
        }

        public static Mall toEntity(MallCreateReqDto createRequest) {
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
    public static class MallResDto {
        @NotNull
        private Long mallNo;
        private Long adminNo;
        @NotNull
        private String mallName;
        @NotNull
        private Country country;
        private Address address;

        public static MallResDto of(Mall mall) {
            return modelMapper.map(mall, MallResDto.class);
        }

        public static Mall toEntity(MallResDto mallResDto) {
            return Mall.builder()
                    .no(mallResDto.getMallNo())
                    .name(mallResDto.getMallName())
                    .countryType(mallResDto.getCountry())
                    .address(mallResDto.getAddress())
                    .build();
        }
    }
}
