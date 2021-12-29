package cn.nbbandxdd.survey.resprec.controller.vo;

import lombok.Data;

/**
 * <p>按问卷作答统计VO。
 *
 * @author howcurious
 */
@Data
public class ExamStatVO {

    /**
     * <p>问卷编号。
     */
    private String examCd;

    /**
     * <p>标题。
     */
    private String ttl;

    /**
     * <p>平均分数。
     */
    private Double avgScre;

    /**
     * <p>平均用时。
     */
    private Double avgSpnd;

    /**
     * <p>参与人数。
     */
    private Integer cnt;

    /**
     * <p>参与人数，分数区间[0, 40]。
     */
    private Integer cntU40;

    /**
     * <p>分数占比，分数区间[0, 40]。
     */
    private Double rateU40;

    /**
     * <p>参与人数，分数区间[41, 70]。
     */
    private Integer cntU70;

    /**
     * <p>分数占比，分数区间[41, 70]。
     */
    private Double rateU70;

    /**
     * <p>参与人数，分数区间[71, 100]。
     */
    private Integer cntU100;

    /**
     * <p>分数占比，分数区间[71, 100]。
     */
    private Double rateU100;

    /**
     * <p>参与人数，分数100。
     */
    private Integer cnt100;

    /**
     * <p>分数占比，分数100。
     */
    private Double rate100;
}
