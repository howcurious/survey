package cn.nbbandxdd.survey.exam.repository;

import cn.nbbandxdd.survey.exam.repository.entity.ExamQuesTypRlnEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>问卷与题目类型间关系Repository。
 *
 * <ul>
 * <li>新增问卷与题目类型间关系，使用{@link #insert(ExamQuesTypRlnEntity)}。</li>
 * <li>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}删除记录，使用{@link #deleteByExamCdAndLastMantUsr(String, String)}。</li>
 * <li>依据问卷编号{@code examCd}查询记录列表，使用{@link #findByExamCd(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
public interface ExamQuesTypRlnRepository extends ReactiveCrudRepository<ExamQuesTypRlnEntity, String> {

    /**
     * <p>新增问卷与题目类型间关系。
     *
     * @param entity 问卷与题目类型间关系Entity
     * @return 影响行数
     */
    @Modifying
    @Query("INSERT INTO EXAM_QUES_TYP_RLN (EXAM_CD, QUES_TYP_CD, SCRE, CNT, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP) " +
        "VALUES (:#{#entity.examCd}, :#{#entity.quesTypCd}, :#{#entity.scre}, :#{#entity.cnt}, :#{#entity.lastMantUsr}, :#{#entity.lastMantDat}, :#{#entity.lastMantTmstp})")
    Mono<Integer> insert(ExamQuesTypRlnEntity entity);

    /**
     * <p>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}删除记录。
     *
     * @param examCd 问卷编号
     * @param lastMantUsr 最后维护用户
     * @return 影响行数
     */
    @Modifying
    @Query("DELETE FROM EXAM_QUES_TYP_RLN WHERE EXAM_CD = :examCd AND LAST_MANT_USR = :lastMantUsr")
    Mono<Integer> deleteByExamCdAndLastMantUsr(String examCd, String lastMantUsr);

    /**
     * <p>依据问卷编号{@code examCd}查询记录列表。
     *
     * @param examCd 问卷编号
     * @return 问卷与题目类型间关系Entity列表
     */
    @Query("SELECT * FROM EXAM_QUES_TYP_RLN WHERE EXAM_CD = :examCd")
    Flux<ExamQuesTypRlnEntity> findByExamCd(String examCd);
}
