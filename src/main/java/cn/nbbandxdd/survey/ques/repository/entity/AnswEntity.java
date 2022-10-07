package cn.nbbandxdd.survey.ques.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <p>答案Entity。
 *
 * @author howcurious
 */
@Data
@Table("ANSW")
public class AnswEntity {

    /**
     * <p>题目编号，与答案编号{@code answCd}组成组合主键。
     */
    @Column("QUES_CD")
    private String quesCd;

    /**
     * <p>答案编号，与题目编号{@code quesCd}组成组合主键。
     */
    @Column("ANSW_CD")
    private String answCd;

    /**
     * <p>答案类型。
     */
    @Column("TYP_CD")
    private String typCd;

    /**
     * <p>描述。
     */
    @Column("DSC")
    private String dsc;

    /**
     * <p>分数。
     */
    @Column("SCRE")
    private Integer scre;

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
