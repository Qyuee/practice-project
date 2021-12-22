package com.practice.project.service;

import com.practice.project.domain.Admin;
import com.practice.project.dto.AdminDto;
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

    @Transactional
    public AdminResDto update(Long no, AdminUpdateReqDto reqDto) {
        Optional<Admin> oPtOldAdmin = adminRepository.findById(no);

        if (oPtOldAdmin.isEmpty()) {
            throw new ApiResourceNotFoundException("Admin not exist.");
        }

        Admin oldAdmin = oPtOldAdmin.get();
        if (! oldAdmin.getAddress().equals(reqDto.getAddress())) {
            oldAdmin.changeAddress(reqDto.getAddress());
        }
        oldAdmin.changePhNumber(reqDto.getPhNumber());

        return AdminResDto.toDto(oldAdmin);
    }

    public Admin findOne(Long no) {
        Optional<Admin> optionalAdmin = adminRepository.findById(no);
        if (optionalAdmin.isEmpty()) {
            throw new ApiResourceNotFoundException("Admin not exist.");
        } else {
            return optionalAdmin.get();
        }
    }

    public AdminResDto findById(String id) {
        Optional<Admin> findAdmin = adminRepository.findById(id);
        if (findAdmin.isEmpty()) {
            throw new ApiResourceNotFoundException("Admin not exist.");
        } else {
            return AdminResDto.toDto(findAdmin.get());
        }
    }

    public List<AdminResDto> findAdmins(Pageable pageable) {
        return adminRepository.findAll(pageable).stream().map(AdminResDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public AdminSimpleResDto removeAdmin(Long no) {
        Optional<Admin> oPtAdmin = adminRepository.findById(no);
        if (oPtAdmin.isEmpty()) {
            throw new ApiResourceNotFoundException("Admin not exist.");
        }
        return AdminSimpleResDto.toDto(oPtAdmin.get());
    }

    /**
     * 중복 운영자 가입여부 검증
     * - 아이디, 이메일
     */
    private void validateDuplicateAdmin(AdminCreateReqDto reqDto) {
        if (adminRepository.existsByIdOrEmail(reqDto.getId(), reqDto.getEmail())) {
            throw new ApiResourceConflictException("Admin already exists.");
        }
    }
}
