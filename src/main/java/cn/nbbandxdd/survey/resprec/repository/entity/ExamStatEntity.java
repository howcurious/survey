package cn.nbbandxdd.survey.resprec.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * <p>按问卷作答统计Entity。
 *
 * @author howcurious
 */
@Data
public class ExamStatEntity {

    /**
     * <p>问卷编号。
     */
    @Column("EXAM_CD")
    private String examCd;

    /**
     * <p>标题。
     */
    @Column("TTL")
    private String ttl;

    /**
     * <p>平均分数。
     */
    @Column("AVG_SCRE")
    private Double avgScre;

    /**
     * <p>平均用时。
     */
    @Column("AVG_SPND")
    private Double avgSpnd;

    /**
     * <p>参与人数。
     */
    @Column("CNT")
    private Integer cnt;

    /**
     * <p>参与人数，分数区间[0, 40]。
     */
    @Column("CNT_U40")
    private Integer cntU40;

    /**
     * <p>分数占比，分数区间[0, 40]。
     */
    @Column("RATE_U40")
    private Double rateU40;

    /**
     * <p>参与人数，分数区间[41, 70]。
     */
    @Column("CNT_U70")
    private Integer cntU70;

    /**
     * <p>分数占比，分数区间[41, 70]。
     */
    @Column("RATE_U70")
    private Double rateU70;

    /**
     * <p>参与人数，分数区间[71, 100]。
     */
    @Column("CNT_U100")
    private Integer cntU100;

    /**
     * <p>分数占比，分数区间[71, 100]。
     */
    @Column("RATE_U100")
    private Double rateU100;

    /**
     * <p>参与人数，分数100。
     */
    @Column("CNT_100")
    private Integer cnt100;

    /**
     * <p>分数占比，分数100。
     */
    @Column("RATE_100")
    private Double rate100;
}
