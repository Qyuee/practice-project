package com.practice.project.api;

import com.practice.project.dto.AdminDto.AdminCreateReqDto;
import com.practice.project.dto.AdminDto.AdminResDto;
import com.practice.project.dto.AdminDto.AdminSimpleResDto;
import com.practice.project.dto.AdminDto.AdminUpdateReqDto;
import com.practice.project.dto.common.Result;
import com.practice.project.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class AdminApiController {
    private final AdminService adminService;

    /**
     * dto를 별도로 생성하여 외부에서 요청받은 값을 엔티티에 바로 매핑하지 않도록 한다.
     * - 엔티티와 api 요청/응답을 1:1로 매핑하면 api의 스펙이 변경되는 경우 엔티티를 변경해야 한다.
     * - 반대로 엔티티가 바뀐다고 api 스펙이 변경되는 것도 문제이다.
     * - 필요에따라 api스펙이 추가/수정 될 때, dto를 별도로 생성하여 대응하는 것이 좋다. (회원가입 방식이 여러개인 것 처럼)
     */
    @PostMapping(value = "/admins")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "운영자 가입", notes = "필수값: 아이디, 이메일, 이름")
    public Result<AdminResDto> saveAdmin(@Valid @RequestBody AdminCreateReqDto reqDto) {
        AdminResDto save = adminService.save(reqDto);
        return new Result<>(save);
    }

    @GetMapping(value = "/admins")
    @ApiOperation(value = "운영자 목록 조회")
    public Result<List<AdminResDto>> getAdmins(
            Pageable pageable) {
        List<AdminResDto> resDtoList = adminService.findAdmins(pageable);
        return new Result<>(resDtoList.size(), resDtoList);
    }

    @GetMapping(value = "/admins/{admin_id}")
    @ApiOperation(value = "운영자 id 기반 조회")
    public Result<AdminResDto> getAdminById(@PathVariable("admin_id") @NotBlank String adminId) {
        AdminResDto resDto = adminService.findByAdminId(adminId);
        return new Result<>(resDto);
    }

    @PutMapping(value = "/admins/{admin_id}")
    //@Todo 타입이 맞지않는 경우 발생하는 예외응답 처리 필요
    @ApiOperation(value = "운영자 정보 일부 수정", notes = "필수값: 운영자 고유번호")
    public Result<AdminResDto> updateAdmin(
            @PathVariable("admin_id") @NotNull String adminId,
            @RequestBody AdminUpdateReqDto reqDto) {
        AdminResDto resDto = adminService.update(adminId, reqDto);
        return new Result<>(resDto);
    }

    @DeleteMapping("/admins/{admin_id}")
    @ApiOperation(value = "운영자 삭제")
    public Result<AdminSimpleResDto> deleteAdmin(@PathVariable("admin_id") @NotNull String adminId) {
        AdminSimpleResDto resDto = adminService.removeByAdminId(adminId);
        return new Result<>(resDto);
    }
}
