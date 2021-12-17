package com.practice.project.dto.admin;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.practice.project.domain.common.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminUpdateReqDto {
    // <optional>
    @ApiModelProperty(position = 1, notes = "휴대전화번호", example = "010-0000-0000")
    private String phNumber;

    @ApiModelProperty(position = 2, notes = "주소정보")
    private Address address;
}
