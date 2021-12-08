package com.practice.project.service;

import com.practice.project.domain.Admin;
import com.practice.project.dto.admin.AdminUpdateRequest;
import com.practice.project.exception.exhandler.ApiResourceDuplicateException;
import com.practice.project.exception.exhandler.ApiResourceNotFoundException;
import com.practice.project.repository.AdminRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Long save(Admin admin) {
        validateDuplicateAdmin(admin);
        return adminRepository.save(admin);
    }

    @Transactional
    public void update(Long no, AdminUpdateRequest request) {
        Admin admin = adminRepository.findOne(no);
        Optional.ofNullable(admin).ifPresentOrElse(oldAdmin -> {
            // oldAdmin의 Address와 updateAdmin의 Address 값이 다르면 변경 필요, 아니면 pass
            if (! oldAdmin.getAddress().equals(request.getAddress())) {
                oldAdmin.changeAddress(request.getAddress());
            }
            oldAdmin.changePhNumber(request.getPhNumber());
        }, () -> {
            throw new ApiResourceNotFoundException("Admin not exist.");
        });
    }

    public Admin findOne(Long no) {
        Admin admin = adminRepository.findOne(no);
        if (Optional.ofNullable(admin).isEmpty()) {
            throw new ApiResourceNotFoundException("Admin not exist.");
        } else {
            return admin;
        }
    }

    public List<Admin> findAdmins() {
        return adminRepository.findAll();
    }

    public Admin findById(String id) {
        Optional<Admin> findAdmin = adminRepository.findById(id);
        if (findAdmin.isEmpty()) {
            throw new ApiResourceNotFoundException("Admin not exist.");
        } else {
            return findAdmin.get();
        }
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Transactional
    public Long removeAdmin(Long no) {
        Admin findAdmin = this.findOne(no);
        return adminRepository.remove(findAdmin);
    }

    /**
     * 중복 운영자 가입여부 검증
     * - 아이디, 이메일
     * @param admin
     */
    private void validateDuplicateAdmin(Admin admin) {
        List<Admin> findAdmins = adminRepository.findByIdOrEmail(admin.getId(), admin.getEmail());
        if (! findAdmins.isEmpty()) {
            throw new ApiResourceDuplicateException("Admin already exists.");
        }
    }
}
