package com.practice.project.service;

import com.practice.project.domain.Admin;
import com.practice.project.dto.AdminDto.AdminCreateReqDto;
import com.practice.project.dto.AdminDto.AdminResDto;
import com.practice.project.dto.AdminDto.AdminSimpleResDto;
import com.practice.project.dto.AdminDto.AdminUpdateReqDto;
import com.practice.project.exception.exhandler.ApiResourceConflictException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import com.practice.project.repository.AdminRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * 운영자 가입
     */
    @Transactional
    public AdminResDto save(AdminCreateReqDto reqDto) {
        validateDuplicateAdmin(reqDto);
        Admin newAdmin = adminRepository.save(AdminCreateReqDto.toEntity(reqDto));
        return AdminResDto.toDto(newAdmin);
    }

    /**
     * 운영자 정보 수정
     */
    @Transactional
    public AdminResDto update(String adminId, AdminUpdateReqDto reqDto) {
        Optional<Admin> optAdmin = adminRepository.findByAdminId(adminId);
        optAdmin.orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });

        Admin admin = optAdmin.get();
        if (! admin.getAddress().equals(reqDto.getAddress())) {
            admin.changeAddress(reqDto.getAddress());
        }
        admin.changePhNumber(reqDto.getPhNumber());
        return AdminResDto.toDto(admin);
    }

    public Admin findOne(Long no) {
        Optional<Admin> admin = adminRepository.findById(no);
        admin.orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });
        return admin.get();
    }

    public AdminResDto findByAdminId(String id) {
        return adminRepository.findByAdminId(id).map(AdminResDto::toDto).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });
    }

    public List<AdminResDto> findAdmins(Pageable pageable) {
        return adminRepository.findAll(pageable).stream().map(AdminResDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AdminSimpleResDto removeByAdminId(String adminId) {
        return adminRepository.findByAdminId(adminId).map(admin -> {
            adminRepository.delete(admin);
            return AdminSimpleResDto.toDto(admin);
        }).orElseThrow(() -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });
    }

    /**
     * 중복 운영자 가입여부 검증
     */
    private void validateDuplicateAdmin(AdminCreateReqDto reqDto) {
        if (adminRepository.existsByAdminIdOrEmail(reqDto.getAdminId(), reqDto.getEmail())) {
            throw new ApiResourceConflictException("Admin already exists.");
        }
    }
}
