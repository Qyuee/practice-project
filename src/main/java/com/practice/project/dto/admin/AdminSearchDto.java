package com.practice.project.dto.admin;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminSearchDto {
    @ApiModelProperty(position = 0, example = "1")
    private Long no;

    @ApiModelProperty(position = 1, example = "lee33398")
    private String id;

    @ApiModelProperty(position = 2, example = "이동석")
    private String name;

    @ApiModelProperty(position = 3, example = "lee33398@naver.com")
    private String email;
}
