package com.practice.project.dto.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminCreateResponse {
    @ApiModelProperty(position = 1, example = "1")
    private Long no;

    @ApiModelProperty(position = 2, example = "lee33398")
    private String id;

    @ApiModelProperty(position = 3, example = "이동석")
    private String name;

    @ApiModelProperty(position = 4, example = "lee33398@naver.com")
    private String email;
}
