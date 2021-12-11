package com.practice.project.service;

import com.practice.project.dto.mall.MallDto.MallCreateReq;
import com.practice.project.dto.mall.MallDto.Response;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.repository.AdminRepository;
import com.practice.project.repository.MallRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Response save(MallCreateReq mallCreateReq) {
        validateSaveMall(mallCreateReq);
        return Response.of(mallRepository.save(MallCreateReq.toEntity(mallCreateReq)));
    }

    private void validateSaveMall(MallCreateReq req) {
        // 운영자 존재여부 확인
        if (adminRepository.findById(req.getAdmin().getNo()).isEmpty()) {
            throw new ApiResourceConflictException("Admin not exist.");
        }

        // 국가별 하나씩만 생성 가능
        boolean test = mallRepository.existsMallByAdminAndCountryType(req.getAdmin(), req.getCountry());
        log.error("test: {}", test);

        if (test) {
            throw new ApiResourceConflictException("There is already a mall in the country.");
        }
    }
}
