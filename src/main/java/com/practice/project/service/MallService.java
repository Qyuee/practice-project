package com.practice.project.service;

import com.practice.project.domain.Mall;
import com.practice.project.domain.statusinfo.MallStatus;
import com.practice.project.dto.MallDto.MallCreateReqDto;
import com.practice.project.dto.MallDto.MallResDto;
import com.practice.project.dto.MallDto.MallStatusResDto;
import com.practice.project.dto.MallDto.MallUpdateReqDto;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import com.practice.project.repository.AdminRepository;
import com.practice.project.repository.MallRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MallService {
    private final MallRepository mallRepository;
    private final AdminRepository adminRepository;
    private final MemberService memberService;

    public MallService(MallRepository mallRepository, AdminRepository adminRepository, MemberService memberService) {
        this.mallRepository = mallRepository;
        this.adminRepository = adminRepository;
        this.memberService = memberService;
    }

    @Transactional
    public MallResDto save(MallCreateReqDto reqDto) {
        validateSaveMall(reqDto);
        Mall saveMall = mallRepository.save(MallCreateReqDto.toEntity(reqDto));
        return MallResDto.toDto(saveMall);
    }

    public List<MallResDto> findAll(Pageable pageable) {
        Sort sort = pageable.getSort();
        Iterator<Sort.Order> iterator = sort.iterator();
        //@Todo 확인필요
        while (iterator.hasNext()) {
            Sort.Order next = iterator.next();
            log.info("property:{}", next.getProperty());
            log.info("direction:{}", next.getDirection().name());
        }
        Page<Mall> mallPage = mallRepository.findAll(pageable);
        log.info("totalPage:{}", mallPage.getTotalPages());
        return mallPage.stream().map(MallResDto::toDto).collect(Collectors.toList());
    }

    public List<MallResDto> findByAdminId(String id) {
        return adminRepository.findByAdminId(id).map(admin ->
                mallRepository.findByAdmin(admin).stream().map(MallResDto::toDto).collect(Collectors.toList())
        ).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });
    }

    public MallResDto findByNo(String adminId, Long no) {
        return adminRepository.findByAdminId(adminId).map(admin -> {
            return mallRepository.findByAdminAndNo(admin, no).map(MallResDto::toDto).orElseThrow(() -> {
                throw new ApiResourceNotFoundException("Mall not exist.");
            });
        }).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });
    }

    @Transactional
    public MallResDto update(Long no, MallUpdateReqDto reqDto) {
        adminRepository.findByAdminId(reqDto.getAdminId()).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });

        return mallRepository.findByNo(no).map(mall -> {
            mall.changeMallName(reqDto.getMallName());
            mall.changeAddress(reqDto.getAddress());
            return MallResDto.toDto(mall);
        }).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Mall not exist.");
        });
    }

    @Transactional
    public MallStatusResDto updateStatus(String adminId, Long mallNo, MallStatus status) {
        adminRepository.findByAdminId(adminId).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });

        return mallRepository.findByNo(mallNo).map(mall -> {
            mall.changeStatus(status);
            return MallStatusResDto.toDto(mall);
        }).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Mall not exist.");
        });
    }

    @Transactional
    public MallResDto delete(String adminId, Long no) {
        adminRepository.findByAdminId(adminId).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });

        return mallRepository.findByNo(no).map(mall -> {
            validateDeleteMall(mall);
            mallRepository.delete(mall);
            return MallResDto.toDto(mall);
        }).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Mall not exist.");
        });
    }

    private void validateSaveMall(MallCreateReqDto reqDto) {
        // 운영자 ID기반 존재여부 확인
        adminRepository.findByAdminId(reqDto.getAdminId()).ifPresentOrElse(admin -> {
            reqDto.setAdmin(admin);
        }, () -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });

        // 몰 이름 중복 여부 확인
        mallRepository.findByName(reqDto.getMallName()).ifPresent(mall -> {
            throw new ApiResourceConflictException("The name of the mall is already in use.");
        });

        // 국가코드 중복 여부 확인
        if (mallRepository.existsMallByAdminAndCountryType(reqDto.getAdmin(), reqDto.getCountryType())) {
            throw new ApiResourceConflictException("There is already a mall in the country.");
        }
    }

    // 몰 삭제 사전검사
    private void validateDeleteMall(Mall mall) {
        // 몰에 연관된 주문건이 존재하는지 확인
        // 진행중인 주문이 있다면 삭제 불가

        // 몰에 가입된 모든 회원 삭제 처리
        memberService.deleteAll(mall);
    }
}
