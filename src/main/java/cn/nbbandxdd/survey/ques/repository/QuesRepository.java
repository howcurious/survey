package cn.nbbandxdd.survey.ques.repository;

import cn.nbbandxdd.survey.ques.repository.entity.QuesEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>题目Repository。
 *
 * @author howcurious
 */
public interface QuesRepository extends ReactiveCrudRepository<QuesEntity, String> {

    @Modifying
    @Query("DELETE FROM QUES WHERE QUES_CD = :quesCd AND LAST_MANT_USR = :lastMantUsr")
    Mono<Integer> deleteByQuesCdAndLastMantUsr(String quesCd, String lastMantUsr);

    @Query("SELECT QUES_CD, TYP_CD, DSC, COMM FROM QUES " +
        "WHERE LAST_MANT_USR IN (SELECT ROLE_ID FROM ROLE_INFO WHERE OPEN_ID = :lastMantUsr) AND TYP_CD = :typCd ORDER BY RAND() LIMIT :cnt")
    Flux<QuesEntity> findRandomByLastMantUsrAndTypCd(String lastMantUsr, String typCd, Integer cnt);

    @Query("SELECT QUES.QUES_CD AS QUES_CD, QUES.TYP_CD AS TYP_CD, QUES.DSC AS DSC, " +
            "QUES.LAST_MANT_USR AS LAST_MANT_USR, QUES.LAST_MANT_DAT AS LAST_MANT_DAT, " +
            "QUES.LAST_MANT_TMSTP AS LAST_MANT_TMSTP " +
        "FROM (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd) RLN " +
        "LEFT JOIN QUES ON RLN.QUES_CD = QUES.QUES_CD " +
        "WHERE SEQ_NO > (" +
            "SELECT IFNULL(MAX(RLN.SEQ_NO), -1) " +
            "FROM (SELECT * FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd) REC " +
            "LEFT JOIN (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd) RLN " +
            "ON REC.EXAM_CD = RLN.EXAM_CD AND REC.QUES_CD = RLN.QUES_CD" +
        ") ORDER BY RLN.SEQ_NO")
    Flux<QuesEntity> findToAnswByExamCdAndOpenId(String examCd, String openId);

    @Query("SELECT QUES.QUES_CD AS QUES_CD, QUES.TYP_CD AS TYP_CD, QUES.DSC AS DSC, " +
        "QUES.LAST_MANT_USR AS LAST_MANT_USR, QUES.LAST_MANT_DAT AS LAST_MANT_DAT, QUES.LAST_MANT_TMSTP AS LAST_MANT_TMSTP " +
        "FROM QUES " +
        "RIGHT JOIN (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd) RLN " +
            "ON QUES.QUES_CD = RLN.QUES_CD " +
        "ORDER BY RLN.SEQ_NO LIMIT :limit OFFSET :offset")
    Flux<QuesEntity> findByExamCd(String examCd, Integer limit, Integer offset);

    @Query("SELECT QUES_CD, TYP_CD, DSC, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
        "FROM QUES " +
        "WHERE LAST_MANT_USR = :lastMantUsr " +
        "ORDER BY QUES_CD DESC LIMIT :limit OFFSET :offset")
    Flux<QuesEntity> findByLastMantUsr(String lastMantUsr, Integer limit, Integer offset);

    @Query("SELECT QUES_CD, TYP_CD, DSC, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
        "FROM QUES " +
        "WHERE LAST_MANT_USR IN (SELECT ROLE_ID FROM ROLE_INFO WHERE OPEN_ID = :lastMantUsr) " +
        "AND NOT EXISTS (" +
            "SELECT 1 FROM EXAM_QUES_RLN RLN " +
            "WHERE RLN.EXAM_CD = :examCd AND QUES.QUES_CD = RLN.QUES_CD" +
        ") " +
        "ORDER BY QUES_CD DESC LIMIT :limit OFFSET :offset")
    Flux<QuesEntity> findAvaByExamCdAndLastMantUsr(String examCd, String lastMantUsr, Integer limit, Integer offset);

    @Query("SELECT QUES_CD, TYP_CD, DSC, COMM, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP FROM QUES WHERE QUES_CD = :quesCd")
    Mono<QuesEntity> findByQuesCd(String quesCd);

    @Query("SELECT QUES_CD, TYP_CD, DSC, COMM, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP " +
        "FROM QUES " +
        "WHERE QUES_CD = :quesCd AND LAST_MANT_USR IN (SELECT ROLE_ID FROM ROLE_INFO WHERE OPEN_ID = :lastMantUsr)")
    Mono<QuesEntity> findByQuesCdAndLastMantUsr(String quesCd, String lastMantUsr);
}
