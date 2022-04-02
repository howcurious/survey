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
 * <ul>
 * <li>依据题目编号{@code quesCd}和最后维护用户{@code lastMantUsr}删除记录，使用{@link #deleteByQuesCdAndLastMantUsr(String, String)}。</li>
 * <li>依据最后维护用户{@code lastMantUsr}和题目类型{@code typCd}随机抽样{@code cnt}项记录，使用{@link #findRandomByLastMantUsrAndTypCd(String, String, Integer)}。</li>
 * <li>依据问卷编号{@code examCd}和OpenId{@code openId}查询待作答题目列表，使用{@link #findToAnswByExamCdAndOpenId(String, String)}。</li>
 * <li>依据问卷编号{@code examCd}分页查询记录，使用{@link #findByExamCd(String, Integer, Integer)}。</li>
 * <li>依据最后维护用户{@code lastMantUsr}分页查询记录，使用{@link #findByLastMantUsr(String, Integer, Integer)}。</li>
 * <li>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}分页查询未纳入问卷的题目列表，使用{@link #findAvaByExamCdAndLastMantUsr(String, String, Integer, Integer)}。</li>
 * <li>依据题目编号{@code quesCd}查询记录，使用{@link #findByQuesCd(String)}。</li>
 * <li>依据题目编号{@code quesCd}和最后维护用户{@code lastMantUsr}查询记录，使用{@link #findByQuesCdAndLastMantUsr(String, String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
public interface QuesRepository extends ReactiveCrudRepository<QuesEntity, String> {

    /**
     * <p>依据题目编号{@code quesCd}和最后维护用户{@code lastMantUsr}删除记录。
     *
     * @param quesCd 题目编号
     * @param lastMantUsr 最后维护用户
     * @return 影响行数
     */
    @Modifying
    @Query("DELETE FROM QUES WHERE QUES_CD = :quesCd AND LAST_MANT_USR = :lastMantUsr")
    Mono<Integer> deleteByQuesCdAndLastMantUsr(String quesCd, String lastMantUsr);

    /**
     * <p>依据最后维护用户{@code lastMantUsr}和题目类型{@code typCd}随机抽样{@code cnt}项记录。
     *
     * @param lastMantUsr 最后维护用户
     * @param typCd 题目类型
     * @param cnt 随机抽样数量
     * @return 题目Entity列表
     */
    @Query("""
        SELECT QUES_CD, TYP_CD, DSC, COMM FROM QUES \
        WHERE LAST_MANT_USR IN (SELECT ROLE_ID FROM ROLE_INFO WHERE OPEN_ID = :lastMantUsr) AND TYP_CD = :typCd ORDER BY RAND() LIMIT :cnt \
        """)
    Flux<QuesEntity> findRandomByLastMantUsrAndTypCd(String lastMantUsr, String typCd, Integer cnt);

    /**
     * <p>依据问卷编号{@code examCd}和OpenId{@code openId}查询待作答题目列表。
     *
     * @param examCd 问卷编号
     * @param openId OpenId
     * @return 题目Entity列表
     */
    @Query("""
        SELECT QUES.QUES_CD AS QUES_CD, QUES.TYP_CD AS TYP_CD, QUES.DSC AS DSC, \
            QUES.LAST_MANT_USR AS LAST_MANT_USR, QUES.LAST_MANT_DAT AS LAST_MANT_DAT, \
            QUES.LAST_MANT_TMSTP AS LAST_MANT_TMSTP \
        FROM (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd) RLN \
        LEFT JOIN QUES ON RLN.QUES_CD = QUES.QUES_CD \
        WHERE SEQ_NO > ( \
            SELECT IFNULL(MAX(RLN.SEQ_NO), -1) \
            FROM (SELECT * FROM DTL_REC WHERE OPEN_ID = :openId AND EXAM_CD = :examCd) REC \
            LEFT JOIN (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd) RLN \
            ON REC.EXAM_CD = RLN.EXAM_CD AND REC.QUES_CD = RLN.QUES_CD \
        ) ORDER BY RLN.SEQ_NO \
        """)
    Flux<QuesEntity> findToAnswByExamCdAndOpenId(String examCd, String openId);

    /**
     * <p>依据问卷编号{@code examCd}分页查询记录。
     *
     * @param examCd 问卷编号
     * @param limit limit
     * @param offset offset
     * @return 题目Entity列表
     */
    @Query("""
        SELECT QUES.QUES_CD AS QUES_CD, QUES.TYP_CD AS TYP_CD, QUES.DSC AS DSC, \
        QUES.LAST_MANT_USR AS LAST_MANT_USR, QUES.LAST_MANT_DAT AS LAST_MANT_DAT, QUES.LAST_MANT_TMSTP AS LAST_MANT_TMSTP \
        FROM QUES \
        RIGHT JOIN (SELECT * FROM EXAM_QUES_RLN WHERE EXAM_CD = :examCd) RLN \
            ON QUES.QUES_CD = RLN.QUES_CD \
        ORDER BY RLN.SEQ_NO LIMIT :limit OFFSET :offset \
        """)
    Flux<QuesEntity> findByExamCd(String examCd, Integer limit, Integer offset);

    /**
     * <p>依据最后维护用户{@code lastMantUsr}分页查询记录。
     *
     * @param lastMantUsr 最后维护用户
     * @param limit limit
     * @param offset offset
     * @return 题目Entity列表
     */
    @Query("""
        SELECT QUES_CD, TYP_CD, DSC, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP \
        FROM QUES \
        WHERE LAST_MANT_USR = :lastMantUsr \
        ORDER BY QUES_CD DESC LIMIT :limit OFFSET :offset \
        """)
    Flux<QuesEntity> findByLastMantUsr(String lastMantUsr, Integer limit, Integer offset);

    /**
     * <p>依据问卷编号{@code examCd}和最后维护用户{@code lastMantUsr}分页查询未纳入问卷的题目列表。
     *
     * @param examCd 问卷编号
     * @param lastMantUsr 最后维护用户
     * @param limit limit
     * @param offset offset
     * @return 题目Entity列表
     */
    @Query("""
        SELECT QUES_CD, TYP_CD, DSC, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP \
        FROM QUES \
        WHERE LAST_MANT_USR IN (SELECT ROLE_ID FROM ROLE_INFO WHERE OPEN_ID = :lastMantUsr) \
        AND NOT EXISTS (SELECT 1 FROM EXAM_QUES_RLN RLN \
            WHERE RLN.EXAM_CD = :examCd AND QUES.QUES_CD = RLN.QUES_CD \
            ) \
        ORDER BY QUES_CD DESC LIMIT :limit OFFSET :offset \
        """)
    Flux<QuesEntity> findAvaByExamCdAndLastMantUsr(String examCd, String lastMantUsr, Integer limit, Integer offset);

    /**
     * <p>依据题目编号{@code quesCd}查询记录。
     *
     * @param quesCd 题目编号
     * @return 题目Entity
     */
    @Query("SELECT QUES_CD, TYP_CD, DSC, COMM, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP FROM QUES WHERE QUES_CD = :quesCd")
    Mono<QuesEntity> findByQuesCd(String quesCd);

    /**
     * <p>依据题目编号{@code quesCd}和最后维护用户{@code lastMantUsr}查询记录。
     *
     * @param quesCd 题目编号
     * @param lastMantUsr 最后维护用户
     * @return 题目Entity
     */
    @Query("""
        SELECT QUES_CD, TYP_CD, DSC, COMM, LAST_MANT_USR, LAST_MANT_DAT, LAST_MANT_TMSTP \
        FROM QUES \
        WHERE QUES_CD = :quesCd AND LAST_MANT_USR IN (SELECT ROLE_ID FROM ROLE_INFO WHERE OPEN_ID = :lastMantUsr) \
        """)
    Mono<QuesEntity> findByQuesCdAndLastMantUsr(String quesCd, String lastMantUsr);
}
