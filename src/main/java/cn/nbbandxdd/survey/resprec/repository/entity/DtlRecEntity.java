package cn.nbbandxdd.survey.resprec.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <p>作答明细Entity。
 *
 * @author howcurious
 */
@Data
@Table("DTL_REC")
public class DtlRecEntity {

    /**
     * <p>微信小程序中用户openId，与问卷编号{@code examCd}、题目编号{@code quesCd}组成组合主键。
     */
    @Column("OPEN_ID")
    private String openId;

    /**
     * <p>问卷编号，与openId {@code openId}、题目编号{@code quesCd}组成组合主键。
     */
    @Column("EXAM_CD")
    private String examCd;

    /**
     * <p>题目编号，与openId {@code openId}、问卷编号{@code examCd}组成组合主键。
     */
    @Column("QUES_CD")
    private String quesCd;

    /**
     * <p>答案编号。
     */
    @Column("ANSW_CD")
    private String answCd;

    /**
     * <p>分数。
     */
    @Column("SCRE")
    private Integer scre;

    /**
     * <p>最后维护时间戳。
     */
    @Column("LAST_MANT_TMSTP")
    private LocalDateTime lastMantTmstp;
}
