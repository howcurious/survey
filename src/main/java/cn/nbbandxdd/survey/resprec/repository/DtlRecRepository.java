package cn.nbbandxdd.survey.resprec.repository;

import cn.nbbandxdd.survey.resprec.repository.entity.DtlRecEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * <p>作答明细Repository。
 *
 * <ul>
 * <li>新增作答明细，使用{@link #insert(DtlRecEntity)}。</li>
 * <li>依据OpenId{@code openId}、问卷编号{@code examCd}和题目编号{@code quesCd}删除记录，使用{@link #deleteByOpenIdAndExamCdAndQuesCd(String, String, String)}。</li>
 * <li>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}删除记录，使用{@link #deleteByExamCdAndLastMantUsr(String, String)}。</li>
 * <li>依据OpenId{@code openId}和问卷编号{@code examCd}查询手工选择题目问卷分数，使用{@link #findScreByOpenIdAndExamCdForDefinite(String, String)}。</li>
 * <li>依据OpenId{@code openId}和问卷编号{@code examCd}查询随机生成题目问卷分数，使用{@link #findScreByOpenIdAndExamCdForRandom(String, String)}。</li>
 * <li>依据OpenId{@code openId}和问卷编号{@code examCd}查询问卷用时，使用{@link #findSpndByOpenIdAndExamCd(String, String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
public interface DtlRecRepository extends ReactiveCrudRepository<DtlRecEntity, String> {

    /**
     * <p>新增作答明细。
     *
     * @param entity 作答明细Entity
     * @return 影响行数
     */
    @Modifying
    @Query("INSERT INTO DTL_REC (OPEN_ID, EXAM_CD, QUES_CD, ANSW_CD, SCRE, LAST_MANT_TMSTP) VALUES (:#{#entity.openId}, :#{#entity.examCd}, :#{#entity.quesCd}, :#{#entity.answCd}, :#{#entity.scre}, :#{#entity.lastMantTmstp})")
    Mono<Integer> insert(DtlRecEntity entity);

    /**
     * <p>依据OpenId{@code openId}、问卷编号{@code examCd}和题目编号{@code quesCd}删除记录。
     *
     * @param openId OpenId
     * @param examCd 问卷编号
     * @param quesCd 题目编号
     * @return 影响行数
     */
    @Modifying
    @Query("DELETE FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd AND QUES_CD = :quesCd")
    Mono<Integer> deleteByOpenIdAndExamCdAndQuesCd(String openId, String examCd, String quesCd);

    /**
     * <p>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}删除记录。
     *
     * @param examCd 问卷编号
     * @param lastMantUsr 最后维护用户
     * @return 影响行数
     */
    @Modifying
    @Query("DELETE FROM DTL_REC REC WHERE EXISTS (SELECT 1 FROM EXAM WHERE REC.EXAM_CD = EXAM.EXAM_CD AND EXAM.EXAM_CD = :examCd AND EXAM.LAST_MANT_USR = :lastMantUsr)")
    Mono<Integer> deleteByExamCdAndLastMantUsr(String examCd, String lastMantUsr);

    /**
     * <p>依据OpenId{@code openId}和问卷编号{@code examCd}查询手工选择题目问卷分数。
     *
     * @param openId OpenId
     * @param examCd 问卷编号
     * @return 手工选择题目问卷分数
     */
    @Query("""
        SELECT ROUND( \
        (SELECT COUNT(1) FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd AND SCRE > 0) \
        / \
        (SELECT COUNT(1) FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd) * 100 \
        ) \
        """)
    Mono<Integer> findScreByOpenIdAndExamCdForDefinite(String openId, String examCd);

    /**
     * <p>依据OpenId{@code openId}和问卷编号{@code examCd}查询随机生成题目问卷分数。
     *
     * @param openId OpenId
     * @param examCd 问卷编号
     * @return 随机生成题目问卷分数
     */
    @Query("""
        SELECT CASE WHEN SUM(RLN.SCRE) IS NULL THEN 0 ELSE SUM(RLN.SCRE) END \
        FROM (SELECT EXAM_CD, QUES_CD FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd AND SCRE > 0) REC \
            LEFT JOIN QUES ON QUES.QUES_CD = REC.QUES_CD \
            LEFT JOIN EXAM_QUES_TYP_RLN RLN ON RLN.EXAM_CD = REC.EXAM_CD AND RLN.QUES_TYP_CD = QUES.TYP_CD \
        """)
    Mono<Integer> findScreByOpenIdAndExamCdForRandom(String openId, String examCd);

    /**
     * <p>依据OpenId{@code openId}和问卷编号{@code examCd}查询问卷用时。
     *
     * @param openId OpenId
     * @param examCd 问卷编号
     * @return 问卷用时
     */
    @Query("SELECT TIMESTAMPDIFF(SECOND, MIN(LAST_MANT_TMSTP), MAX(LAST_MANT_TMSTP)) FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd")
    Mono<Integer> findSpndByOpenIdAndExamCd(String openId, String examCd);
}
