package cn.nbbandxdd.survey.exam.repository;

import cn.nbbandxdd.survey.exam.repository.entity.ExamEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>问卷Repository。
 *
 * <ul>
 * <li>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}删除记录，使用{@link #deleteByExamCdAndLastMantUsr(String, String)}。</li>
 * <li>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}修改下一个题目序号{@code seqNo}，使用{@link #updateSeqNoByExamCdAndLastMantUsr(String, String, Integer)}。</li>
 * <li>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}查询记录，使用{@link #findByExamCdAndLastMantUsrForUpdate(String, String)}。</li>
 * <li>依据最后维护用户{@code lastMantUsr}分页查询记录，使用{@link #findByLastMantUsr(String, Integer, Integer)}。</li>
 * </ul>
 *
 * @author howcurious
 */
public interface ExamRepository extends ReactiveCrudRepository<ExamEntity, String> {

    /**
     * <p>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}删除记录。
     *
     * @param examCd 问卷编号
     * @param lastMantUsr 最后维护用户
     * @return 影响行数
     */
    @Modifying
    @Query("DELETE FROM EXAM WHERE EXAM_CD = :examCd AND LAST_MANT_USR = :lastMantUsr")
    Mono<Integer> deleteByExamCdAndLastMantUsr(String examCd, String lastMantUsr);

    /**
     * <p>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}修改下一个题目序号{@code seqNo}。
     *
     * @param examCd 问卷编号
     * @param lastMantUsr 最后维护用户
     * @param seqNo 下一个题目序号
     * @return 影响行数
     */
    @Modifying
    @Query("UPDATE EXAM SET SEQ_NO = SEQ_NO + :seqNo WHERE EXAM_CD = :examCd AND LAST_MANT_USR = :lastMantUsr")
    Mono<Integer> updateSeqNoByExamCdAndLastMantUsr(String examCd, String lastMantUsr, Integer seqNo);

    /**
     * <p>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}查询记录。
     *
     * @param examCd 问卷编号
     * @param lastMantUsr 最后维护用户
     * @return 问卷Entity
     */
    @Query("SELECT * FROM EXAM WHERE EXAM_CD = :examCd AND LAST_MANT_USR = :lastMantUsr FOR UPDATE")
    Mono<ExamEntity> findByExamCdAndLastMantUsrForUpdate(String examCd, String lastMantUsr);

    /**
     * <p>依据最后维护用户{@code lastMantUsr}分页查询记录。
     *
     * @param lastMantUsr 最后维护用户
     * @param limit limit
     * @param offset offset
     * @return 问卷Entity
     */
    @Query("SELECT * FROM EXAM WHERE LAST_MANT_USR = :lastMantUsr ORDER BY EXAM_CD DESC LIMIT :limit OFFSET :offset")
    Flux<ExamEntity> findByLastMantUsr(String lastMantUsr, Integer limit, Integer offset);
}
