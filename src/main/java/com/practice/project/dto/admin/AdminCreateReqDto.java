package com.practice.project.dto.admin;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminCreateReqDto {
    // <required>
    @NotBlank(message = "Admin's id is required")
    @ApiModelProperty(position = 1, notes = "운영자 아이디", example = "lee33398")
    private String id;

    @NotBlank(message = "Admin's name is required")
    @ApiModelProperty(position = 2, notes = "운영자 이름", example = "이동석")
    private String name;

    @NotBlank(message = "Admin's email is required")
    @Email(message = "It's wrong admin's email format.")
    @ApiModelProperty(position = 3, notes = "운영자 이메일", example = "lee33398@naver.com")
    private String email;

    // <optional>
    @ApiModelProperty(position = 5, notes = "휴대전화번호", example = "010-0000-0000")
    private String phNumber;

    @ApiModelProperty(position = 6, notes = "주소정보")
    private Address address;
}
