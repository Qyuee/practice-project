package com.practice.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.practice.project.domain.Mall;
import com.practice.project.domain.Member;
import com.practice.project.domain.common.Gender;
import com.practice.project.utils.ModelMapperUtils;
import lombok.*;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

//@Todo 제네릭을 활용하여 중복되는 toEntity, toDto 통합처리
/**
 * 정적 중첩 클래스를 사용하여 외부에서 바로 접근하여 사용 할 수 있도록 작성
 */
@Data
@RequiredArgsConstructor
public class MemberDto {
    /**
     * Create Request Dto
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class MemberCreateReqDto {
        private Mall mall;
        private Long mallNo;
        private String id;
        private String name;
        private String email;
        private String phNumber;
        private Gender gender;
        private LocalDate birthdate;

        public static Member toEntity(MemberCreateReqDto dto) {
            return Member.builder()
                    .mall(dto.getMall())
                    .id(dto.getId())
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .phNumber(dto.getPhNumber())
                    .gender(dto.getGender())
                    .birthdate(dto.getBirthdate())
                    .build();
        }
    }

    /**
     * Create Response Dto
     * - Entity -> dto의 경우만 필요
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class MemberCreateResDto {
        // 몰NO, 회원ID, 이름, 이메일, 휴대번호, 성별, 생년월일
        private Long mallNo;
        private String Id;
        private String name;
        private String email;
        private String phNumber;
        private Gender gender;
        private LocalDate birthdate;

        public static MemberCreateResDto toDto(Member member) {
            return ModelMapperUtils.getModelMapper().map(member, MemberCreateResDto.class);
        }
    }

    /**
     * Update Request Dto
     */
    @Getter
    @Builder
    public static class MemberUpdateReqDto {
        private Long mallNo;
        private String id;
        private String phNumber;
        private LocalDate birthdate;
    }

    /**
     * Update Response Dto
     */
    public static class MemberUpdateResDto {
        private Long mallNo;
        private String Id;
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
     * 검색 결과 응답 Dto
     */
    @Data
    @Builder
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MemberSearchResDto {
        private Long mallNo;
        private String Id;
        private String name;
        private String email;
        private String phNumber;
        private Gender gender;
        private LocalDate birthdate;

        public static MemberSearchResDto toDto(Member member) {
            return ModelMapperUtils.getModelMapper().map(member, MemberSearchResDto.class);
        }
    }

    /**
     * 회원 최소정보 응답 Dto
     */
    @Builder
    public static class MemberBasicResDto {
        private Long mallNo;
        private String id;
    }
}
