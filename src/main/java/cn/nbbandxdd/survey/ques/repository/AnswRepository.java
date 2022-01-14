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
 * <ul>
 * <li>新增答案，使用{@link #insert(AnswEntity)}。</li>
 * <li>依据题目编号{@code quesCd}和最后维护用户{@code lastMantUsr}删除记录，使用{@link #deleteByQuesCdAndLastMantUsr(String, String)}。</li>
 * <li>依据题目编号{@code quesCd}查询记录列表，使用{@link #findByQuesCd(String)}。</li>
 * <li>依据OpenId{@code openId}、问卷编号{@code examCd}和题目编号{@code quesCd}查询用户选择的答案编号，使用{@link #findSelByOpenIdAndExamCdAndQuesCd(String, String, String)}。</li>
 * </ul>
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

    /**
     * <p>依据题目编号{@code quesCd}查询记录列表。
     *
     * @param quesCd 题目编号
     * @return 答案Entity列表
     */
    @Query("SELECT QUES_CD, ANSW_CD, TYP_CD, DSC, SCRE, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP FROM ANSW WHERE QUES_CD = :quesCd")
    Flux<AnswEntity> findByQuesCd(String quesCd);

    /**
     * <p>依据OpenId{@code openId}、问卷编号{@code examCd}和题目编号{@code quesCd}查询用户选择的答案编号。
     *
     * @param openId OpenId
     * @param examCd 问卷编号
     * @param quesCd 题目编号
     * @return 用户选择的答案编号
     */
    @Query("SELECT ANSW_CD FROM ANSW " +
        "WHERE QUES_CD = :quesCd AND (SELECT ANSW_CD FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd and QUES_CD = :quesCd" +
        ") LIKE CONCAT('%', ANSW_CD, '%')")
    Flux<String> findSelByOpenIdAndExamCdAndQuesCd(String openId, String examCd, String quesCd);
}
