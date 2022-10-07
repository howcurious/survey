package cn.nbbandxdd.survey.admin_user.repository;

import cn.nbbandxdd.survey.admin_user.repository.entity.AdminUserInfoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AdminUserRepository extends ReactiveCrudRepository<AdminUserInfoEntity, String> {
    Mono<AdminUserInfoEntity> findAdminUserInfoEntityByUserName(String userName);
}
