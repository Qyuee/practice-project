package com.practice.project.api;

import com.practice.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    // 회원 생성

    // 쇼핑몰 회원 리스트 조회

    // 쇼핑몰 회원 정보 조회

    // 쇼핑몰
}
