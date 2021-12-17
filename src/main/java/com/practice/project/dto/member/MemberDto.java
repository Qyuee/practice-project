package com.practice.project.dto.member;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@RequiredArgsConstructor
public class MemberDto {
    private ModelMapper modelMapper;
}
