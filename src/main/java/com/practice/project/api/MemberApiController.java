package com.practice.project.api;

import com.practice.project.dto.MemberDto;
import com.practice.project.dto.MemberDto.MemberCreateReqDto;
import com.practice.project.dto.MemberDto.MemberCreateResDto;
import com.practice.project.dto.MemberDto.MemberSearchResDto;
import com.practice.project.dto.common.Result;
import com.practice.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    // 회원 생성
    @PostMapping("/malls/{mall_no}/members")
    public Result<MemberCreateResDto> createMember(
            @PathVariable("mall_no") Long mallNo,
            @RequestBody MemberCreateReqDto reqDto) {
        reqDto.setMallNo(mallNo);
        MemberCreateResDto savedMember = memberService.save(reqDto);
        return new Result<>(savedMember);
    }

    // 특정 쇼핑몰 회원 리스트 조회
    @GetMapping("/malls/{mall_no}/members")
    public Result<List<MemberSearchResDto>> getMemberList(
            @PathVariable("mall_no") Long mallNo,
            @RequestParam Pageable pageable) {
        List<MemberSearchResDto> memberList = memberService.findAllByMall(mallNo, pageable);
        return new Result<>(memberList.size(), memberList);
    }

    // 특정 쇼핑몰 특정 회원 정보 조회
    @GetMapping("/malls/{mall_no}/members/{id}")
    public Result<MemberSearchResDto> getMember(
            @PathVariable("mall_no") Long mallNo,
            @PathVariable("id") String id) {
        MemberSearchResDto searchMember = memberService.findByMallNoAndId(mallNo, id);
        return new Result<>(searchMember);
    }
}
