package com.practice.project.api;

import com.practice.project.domain.statusinfo.MallStatus;
import com.practice.project.dto.MallDto.MallCreateReqDto;
import com.practice.project.dto.MallDto.MallResDto;
import com.practice.project.dto.MallDto.MallStatusResDto;
import com.practice.project.dto.MallDto.MallUpdateReqDto;
import com.practice.project.dto.common.ApiResult;
import com.practice.project.service.MallService;
import com.practice.project.validator.AllowedSortProperties;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class MallApiController {
    private final MallService mallService;

    @GetMapping(value = "/malls")
    @ApiOperation(value = "몰 리스트 조회")
    public ApiResult<List<MallResDto>> getMalls(
            @AllowedSortProperties({"name", "countryType", "createdDate"})
            @PageableDefault(size=5, sort="name", direction = Sort.Direction.ASC) Pageable pageable) {
        List<MallResDto> mallList = mallService.findAll(pageable);
        return new ApiResult<>(mallList.size(), mallList);
    }

    @GetMapping(value = "/admin/{id}/malls/{no}")
    @ApiOperation(value = "특정 몰 정보 조회")
    public ApiResult<MallResDto> getMallInfo(
            @PathVariable("id") @NotBlank String id,
            @PathVariable("no") @NotBlank Long no) {
        MallResDto mallResDto = mallService.findByNo(id, no);
        return new ApiResult<>(mallResDto);
    }

    @GetMapping(value = "/admin/{id}/malls")
    @ApiOperation(value = "운영자의 몰 리스트")
    public ApiResult<List<MallResDto>> getAdminMalls(@PathVariable("id") @NotBlank String id) {
        List<MallResDto> mallList = mallService.findByAdminId(id);
        return new ApiResult<>(mallList.size(), mallList);
    }

    @PostMapping(value = "/admin/{id}/malls")
    @ApiOperation(value = "특정 운영자에 몰 추가")
    public ApiResult<MallResDto> createMall(
            @PathVariable("id") @NotBlank String id,
            @RequestBody MallCreateReqDto reqDto) {
        reqDto.setAdminId(id);
        MallResDto newMall = mallService.save(reqDto);
        return new ApiResult<>(newMall);
    }

    // 몰 정보 변경
    @PutMapping(value = "/admin/{id}/malls/{no}")
    @ApiOperation(value = "몰 정보 수정")
    public ApiResult<MallResDto> updateMallInfo(
            @PathVariable("id") @NotBlank String id,
            @PathVariable("no") @NotBlank Long no,
            @RequestBody MallUpdateReqDto reqDto) {
        reqDto.setAdminId(id);
        MallResDto updatedMall = mallService.update(no, reqDto);
        return new ApiResult<>(updatedMall);
    }

    // 몰 삭제
    @DeleteMapping(value = "/admin/{id}/malls/{no}")
    public ApiResult<MallResDto> deleteMallInfo(
            @PathVariable("id") @NotBlank String id,
            @PathVariable("no") @NotBlank Long no) {
        MallResDto deletedMall = mallService.delete(id, no);
        return new ApiResult<>(deletedMall);
    }

    // 몰 상태 변경
    @PutMapping(value = "/admin/{id}/malls/{no}/changeStatus")
    public ApiResult<MallStatusResDto> changeMallStatus(
            @PathVariable("id") @NotBlank String id,
            @PathVariable("no") @NotBlank Long no,
            @RequestBody @NotBlank MallStatus status) {
        MallStatusResDto updatedMall = mallService.updateStatus(id, no, status);
        return new ApiResult<>(updatedMall);
    }
}
