package com.practice.project.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.practice.project.domain.Admin;
import com.practice.project.domain.common.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class AdminDto {

    @ApiModelProperty(position = 0, example = "1")
    private Long no;

    @ApiModelProperty(position = 1, example = "lee33398")
    private String id;

    @ApiModelProperty(position = 2, example = "이동석")
    private String name;

    @ApiModelProperty(position = 3, example = "lee33398@naver.com")
    private String email;

    @ApiModelProperty(position = 4, example = "010-0000-0000")
    private String phNumber;

    @ApiModelProperty(position = 5)
    private Address address;

    @ApiModelProperty(position = 6)
    private LocalDateTime createdDate;

    @ApiModelProperty(position = 7)
    private LocalDateTime lastModifiedDate;

    public AdminDto(Admin admin) {
        this.no = admin.getNo();
        this.id = admin.getId();
        this.name = admin.getName();
        this.email = admin.getEmail();
        this.phNumber = admin.getPhNumber();
        this.address = admin.getAddress();
        this.createdDate = admin.getCreatedDate();
        this.lastModifiedDate = admin.getLastModifiedDate();
    }
}
