package cn.nbbandxdd.survey.ncov.repository;

import cn.nbbandxdd.survey.ncov.repository.entity.NCoVEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NCoVRepository extends ReactiveCrudRepository<NCoVEntity, String> {

}
