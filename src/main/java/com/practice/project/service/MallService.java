package com.practice.project.service;

import com.practice.project.domain.Admin;
import com.practice.project.domain.Mall;
import com.practice.project.dto.mall.MallDto.MallCreateReqDto;
import com.practice.project.dto.mall.MallDto.MallResDto;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MallService {
    private final MallRepository mallRepository;
    private final AdminRepository adminRepository;

    public MallService(MallRepository mallRepository, AdminRepository adminRepository) {
        this.mallRepository = mallRepository;
        this.adminRepository = adminRepository;
    }

    // Controller -> (dto) -> Service -> (dto -> entity) -> repository
    @Transactional
    public MallResDto save(MallCreateReqDto reqDto) {
        validateSaveMall(reqDto);
        Mall saveMall = mallRepository.save(MallCreateReqDto.toEntity(reqDto));
        return MallResDto.of(saveMall);
    }

    public List<MallResDto> findAll(Pageable pageable) {
        Sort sort = pageable.getSort();
        Iterator<Sort.Order> iterator = sort.iterator();
        while (iterator.hasNext()) {
            Sort.Order next = iterator.next();
            log.info("property:{}", next.getProperty());
            log.info("direction:{}", next.getDirection().name());
        }
        Page<Mall> mallPage = mallRepository.findAll(pageable);
        log.info("totalPage:{}", mallPage.getTotalPages());
        return mallPage.stream().map(MallResDto::of).collect(Collectors.toList());
    }

    public List<MallResDto> findByAdminId(String id) {
        Optional<Admin> admin = adminRepository.findById(id);
        if (admin.isEmpty()) {
            throw new ApiResourceNotFoundException("Admin not exist.");
        }

        return mallRepository.findMallByAdmin(admin.get()).stream().map(MallResDto::of).collect(Collectors.toList());
    }

    private void validateSaveMall(MallCreateReqDto reqDto) {
        // 운영자 ID기반 존재여부 확인
        if (adminRepository.findById(reqDto.getAdminId()).isEmpty()) {
            throw new ApiResourceConflictException("Admin not exist.");
        }

        // 몰 이름 중복 여부 확인
        if (ofNullable(mallRepository.findByName(reqDto.getMallName())).isPresent()) {
            throw new ApiResourceConflictException("The name of the mall is already in use.");
        }

        // 국가코드 중복 여부 확인
        if (mallRepository.existsMallByAdminAndCountryType(reqDto.getAdmin(), reqDto.getCountryType())) {
            throw new ApiResourceConflictException("There is already a mall in the country.");
        }
    }
}
