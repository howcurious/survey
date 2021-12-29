package cn.nbbandxdd.survey.resprec.repository.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * <p>按分组作答统计Entity。
 *
 * @author howcurious
 */
@Data
public class GrpStatEntity {

    /**
     * <p>问卷编号。
     */
    @Column("EXAM_CD")
    private String examCd;

    /**
     * <p>部门名。
     */
    @Column("DPRT_NAM")
    private String dprtNam;

    /**
     * <p>分组名。
     */
    @Column("GRP_NAM")
    private String grpNam;

    /**
     * <p>参与人数。
     */
    @Column("CNT")
    private Integer cnt;

    /**
     * <p>总人数。
     */
    @Column("TOT_CNT")
    private Integer totCnt;

    /**
     * <p>参与率。
     */
    @Column("PTPN_RATE")
    private Double ptpnRate;

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
}
