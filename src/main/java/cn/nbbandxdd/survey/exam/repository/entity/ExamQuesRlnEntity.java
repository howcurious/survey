package cn.nbbandxdd.survey.exam.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <p>问卷与题目间关系Entity。
 *
 * @author howcurious
 */
@Data
@Table("EXAM_QUES_RLN")
public class ExamQuesRlnEntity {

    /**
     * <p>问卷编号，与题目编号{@code quesCd}组成组合主键。
     */
    @Column("EXAM_CD")
    private String examCd;

    /**
     * <p>题目编号，与问卷编号{@code examCd}组成组合主键。
     */
    @Column("QUES_CD")
    private String quesCd;

    /**
     * <p>题目序号。
     */
    @Column("SEQ_NO")
    private Integer seqNo;

    /**
     * <p>最后维护用户。
     */
    @Column("LAST_MANT_USR")
    private String lastMantUsr;

    /**
     * <p>最后维护日期。
     */
    @Column("LAST_MANT_DAT")
    private String lastMantDat;

    /**
     * <p>最后维护时间戳。
     */
    @Column("LAST_MANT_TMSTP")
    private LocalDateTime lastMantTmstp;
}
