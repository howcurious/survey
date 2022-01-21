package cn.nbbandxdd.survey.grpinfo.repository;

import cn.nbbandxdd.survey.grpinfo.repository.entity.GrpInfoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>分组信息Repository。
 *
 * <ul>
 * <li>依据部门名{@code dprtNam}和分组名{@code grpNam}查询记录，使用{@link #findByDprtNamAndGrpNam(String, String)}。</li>
 * <li>依据部门名{@code dprtNam}查询记录，使用{@link #findByDprtNam(String)}。</li>
 * <li>查询所有部门名{@code dprtNam}，使用{@link #findDistinctDprtNam()}。</li>
 * </ul>
 *
 * @author howcurious
 */
public interface GrpInfoRepository extends ReactiveCrudRepository<GrpInfoEntity, String> {

    /**
     * <p>依据部门名{@code dprtNam}和分组名{@code grpNam}查询记录。
     *
     * @param dprtNam 部门名
     * @param grpNam 分组名
     * @return 分组信息Entity
     */
    @Query("SELECT DPRT_NAM, GRP_NAM, SEQ_NO FROM GRP_INFO WHERE DPRT_NAM = :dprtNam AND GRP_NAM = :grpNam")
    Mono<GrpInfoEntity> findByDprtNamAndGrpNam(String dprtNam, String grpNam);

    /**
     * <p>依据部门名{@code dprtNam}查询记录。
     *
     * @param dprtNam 部门名
     * @return 分组信息Entity
     */
    @Query("SELECT DPRT_NAM, GRP_NAM, SEQ_NO FROM GRP_INFO WHERE DPRT_NAM = :dprtNam ORDER BY SEQ_NO")
    Flux<GrpInfoEntity> findByDprtNam(String dprtNam);

    /**
     * <p>查询所有部门名{@code dprtNam}。
     *
     * @return 分组信息Entity
     */
    @Query("SELECT DPRT_NAM, MIN(SEQ_NO) AS SEQ_NO FROM GRP_INFO GROUP BY DPRT_NAM ORDER BY SEQ_NO")
    Flux<GrpInfoEntity> findDistinctDprtNam();
}
