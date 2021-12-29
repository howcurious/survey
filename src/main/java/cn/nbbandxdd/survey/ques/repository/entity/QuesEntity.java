package cn.nbbandxdd.survey.ques.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <p>题目Entity。
 *
 * @author howcurious
 */
@Data
@Table("QUES")
public class QuesEntity implements Persistable<String> {

    /**
     * <p>题目编号。
     */
    @Id
    @Column("QUES_CD")
    private String quesCd;

    /**
     * <p>题目类型。
     */
    @Column("TYP_CD")
    private String typCd;

    /**
     * <p>描述。
     */
    @Column("DSC")
    private String dsc;

    /**
     * <p>题解。
     */
    @Column("COMM")
    private String comm;

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
     * @return 题目编号{@code quesCd}
     */
    @Override
    public String getId() {

        return quesCd;
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
