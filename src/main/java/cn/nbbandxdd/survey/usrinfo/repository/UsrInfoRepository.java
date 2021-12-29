package cn.nbbandxdd.survey.usrinfo.repository;

import cn.nbbandxdd.survey.usrinfo.repository.entity.UsrInfoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * <p>用户信息Repository。
 *
 * @author howcurious
 */
public interface UsrInfoRepository extends ReactiveCrudRepository<UsrInfoEntity, String> { }
