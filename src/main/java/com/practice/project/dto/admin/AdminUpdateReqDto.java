package com.practice.project.dto.admin;

import com.practice.project.domain.common.Address;
import com.practice.project.domain.common.Country;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateReqDto {
    // <optional>
    @ApiModelProperty(position = 1, notes = "휴대전화번호", example = "010-0000-0000")
    private String phNumber;

    @ApiModelProperty(position = 2, notes = "주소정보")
    private Address address;
}
