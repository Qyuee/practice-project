package com.practice.project.api;

import com.practice.project.dto.common.Result;
import com.practice.project.dto.mall.MallDto.MallResDto;
import com.practice.project.service.MallService;
import com.practice.project.validator.AllowedSortProperties;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class MallApiController {
    private final MallService mallService;

    /*
    GET /api/mall -> 몰 정보 리스트 (pageable)
     - 운영자와 관련없이 전체몰의 정보를 찾기 위함
     - 파라미터: 국가코드, 몰 이름

    GET /api/mall/{admin_id} -> 운영자 ID기반 몰 정보 찾기
     - 파라미터: 국가코드 -> 국가별 몰 정보를 찾기 위함

    POST /api/mall
     - 운영자 고유번호
     - 몰 이름
     - 국가코드 (default: KR)
     - 주소정보
     */

    @GetMapping("/api/malls")
    @ApiOperation(value = "몰 리스트 조회")
    public Result<List<MallResDto>> getMalls(@AllowedSortProperties({"name, countryType"}) Pageable pageable) {
        List<MallResDto> mallList = mallService.findAll(pageable);
        return new Result<>(mallList.size(), mallList);
    }
}
