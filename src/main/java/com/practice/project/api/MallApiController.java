package com.practice.project.api;

import com.practice.project.dto.common.Result;
import com.practice.project.dto.MallDto.MallCreateReqDto;
import com.practice.project.dto.MallDto.MallResDto;
import com.practice.project.service.MallService;
import com.practice.project.validator.AllowedSortProperties;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class MallApiController {
    private final MallService mallService;

    @GetMapping(value = "/api/malls", produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "몰 리스트 조회")
    public Result<List<MallResDto>> getMalls(
            @AllowedSortProperties({"name", "countryType", "createdDate"})
            @PageableDefault(size=5, sort="name", direction = Sort.Direction.ASC) Pageable pageable) {
        List<MallResDto> mallList = mallService.findAll(pageable);
        return new Result<>(mallList.size(), mallList);
    }

    @GetMapping(value = "/api/admin/{id}/malls", produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "운영자의 몰 리스트")
    public Result<List<MallResDto>> getAdminMalls(@PathVariable("id") String id) {
        List<MallResDto> mallList = mallService.findByAdminId(id);
        return new Result<>(mallList.size(), mallList);
    }

    @PostMapping(value = "/api/admin/{id}/malls", produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "특정 운영자에 몰 추가")
    public Result<MallResDto> createMall(
            @PathVariable("id") String id,
            @RequestBody MallCreateReqDto reqDto) {

        reqDto.setAdminId(id);
        MallResDto newMall = mallService.save(reqDto);
        return new Result<>(newMall);
    }
}
