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
        Optional<Mall> oPtMall = mallRepository.findByNo(dto.getMallNo());
        oPtMall.orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Mall not exist.");
        });

        return memberRepository.findByMallAndId(oPtMall.get(), dto.getId()).map(member -> {
            member.changePhNumber(dto.getPhNumber());
            member.changeBirthDate(dto.getBirthdate());
            return MemberUpdateResDto.toDto(member);

        }).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Member not exist.");
        });
    }

    /**
     * 회원 정보 삭제
     */
    public MemberSimpleResDto delete(Long mallNo, String id) {
        //@Todo 삭제 제한 조건 검증 - 진행중인 주문이 존재하는 회원
        validateDeleteMember();

        return mallRepository.findByNo(mallNo).map(mall ->
                memberRepository.findByMallAndId(mall, id).map(member -> {
                    memberRepository.delete(member);
                    return MemberSimpleResDto.builder().mallNo(mallNo).id(id).build();
                }).orElseThrow(() -> {
                    throw new ApiResourceNotFoundException("Member not exist.");
                })).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Mall not exist.");
        });
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
        Optional<Mall> oPtMall = mallRepository.findByNo(mallNo);

        if (oPtMall.isEmpty()) {
            throw new ApiResourceNotFoundException("Mall not exist.");
        }

        Page<Member> memberPage = memberRepository.findAllByMall(oPtMall.get(), pageable);
        return memberPage.stream().map(MemberSearchResDto::toDto).collect(Collectors.toList());
    }

    /**
     * 특정 몰 회원 검색
     */
    public MemberSearchResDto findByMallNoAndId(Long mallNo, String id) {
        return mallRepository.findByNo(mallNo).map(mall ->
                MemberSearchResDto.toDto(memberRepository.findByMallAndId(mall, id).get())

        ).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Mall not exist.");
        });
    }

    /**
     *  특정 몰 회원 전제 삭제
     */
    @Transactional
    public void deleteAll(Mall mall) {
        memberRepository.deleteAllByMall(mall);
    }


    /**
     * 회원 생성 검증
     */
    private void validateSaveMember(MemberCreateReqDto dto) {
        // 생성하려는 몰의 존재 여부 확인
        Optional<Mall> oPtMall = mallRepository.findByNo(dto.getMallNo());
        if (oPtMall.isEmpty()) {
            throw new ApiResourceNotFoundException("Mall not exist.");

        } else {
            dto.setMall(oPtMall.get());
        }

        // 회원ID, Email 중복 여부 확인
        if (memberRepository.existsMembersById(dto.getId())) {
            throw new ApiResourceConflictException("'" + dto.getId() + "' was already used.");
        }

        if (memberRepository.existsMembersByEmail(dto.getEmail())) {
            throw new ApiResourceConflictException("'" + dto.getEmail() + "' was already used.");
        }
    }

    private void validateDeleteMember() {

    }
}
