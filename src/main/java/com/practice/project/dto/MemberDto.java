package com.practice.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.practice.project.domain.Mall;
import com.practice.project.domain.Member;
import com.practice.project.domain.common.Gender;
import com.practice.project.domain.statusinfo.MemberStatus;
import com.practice.project.utils.ModelMapperUtils;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

/**
 * 정적 중첩 클래스를 사용하여 외부에서 바로 접근하여 사용 할 수 있도록 작성
 */

//@Todo 제네릭을 활용하여 중복되는 toEntity, toDto 통합처리
@NoArgsConstructor(access = PROTECTED)
public class MemberDto {
    /**
     * Member Create Request Dto
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class MemberCreateReqDto {
        @NotBlank(message = "member_id is required.")
        private String memberId;

        @NotBlank(message = "name is required.")
        private String name;

        @NotBlank(message = "email is required.")
        private String email;

        private String phNumber;
        private Gender gender;
        private LocalDate birthdate;

        @JsonIgnore
        private Long mallNo;

        @JsonIgnore
        private Mall mall;

        public static Member toEntity(MemberCreateReqDto dto) {
            return Member.builder()
                    .mall(dto.getMall())
                    .memberId(dto.getMemberId())
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .phNumber(dto.getPhNumber())
                    .gender(dto.getGender())
                    .birthdate(dto.getBirthdate())
                    .build();
        }
    }

    /**
     * Member Create Response Dto
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class MemberCreateResDto {
        private Long mallNo;
        private String memberId;
        private String name;
        private String email;
        private String phNumber;
        private Gender gender;
        private LocalDate birthdate;
        private MemberStatus status;

        public static MemberCreateResDto toDto(Member member) {
            return ModelMapperUtils.getModelMapper().map(member, MemberCreateResDto.class);
        }
    }

    /**
     * Member Update Request Dto
     */
    @Getter
    @Builder
    public static class MemberUpdateReqDto {
        private Long mallNo;
        private String memberId;
        private String phNumber;
        private LocalDate birthdate;
    }

    /**
     * Member Update Response Dto
     */
    public static class MemberUpdateResDto {
        private Long mallNo;
        private String memberId;
        private String name;
        private String email;
        private String phNumber;
        private Gender gender;
        private LocalDate birthdate;

        public static MemberUpdateResDto toDto(Member member) {
            return ModelMapperUtils.getModelMapper().map(member, MemberUpdateResDto.class);
        }
    }

    /**
     * Member Search Response Dto
     */
    @Data
    @Builder
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MemberSearchResDto {
        private Long mallNo;
        private String memberId;
        private String name;
        private String email;
        private String phNumber;
        private Gender gender;
        private LocalDate birthdate;
        private MemberStatus status;

        public static MemberSearchResDto toDto(Member member) {
            return ModelMapperUtils.getModelMapper().map(member, MemberSearchResDto.class);
        }
    }

    /**
     * Member Simple Response Dto
     */
    @Builder
    public static class MemberSimpleResDto {
        private Long mallNo;
        private String memberId;
    }
}
