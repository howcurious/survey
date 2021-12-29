package cn.nbbandxdd.survey.resprec.controller.vo;

import lombok.Data;

/**
 * <p>按分组作答统计VO。
 *
 * @author howcurious
 */
@Data
public class GrpStatVO {

    /**
     * <p>问卷编号。
     */
    private String examCd;

    /**
     * <p>部门名。
     */
    private String dprtNam;

    /**
     * <p>分组名。
     */
    private String grpNam;

    /**
     * <p>参与人数。
     */
    private Integer cnt;

    /**
     * <p>总人数。
     */
    private Integer totCnt;

    /**
     * <p>参与率。
     */
    private Double ptpnRate;

    /**
     * <p>平均分数。
     */
    private Double avgScre;

    /**
     * <p>平均用时。
     */
    private Double avgSpnd;
}
