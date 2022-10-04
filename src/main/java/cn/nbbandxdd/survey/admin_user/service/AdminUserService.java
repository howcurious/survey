package cn.nbbandxdd.survey.admin_user.service;

import cn.nbbandxdd.survey.admin_user.repository.AdminUserRepository;
import cn.nbbandxdd.survey.admin_user.repository.entity.AdminUserInfoEntity;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;
    public AdminUserService(AdminUserRepository repository) {
        this.adminUserRepository = repository;
    }
    public Mono<AdminUserInfoEntity> getAdminUserByName(String name) {
        return adminUserRepository.findAdminUserInfoEntityByUserName(name);
    }
}
