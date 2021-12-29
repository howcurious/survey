package cn.nbbandxdd.survey.resprec.repository;

import cn.nbbandxdd.survey.resprec.repository.entity.DtlRecEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * <p>作答明细Repository。
 *
 * @author howcurious
 */
public interface DtlRecRepository extends ReactiveCrudRepository<DtlRecEntity, String> {

    @Modifying
    @Query("INSERT INTO DTL_REC (OPEN_ID, EXAM_CD, QUES_CD, ANSW_CD, SCRE, LAST_MANT_TMSTP) VALUES (:#{#entity.openId}, :#{#entity.examCd}, :#{#entity.quesCd}, :#{#entity.answCd}, :#{#entity.scre}, :#{#entity.lastMantTmstp})")
    Mono<Integer> insert(DtlRecEntity entity);

    @Modifying
    @Query("DELETE FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd AND QUES_CD = :quesCd")
    Mono<Integer> deleteByOpenIdAndExamCdAndQuesCd(String openId, String examCd, String quesCd);

    @Modifying
    @Query("DELETE FROM DTL_REC REC WHERE EXISTS (SELECT 1 FROM EXAM WHERE REC.EXAM_CD = EXAM.EXAM_CD AND EXAM.EXAM_CD = :examCd AND EXAM.LAST_MANT_USR = :lastMantUsr)")
    Mono<Integer> deleteByExamCdAndLastMantUsr(String examCd, String lastMantUsr);

    @Query("SELECT ROUND(" +
        "(SELECT COUNT(1) FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd AND SCRE > 0)" +
        "/" +
        "(SELECT COUNT(1) FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd) * 100" +
        ")")
    Mono<Integer> findScreByOpenIdAndExamCdForDefinite(String openId, String examCd);

    @Query("SELECT CASE WHEN SUM(RLN.SCRE) IS NULL THEN 0 ELSE SUM(RLN.SCRE) END " +
        "FROM (SELECT EXAM_CD, QUES_CD FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd AND SCRE > 0) REC " +
            "LEFT JOIN QUES ON QUES.QUES_CD = REC.QUES_CD " +
            "LEFT JOIN EXAM_QUES_TYP_RLN RLN ON RLN.EXAM_CD = REC.EXAM_CD AND RLN.QUES_TYP_CD = QUES.TYP_CD")
    Mono<Integer> findScreByOpenIdAndExamCdForRandom(String openId, String examCd);

    @Query("SELECT TIMESTAMPDIFF(SECOND, MIN(LAST_MANT_TMSTP), MAX(LAST_MANT_TMSTP)) FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd")
    Mono<Integer> findSpndByOpenIdAndExamCd(String openId, String examCd);
}
