package com.practice.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import com.practice.project.utils.ModelMapperUtils;
import lombok.*;

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
        @NotBlank
        private String mallName;

        @NotBlank
        private Country countryType;

        private Address address;

        @JsonIgnore
        private String adminId;

        @JsonIgnore
        private Admin admin;

        public static MallCreateReqDto toDto(Mall mall) {
            return ModelMapperUtils.getModelMapper().map(mall, MallCreateReqDto.class);
        }

        public static Mall toEntity(MallCreateReqDto dto) {
            return Mall.builder()
                    .name(dto.getMallName())
                    .admin(dto.getAdmin())
                    .countryType(dto.getCountryType())
                    .build();
        }
    }

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MallUpdateReqDto {
        @NotBlank
        private Long mallNo;

        @NotBlank
        private String mallName;
        private Address address;

        @JsonIgnore
        private Admin admin;

        // Dto -> entity : toEntity
        public static Mall toEntity(MallUpdateReqDto dto) {
            return Mall.builder()
                    .no(dto.getMallNo())
                    .admin(dto.getAdmin())
                    .name(dto.getMallName())
                    .address(dto.getAddress())
                    .build();
        }
    }

    /**
     * Mall Common Response Dto
     */
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MallResDto {
        private Long mallNo;
        private Long adminNo;
        private String mallName;
        private Country countryType;
        private Address address;

        public static MallResDto toDto(Mall mall) {
            return ModelMapperUtils.getModelMapper().map(mall, MallResDto.class);
        }
    }
}
