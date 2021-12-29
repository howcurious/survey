package cn.nbbandxdd.survey.exam.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <p>问卷Entity。
 *
 * @author howcurious
 */
@Data
@Table("EXAM")
public class ExamEntity implements Persistable<String> {

    /**
     * <p>问卷编号。
     */
    @Id
    @Column("EXAM_CD")
    private String examCd;

    /**
     * <p>问卷类型。
     */
    @Column("TYP_CD")
    private String typCd;

    /**
     * <p>重复作答标识。
     */
    @Column("RPET_IND")
    private String rpetInd;

    /**
     * <p>倒计时标识。
     */
    @Column("CNTDWN_IND")
    private String cntdwnInd;

    /**
     * <p>立即展示答案标识。
     */
    @Column("ANSW_IMM_IND")
    private String answImmInd;

    /**
     * <p>标题。
     */
    @Column("TTL")
    private String ttl;

    /**
     * <p>描述。
     */
    @Column("DSC")
    private String dsc;

    /**
     * <p>起始时间。
     */
    @Column("BGN_TIME")
    private LocalDateTime bgnTime;

    /**
     * <p>截止时间。
     */
    @Column("END_TIME")
    private LocalDateTime endTime;

    /**
     * <p>下一个题目序号。
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

    /**
     * <p>新增或修改标识。如果为新增，则设置为{@literal true}，否则设置为{@literal false}。
     */
    @Transient
    private boolean isNew;

    /**
     * getId
     *
     * @return 问卷编号{@code examCd}
     */
    @Override
    public String getId() {

        return examCd;
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
