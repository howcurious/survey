package cn.nbbandxdd.survey.ques.repository;

import cn.nbbandxdd.survey.ques.repository.entity.AnswEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>答案Repository。
 *
 * @author howcurious
 */
public interface AnswRepository extends ReactiveCrudRepository<AnswEntity, String> {

    /**
     * <p>新增答案。
     *
     * @param entity 答案Entity
     * @return 影响行数
     */
    @Modifying
    @Query("INSERT INTO ANSW (QUES_CD, ANSW_CD, TYP_CD, DSC, SCRE, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) VALUES (:#{#entity.quesCd}, :#{#entity.answCd}, :#{#entity.typCd}, :#{#entity.dsc}, :#{#entity.scre}, :#{#entity.lastMantUsr}, :#{#entity.lastMantDat}, :#{#entity.lastMantTmstp})")
    Mono<Integer> insert(AnswEntity entity);

    /**
     * <p>依据题目编号{@code quesCd}和最后维护用户{@code lastMantUsr}删除记录。
     *
     * @param quesCd 题目编号
     * @param lastMantUsr 最后维护用户
     * @return 影响行数
     */
    @Modifying
    @Query("DELETE FROM ANSW WHERE QUES_CD = :quesCd AND LAST_MANT_USR = :lastMantUsr")
    Mono<Integer> deleteByQuesCdAndLastMantUsr(String quesCd, String lastMantUsr);

    @Query("SELECT QUES_CD, ANSW_CD, TYP_CD, DSC, SCRE, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP FROM ANSW WHERE QUES_CD = :quesCd")
    Flux<AnswEntity> findByQuesCd(String quesCd);

    @Query("SELECT ANSW_CD FROM ANSW " +
        "WHERE QUES_CD = :quesCd AND (SELECT ANSW_CD FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd and QUES_CD = :quesCd" +
        ") LIKE CONCAT('%', ANSW_CD, '%')")
    Flux<String> findSelByOpenIdAndExamCdAndQuesCd(String openId, String examCd, String quesCd);
}
