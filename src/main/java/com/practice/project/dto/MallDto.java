package com.practice.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.utils.ModelMapperUtils;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class MallDto {
    /**
     * Mall Create Request Dto
     */
    @Data
    @Builder
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MallCreateReqDto {
        // required
        @NotBlank
        private String mallName;

        @NotBlank
        private Country countryType;

        private Address address;

        @JsonIgnore
        private String adminId;

        @JsonIgnore
        private Admin admin;

        public static MallCreateReqDto of(Mall mall) {
            return ModelMapperUtils.getModelMapper().map(mall, MallCreateReqDto.class);
        }

        public static Mall toEntity(MallCreateReqDto createRequest) {
            return Mall.builder()
                    .name(createRequest.getMallName())
                    .admin(createRequest.getAdmin())
                    .countryType(createRequest.getCountryType())
                    .build();
        }
    }

    /**
     * Mall Common Response Dto
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
        private Country countryType;

        private Address address;

        public static MallResDto of(Mall mall) {
            return ModelMapperUtils.getModelMapper().map(mall, MallResDto.class);
        }

        public static Mall toEntity(MallResDto mallResDto) {
            return Mall.builder()
                    .no(mallResDto.getMallNo())
                    .name(mallResDto.getMallName())
                    .countryType(mallResDto.getCountryType())
                    .address(mallResDto.getAddress())
                    .build();
        }
    }
}
