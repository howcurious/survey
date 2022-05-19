package cn.nbbandxdd.survey.ncov.repository;

import cn.nbbandxdd.survey.ncov.repository.entity.NCoVDetailEntity;
import cn.nbbandxdd.survey.ncov.repository.entity.NCoVEntity;
import cn.nbbandxdd.survey.ncov.repository.entity.NCoVStatEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NCoVRepository extends ReactiveCrudRepository<NCoVEntity, String> {

    @Query("""
        ( \
            SELECT '天津研发部' AS DPRT_NAM, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS ON_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS OFF_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS ON_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS OFF_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS ON_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS OFF_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_V \
            FROM NCoV WHERE DPRT_NAM IN ('天研开发一部', '天研开发二部', '天研开发三部', '天研测试部', '天研综合管理部') \
        ) \
            UNION ALL \
        ( \
            SELECT T1.DPRT_NAM, T2.ON_CNT_I, T2.OFF_CNT_I, T2.LEAVE_CNT_I, T2.AWAY_CNT_I, T2.TEMP_CNT_I, T2.GRAY_CNT_I, \
                T2.ON_CNT_E, T2.OFF_CNT_E, T2.LEAVE_CNT_E, T2.AWAY_CNT_E, T2.TEMP_CNT_E, T2.GRAY_CNT_E, \
                T2.ON_CNT_V, T2.OFF_CNT_V, T2.LEAVE_CNT_V, T2.AWAY_CNT_V, T2.TEMP_CNT_V, T2.GRAY_CNT_V \
            FROM (SELECT DPRT_NAM, MIN(SEQ_NO) AS SEQ_NO FROM GRP_INFO WHERE DPRT_NAM IN ('天研开发一部', '天研开发二部', '天研开发三部', '天研测试部', '天研综合管理部') GROUP BY DPRT_NAM ORDER BY SEQ_NO) T1 \
            LEFT JOIN (\
                SELECT DPRT_NAM, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS ON_CNT_I, \
                    IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS OFF_CNT_I, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_I, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_I, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_I, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_I, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS ON_CNT_E, \
                    IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS OFF_CNT_E, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_E, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_E, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_E, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_E, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS ON_CNT_V, \
                    IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS OFF_CNT_V, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_V, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_V, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_V, \
                    IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_V \
                FROM NCoV WHERE DPRT_NAM IS NOT NULL GROUP BY DPRT_NAM \
            ) T2 ON T1.DPRT_NAM = T2.DPRT_NAM \
            ORDER BY T1.SEQ_NO \
        ) \
        """)
    Flux<NCoVStatEntity> findDprtStat();

    @Query("""
        SELECT T1.DPRT_NAM, T1.GRP_NAM, \
            T2.ON_CNT_I, T2.OFF_CNT_I, T2.LEAVE_CNT_I, T2.AWAY_CNT_I, T2.TEMP_CNT_I, T2.GRAY_CNT_I, \
            T2.ON_CNT_E, T2.OFF_CNT_E, T2.LEAVE_CNT_E, T2.AWAY_CNT_E, T2.TEMP_CNT_E, T2.GRAY_CNT_E, \
            T2.ON_CNT_V, T2.OFF_CNT_V, T2.LEAVE_CNT_V, T2.AWAY_CNT_V, T2.TEMP_CNT_V, T2.GRAY_CNT_V \
        FROM (SELECT DPRT_NAM, GRP_NAM, SEQ_NO FROM GRP_INFO WHERE DPRT_NAM = :dprtNam ORDER BY SEQ_NO) T1 \
        LEFT JOIN ( \
            SELECT DPRT_NAM, GRP_NAM, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS ON_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS OFF_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'I' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_I, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS ON_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS OFF_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'E' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_E, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '到岗%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS ON_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 NOT LIKE '到岗%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS OFF_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '请假%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS LEAVE_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '离开驻地%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS AWAY_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '因居住地临时要求%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS TEMP_CNT_V, \
                IFNULL(SUM(CASE WHEN Q01 LIKE '%（涉及灰名单）%' AND FLAG = 'V' THEN 1 ELSE 0 END), 0) AS GRAY_CNT_V \
            FROM NCoV WHERE DPRT_NAM = :dprtNam AND GRP_NAM IS NOT NULL GROUP BY GRP_NAM \
        ) T2 ON T1.DPRT_NAM = T2.DPRT_NAM AND T1.GRP_NAM = T2.GRP_NAM \
        ORDER BY T1.SEQ_NO \
        """)
    Flux<NCoVStatEntity> findGrpStat(String dprtNam);

    @Query("SELECT DPRT_NAM, GRP_NAM, USR_NAM, HAM_CD, Q01 FROM NCoV WHERE DPRT_NAM = :dprtNam AND GRP_NAM = :grpNam ORDER BY HAM_CD")
    Flux<NCoVDetailEntity> findDetail(String dprtNam, String grpNam);

    @Query("SELECT COUNT(1) FROM NCoV WHERE OPEN_ID = :openId AND FLAG = 'I'")
    Mono<Integer> findCntByOpenIdAndHamCd(String openId);
}
