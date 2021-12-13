package com.practice.project.service;

import com.practice.project.dto.mall.MallDto.MallCreateReqDto;
import com.practice.project.dto.mall.MallDto.MallResDto;
import com.practice.project.exception.exhandler.ApiBadRequestException;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.repository.AdminRepository;
import com.practice.project.repository.MallRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MallService {
    private final MallRepository mallRepository;
    private final AdminRepository adminRepository;
    private final List<String> supportSortKey = Arrays.asList("name", "countryType");

    public MallService(MallRepository mallRepository, AdminRepository adminRepository) {
        this.mallRepository = mallRepository;
        this.adminRepository = adminRepository;
    }

    // Controller -> (dto) -> Service -> (dto -> entity) -> repository
    public MallResDto save(MallCreateReqDto mallCreateReqDto) {
        validateSaveMall(mallCreateReqDto);
        return MallResDto.of(mallRepository.save(MallCreateReqDto.toEntity(mallCreateReqDto)));
    }

    public List<MallResDto> findAll(Pageable pageable) {
        // 정렬 파라미터 지원 validation
        // validateSearchMall(pageable.getSort());
        return mallRepository.findAll(pageable).stream().map(MallResDto::of).collect(Collectors.toList());
    }

    private void validateSaveMall(MallCreateReqDto req) {
        // 운영자 존재여부 확인
        if (adminRepository.findById(req.getAdmin().getNo()).isEmpty()) {
            throw new ApiResourceConflictException("Admin not exist.");
        }

        // 몰 이름 중복 여부 확인
        if (Optional.ofNullable(mallRepository.findByName(req.getMallName())).isPresent()) {
            throw new ApiResourceConflictException("The name of the mall is already in use.");
        }

        // 국가코드 중복 여부 확인
        if (mallRepository.existsMallByAdminAndCountryType(req.getAdmin(), req.getCountry())) {
            throw new ApiResourceConflictException("There is already a mall in the country.");
        }
    }

    private void validateSearchMall(Sort sort) {
        String sortKey = sort.toString();
        if (! supportSortKey.contains(sortKey)) {
            throw new ApiBadRequestException(sortKey+" is not supported key. please confirm your sort key.");
        }
    }
}
