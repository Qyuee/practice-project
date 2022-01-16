package com.practice.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.practice.project.domain.Admin;
import com.practice.project.domain.common.Address;
import com.practice.project.utils.ModelMapperUtils;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
public class AdminDto {

    /**
     * Admin Create Request Dto
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class AdminCreateReqDto {
        @NotBlank(message = "Admin's id required")
        private String adminId;

        @NotBlank(message = "Admin's name required")
        private String name;

        @NotBlank(message = "Admin's email required")
        @Email(message = "It's wrong admin's email format.")
        private String email;

        private String phNumber;
        private Address address;

        public static Admin toEntity(AdminCreateReqDto dto) {
            return Admin.builder()
                    .adminId(dto.getAdminId())
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .phNumber(dto.getPhNumber())
                    .address(dto.getAddress())
                    .build();
        }
    }

    /**
     * Admin Update Request Dto
     */
    @Getter
    @Builder
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class AdminUpdateReqDto {
        private String phNumber;
        private Address address;
    }

    /**
     * Admin Create/Update Response Dto
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class AdminResDto {
        @JsonIgnore
        private Long no;
        private String adminId;
        private String name;
        private String email;
        private Address address;

        public static AdminResDto toDto(Admin admin) {
            return ModelMapperUtils.getModelMapper().map(admin, AdminResDto.class);
        }
    }

    /**
     * Simple Admin Response Dto
     */
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AdminSimpleResDto {
        @JsonIgnore
        private Long no;
        private String adminId;

        public static AdminSimpleResDto toDto(Admin admin) {
            return ModelMapperUtils.getModelMapper().map(admin, AdminSimpleResDto.class);
        }
    }
}
