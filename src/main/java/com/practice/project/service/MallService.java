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
        /*
        //@Todo νμΈνμ
        Sort sort = pageable.getSort();
        Iterator<Sort.Order> iterator = sort.iterator();
        while (iterator.hasNext()) {
            Sort.Order next = iterator.next();
            log.info("property:{}", next.getProperty());
            log.info("direction:{}", next.getDirection().name());
        }*/
        Page<Mall> mallPage = mallRepository.findAll(pageable);
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
        // μ΄μμ IDκΈ°λ° μ‘΄μ¬μ¬λΆ νμΈ
        adminRepository.findByAdminId(reqDto.getAdminId()).ifPresentOrElse(admin -> {
            reqDto.setAdmin(admin);
        }, () -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });

        // λͺ° μ΄λ¦ μ€λ³΅ μ¬λΆ νμΈ
        mallRepository.findByName(reqDto.getMallName()).ifPresent(mall -> {
            throw new ApiResourceConflictException("The name of the mall is already in use.");
        });

        // κ΅­κ°μ½λ μ€λ³΅ μ¬λΆ νμΈ
        if (mallRepository.existsMallByAdminAndCountryType(reqDto.getAdmin(), reqDto.getCountryType())) {
            throw new ApiResourceConflictException("There is already a mall in the country.");
        }
    }

    // λͺ° μ­μ  μ¬μ κ²μ¬
    private void validateDeleteMall(Mall mall) {
        // λͺ°μ μ°κ΄λ μ£Όλ¬Έκ±΄μ΄ μ‘΄μ¬νλμ§ νμΈ
        // μ§νμ€μΈ μ£Όλ¬Έμ΄ μλ€λ©΄ μ­μ  λΆκ°

        // λͺ°μ κ°μλ λͺ¨λ  νμ μ­μ  μ²λ¦¬
        memberService.deleteAll(mall);
    }
}
