package cn.nbbandxdd.survey.exam.repository;

import cn.nbbandxdd.survey.exam.repository.entity.ExamQuesRlnEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * <p>问卷与题目间关系Repository。
 *
 * <ul>
 * <li>新增问卷与题目间关系，使用{@link #insert(ExamQuesRlnEntity)}。</li>
 * <li>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}删除记录，使用{@link #deleteByExamCdAndLastMantUsr(String, String)}。</li>
 * <li>依据问卷编号{@code examCd}、题目编号{@code quesCd}和最后维护用户{@code lastMantUsr}删除记录，使用{@link #deleteByExamCdAndQuesCdAndLastMantUsr(String, String, String)}。</li>
 * <li>依据问卷编号{@code examCd}查询记录数量，使用{@link #findQuesCntByExamCd(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
public interface ExamQuesRlnRepository extends ReactiveCrudRepository<ExamQuesRlnEntity, String> {

    /**
     * <p>新增问卷与题目间关系。
     *
     * @param entity 问卷与题目间关系Entity
     * @return 影响行数
     */
    @Modifying
    @Query("INSERT INTO EXAM_QUES_RLN (EXAM_CD, QUES_CD, SEQ_NO, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) VALUES (:#{#entity.examCd}, :#{#entity.quesCd}, :#{#entity.seqNo}, :#{#entity.lastMantUsr}, :#{#entity.lastMantDat}, :#{#entity.lastMantTmstp})")
    Mono<Integer> insert(ExamQuesRlnEntity entity);

    /**
     * <p>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}删除记录。
     *
     * @param examCd 问卷编号
     * @param lastMantUsr 最后维护用户
     * @return 影响行数
     */
    @Modifying
    @Query("DELETE FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd AND LAST_MANT_USR = :lastMantUsr")
    Mono<Integer> deleteByExamCdAndLastMantUsr(String examCd, String lastMantUsr);

    /**
     * <p>依据问卷编号{@code examCd}、题目编号{@code quesCd}和最后维护用户{@code lastMantUsr}删除记录。
     *
     * @param examCd 问卷编号
     * @param quesCd 题目编号
     * @param lastMantUsr 最后维护用户
     * @return 影响行数
     */
    @Modifying
    @Query("DELETE FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd AND QUES_CD = :quesCd AND LAST_MANT_USR = :lastMantUsr")
    Mono<Integer> deleteByExamCdAndQuesCdAndLastMantUsr(String examCd, String quesCd, String lastMantUsr);

    /**
     * <p>依据问卷编号{@code examCd}查询记录数量。
     *
     * @param examCd 问卷编号
     * @return 题目数量
     */
    @Query("SELECT IFNULL(COUNT(1), 0) FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd")
    Mono<Integer> findQuesCntByExamCd(String examCd);
}
