package cn.nbbandxdd.survey.ncov.repository;

import cn.nbbandxdd.survey.ncov.repository.entity.NCoVEntity;
import cn.nbbandxdd.survey.ncov.repository.entity.NCoVStatEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface NCoVRepository extends ReactiveCrudRepository<NCoVEntity, String> {

    @Query("SELECT DPRT_NAM, " +
            "SUM(CASE WHEN Q01 LIKE '到岗%' AND HAM_CD LIKE 'TY%' THEN 1 ELSE 0 END) AS ON_CNT_I, " +
            "SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND HAM_CD LIKE 'TY%' THEN 1 ELSE 0 END) AS OFF_CNT_I, " +
            "SUM(CASE WHEN Q01 LIKE '请假%' AND HAM_CD LIKE 'TY%' THEN 1 ELSE 0 END) AS LEAVE_CNT_I, " +
            "SUM(CASE WHEN Q01 LIKE '到岗%' AND HAM_CD NOT LIKE 'TY%' THEN 1 ELSE 0 END) AS ON_CNT_E, " +
            "SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND HAM_CD NOT LIKE 'TY%' THEN 1 ELSE 0 END) AS OFF_CNT_E, " +
            "SUM(CASE WHEN Q01 LIKE '请假%' AND HAM_CD NOT LIKE 'TY%' THEN 1 ELSE 0 END) AS LEAVE_CNT_E " +
        "FROM NCoV WHERE DPRT_NAM IS NOT NULL GROUP BY DPRT_NAM")
    Flux<NCoVStatEntity> findDprtStat();

    @Query("SELECT DPRT_NAM, GRP_NAM, " +
            "SUM(CASE WHEN Q01 LIKE '到岗%' AND HAM_CD LIKE 'TY%' THEN 1 ELSE 0 END) AS ON_CNT_I, " +
            "SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND HAM_CD LIKE 'TY%' THEN 1 ELSE 0 END) AS OFF_CNT_I, " +
            "SUM(CASE WHEN Q01 LIKE '请假%' AND HAM_CD LIKE 'TY%' THEN 1 ELSE 0 END) AS LEAVE_CNT_I, " +
            "SUM(CASE WHEN Q01 LIKE '到岗%' AND HAM_CD NOT LIKE 'TY%' THEN 1 ELSE 0 END) AS ON_CNT_E, " +
            "SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND HAM_CD NOT LIKE 'TY%' THEN 1 ELSE 0 END) AS OFF_CNT_E, " +
            "SUM(CASE WHEN Q01 LIKE '请假%' AND HAM_CD NOT LIKE 'TY%' THEN 1 ELSE 0 END) AS LEAVE_CNT_E " +
        "FROM NCoV WHERE DPRT_NAM = :dprtNam AND GRP_NAM IS NOT NULL GROUP BY GRP_NAM")
    Flux<NCoVStatEntity> findGrpStat(String dprtNam);
}
