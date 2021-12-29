package cn.nbbandxdd.survey.exam.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <p>问卷与题目类型间关系Entity。
 *
 * @author howcurious
 */
@Data
@Table("EXAM_QUES_TYP_RLN")
public class ExamQuesTypRlnEntity implements Persistable<String> {

    /**
     * <p>问卷编号，与题目类型{@code quesTypCd}组成组合主键。
     */
    @Column("EXAM_CD")
    private String examCd;

    /**
     * <p>题目类型，与问卷编号{@code examCd}组成组合主键。
     */
    @Column("QUES_TYP_CD")
    private String quesTypCd;

    /**
     * <p>题目类型分数。
     */
    @Column("SCRE")
    private Integer scre;

    /**
     * <p>题目类型数量。
     */
    @Column("CNT")
    private Integer cnt;

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

    /**
     * <p>新增或修改标识。如果为新增，则设置为{@literal true}，否则设置为{@literal false}。
     */
    @Transient
    private boolean isNew;

    /**
     * getId
     *
     * @return 问卷编号{@code examCd}与题目类型{@code quesTypCd}组成组合主键
     */
    @Override
    public String getId() {

        return examCd + quesTypCd;
    }

    /**
     * isNew
     *
     * @return 新增或修改标识{@code isNew}
     */
    @Override
    public boolean isNew() {

        return isNew;
    }
}
