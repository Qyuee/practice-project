package com.practice.project.api;

import com.practice.project.domain.Admin;
import com.practice.project.dto.admin.*;
import com.practice.project.dto.common.Result;
import com.practice.project.service.AdminService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @PostMapping("/api/admins")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "운영자 가입", notes = "필수값: 아이디, 이메일, 이름")
    public AdminCreateResponse saveAdmin(@RequestBody AdminCreateRequest request) {
        Admin admin = Admin.builder()
                .id(request.getId())
                .email(request.getEmail())
                .name(request.getName())
                .phNumber(request.getPhNumber())
                .address(request.getAddress())
                .build();

        Long no = adminService.save(admin);
        Admin newAdmin = adminService.findOne(no);
        return new AdminCreateResponse(no, newAdmin.getId(), newAdmin.getName(), newAdmin.getEmail());
    }

    @PutMapping("/api/admins/{no}")
    //@ApiImplicitParam(name = "no", value = "운영자 고유번호") -> no를 문서상에서는 String 타입으로 보이게 함
    //@Todo 타입이 맞지않는 경우 발생하는 예외응답 처리 필요
    @ApiOperation(value = "운영자 정보 일부 수정", notes = "필수값: 운영자 고유번호")
    public AdminUpdateResponse updateAdmin(
            @PathVariable("no") Long no,
            @RequestBody AdminUpdateRequest request) {

        adminService.update(no, request);
        Admin admin = adminService.findOne(no);
        return new AdminUpdateResponse(admin);
    }

    @GetMapping("/api/admins")
    @ApiOperation(value = "운영자 목록 조회")
    public Result<List<AdminDto>> getAdmins(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "0") int limit) {
        List<Admin> findAdmins = adminService.findAdmins();
        List<AdminDto> adminList = findAdmins.stream()
                .map(AdminDto::new)
                .collect(Collectors.toList());
        return new Result<>(adminList.size(), adminList);
    }

    @GetMapping("/api/admins/{id}")
    @ApiOperation(value = "운영자 id 기반 조회")
    public Result<AdminDto> getAdminById(@PathVariable("id") String id) {
        Admin findAdmin = adminService.findById(id);
        return new Result(new AdminDto(findAdmin));
    }

    @GetMapping("/api/admins/no/{no}")
    @ApiOperation(value = "운영자 no 기반 조회")
    public Result<AdminDto> getAdminByNo(@PathVariable("no") Long no) {
        Admin findAdmin = adminService.findOne(no);
        return new Result(new AdminDto(findAdmin));
    }

    @DeleteMapping("/api/admins/{no}")
    @ApiOperation(value = "운영자 삭제")
    public AdminDto deleteAdmin(@PathVariable("no") Long no) {
        adminService.removeAdmin(no);
        return AdminDto.builder().no(no).build();
    }
}
