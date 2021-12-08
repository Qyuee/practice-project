package com.practice.project.config;

import com.practice.project.repository.AdminRepository;
import com.practice.project.repository.JpaAdminRepository;
import com.practice.project.service.AdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

@Configuration
public class SpringConfig {

    @Bean
    public AdminRepository adminRepository(){
        return new JpaAdminRepository();
    }

    // 상황에 따라서 repository를 변경 할 수 있다.
    @Bean
    public AdminService adminService() {
        return new AdminService(adminRepository());
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
