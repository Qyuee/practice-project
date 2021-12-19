package com.practice.project.service;

import com.practice.project.domain.Mall;
import com.practice.project.domain.Member;
import com.practice.project.dto.MemberDto.*;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import com.practice.project.repository.MallRepository;
import com.practice.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MallRepository mallRepository;
    private final MemberRepository memberRepository;

    /**
     * 회원 생성
     */
    @Transactional
    public MemberCreateResDto save(MemberCreateReqDto dto) {
        // id, email에 해당하는 회원이 존재하는가?
            // 존재하지 않으면 신규생성
            // 존재하면 update
            // 이렇게 service로직을 공유하면 변경되면 안되는 항목에 변경이 발생 할 수도 있을 듯. 혼란스러움
        validateSaveMember(dto);
        Member member = MemberCreateReqDto.toEntity(dto);
        return MemberCreateResDto.toDto(memberRepository.save(member));
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public MemberUpdateResDto update(MemberUpdateReqDto dto) {
        Mall mall = mallRepository.findByNo(dto.getMallNo());
        if (Optional.ofNullable(mall).isEmpty()) {
            throw new ApiResourceNotFoundException("Mall not exist.");
        }

        Member member = memberRepository.findByMallAndId(mall, dto.getId());
        if (Optional.ofNullable(member).isEmpty()) {
            throw new ApiResourceNotFoundException("Member not exist.");
        }
        member.changePhNumber(dto.getPhNumber());
        member.changeBirthDate(dto.getBirthdate());
        return MemberUpdateResDto.toDto(member);
    }

    /**
     * 회원 정보 삭제
     */
    public MemberBasicResDto delete(Long mallNo, String id) {
        //@Todo 삭제 제한 조건 검증 - 진행중인 주문이 존재하는 회원
        validateDeleteMember();

        Mall mall = mallRepository.findByNo(mallNo);
        if (Optional.ofNullable(mall).isEmpty()) {
            throw new ApiResourceNotFoundException("Mall not exist.");
        }

        Member deletedMember = memberRepository.findByMallAndId(mall, id);
        if (Optional.ofNullable(deletedMember).isEmpty()) {
            throw new ApiResourceNotFoundException("Member not exist.");
        }

        memberRepository.delete(deletedMember);
        return MemberBasicResDto.builder().mallNo(mallNo).id(id).build();
    }

    /**
     * 전체몰 전체 회원 리스트 검색 (pageable)
     */
    public List<MemberSearchResDto> findAll(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return memberPage.stream().map(MemberSearchResDto::toDto).collect(Collectors.toList());
    }

    /**
     * 특정 몰 회원 목록 검색 (mallNo, pageable)
     */
    public List<MemberSearchResDto> findAllByMall(Long mallNo, Pageable pageable) {
        Mall mall = mallRepository.findByNo(mallNo);

        if (Optional.ofNullable(mall).isEmpty()) {
            throw new ApiResourceNotFoundException("Mall not exist.");
        }

        Page<Member> memberPage = memberRepository.findAllByMall(mall, pageable);
        return memberPage.stream().map(MemberSearchResDto::toDto).collect(Collectors.toList());
    }

    /**
     * 특정 몰 회원 검색
     */
    public MemberSearchResDto findByMallNoAndId(Long mallNo, String id) {
        Mall mall = mallRepository.findByNo(mallNo);

        if (Optional.ofNullable(mall).isEmpty()) {
            throw new ApiResourceNotFoundException("Mall not exist.");
        }

        return MemberSearchResDto.toDto(memberRepository.findByMallAndId(mall, id));
    }


    /**
     * 회원 생성 검증
     */
    private void validateSaveMember(MemberCreateReqDto dto) {
        // 생성하려는 몰의 존재 여부 확인
        Mall findMall = mallRepository.findByNo(dto.getMallNo());
        if (Optional.ofNullable(findMall).isEmpty()) {
            throw new ApiResourceNotFoundException("Mall not exist.");

        } else {
            dto.setMall(findMall);
        }

        // 회원ID, Email 중복 여부 확인
        if (memberRepository.existsMembersById(dto.getId())) {
            throw new ApiResourceConflictException("'" +dto.getId()+ "' was already used.");
        }

        if (memberRepository.existsMembersByEmail(dto.getEmail())) {
            throw new ApiResourceConflictException("'" +dto.getEmail()+ "' was already used.");
        }
    }

    private void validateDeleteMember() {

    }
}
