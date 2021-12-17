package com.practice.project.dto.admin;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.practice.project.domain.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminUpdateResDto {
    private Long no;
    private String id;

    public AdminUpdateResDto(Admin admin) {
        this.no = admin.getNo();
        this.id = admin.getId();
    }
}
