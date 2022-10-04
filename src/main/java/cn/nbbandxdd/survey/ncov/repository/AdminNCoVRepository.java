package cn.nbbandxdd.survey.ncov.repository;

import cn.nbbandxdd.survey.ncov.repository.entity.AdminNCoVEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminNCoVRepository extends ReactiveCrudRepository<AdminNCoVEntity, String> {
    Flux<AdminNCoVEntity> findAdminNCoVEntitiesByUserName(String userName);

}
