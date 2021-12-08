package com.practice.project.dto.admin;

import com.practice.project.domain.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUpdateResponse {
    private Long no;
    private String id;

    public AdminUpdateResponse(Admin admin) {
        this.no = admin.getNo();
        this.id = admin.getId();
    }
}
