package cn.nbbandxdd.survey.resprec.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <p>作答记录Entity。
 *
 * @author howcurious
 */
@Data
@Table("RESP_REC")
public class RespRecEntity {

    /**
     * <p>微信小程序中用户openId，与问卷编号{@code examCd}组成组合主键。
     */
    @Column("OPEN_ID")
    private String openId;

    /**
     * <p>问卷编号，与openId {@code openId}组成组合主键。
     */
    @Column("EXAM_CD")
    private String examCd;

    /**
     * <p>分数。
     */
    @Column("SCRE")
    private Integer scre;

    /**
     * <p>用时。
     */
    @Column("SPND")
    private Integer spnd;

    /**
     * <p>作答日期。
     */
    @Column("DAT")
    private String dat;
}
