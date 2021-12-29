package cn.nbbandxdd.survey.usrinfo.repository;

import cn.nbbandxdd.survey.usrinfo.repository.entity.RoleInfoEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * <p>角色信息Repository。
 *
 * <ul>
 * <li>新增用户角色，使用{@link #save(String, String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
public interface RoleInfoRepository extends ReactiveCrudRepository<RoleInfoEntity, String> {

    /**
     * <p>新增用户角色。
     *
     * @param openId openId
     * @param roleId 角色Id
     * @return 角色信息Entity
     */
    @Modifying
    @Query("INSERT INTO ROLE_INFO (OPEN_ID, ROLE_ID) VALUES (:openId, :roleId)")
    Mono<RoleInfoEntity> save(String openId, String roleId);
}
