package com.practice.project.api;

import com.practice.project.domain.Admin;
import com.practice.project.dto.admin.*;
import com.practice.project.dto.common.Result;
import com.practice.project.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
public class AdminApiController {
    private final AdminService adminService;

    /**
     * dto를 별도로 생성하여 외부에서 요청받은 값을 엔티티에 바로 매핑하지 않도록 한다.
     * - 엔티티와 api 요청/응답을 1:1로 매핑하면 api의 스펙이 변경되는 경우 엔티티를 변경해야 한다.
     * - 반대로 엔티티가 바뀐다고 api 스펙이 변경되는 것도 문제이다.
     * - 필요에따라 api스펙이 추가/수정 될 때, dto를 별도로 생성하여 대응하는 것이 좋다. (회원가입 방식이 여러개인 것 처럼)
     */
    @PostMapping(value = "/api/admins", produces = "application/json; charset=UTF-8")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "운영자 가입", notes = "필수값: 아이디, 이메일, 이름")
    public AdminCreateResDto saveAdmin(@RequestBody AdminCreateReqDto request) {
        Admin admin = Admin.builder()
                .id(request.getId())
                .email(request.getEmail())
                .name(request.getName())
                .phNumber(request.getPhNumber())
                .address(request.getAddress())
                .build();

        Long no = adminService.save(admin);
        Admin newAdmin = adminService.findOne(no);
        return new AdminCreateResDto(no, newAdmin.getId(), newAdmin.getName(), newAdmin.getEmail());
    }

    @GetMapping(value = "/api/admins", produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "운영자 목록 조회")
    public Result<List<AdminDto>> getAdmins(
            Pageable pageable) {
        List<Admin> findAdmins = adminService.findAdmins(pageable);
        List<AdminDto> adminList = findAdmins.stream()
                .map(AdminDto::new)
                .collect(Collectors.toList());
        return new Result<>(adminList.size(), adminList);
    }

    @GetMapping(value = "/api/admins/{id}", produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "운영자 id 기반 조회")
    public Result<AdminDto> getAdminById(@PathVariable("id") String id) {
        Admin findAdmin = adminService.findById(id);
        return new Result<>(new AdminDto(findAdmin));
    }

    @PutMapping("/api/admins/{no}")
    //@ApiImplicitParam(name = "no", value = "운영자 고유번호") -> no를 문서상에서는 String 타입으로 보이게 함
    //@Todo 타입이 맞지않는 경우 발생하는 예외응답 처리 필요
    @ApiOperation(value = "운영자 정보 일부 수정", notes = "필수값: 운영자 고유번호")
    public AdminUpdateResDto updateAdmin(
            @PathVariable("no") Long no,
            @RequestBody AdminUpdateReqDto request) {

        adminService.update(no, request);
        Admin admin = adminService.findOne(no);
        return new AdminUpdateResDto(admin);
    }

    @DeleteMapping("/api/admins/{no}")
    @ApiOperation(value = "운영자 삭제")
    public AdminDto deleteAdmin(@PathVariable("no") Long no) {
        adminService.removeAdmin(no);
        return AdminDto.builder().no(no).build();
    }
}
