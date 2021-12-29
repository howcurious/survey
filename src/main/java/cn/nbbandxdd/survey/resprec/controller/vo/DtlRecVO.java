package cn.nbbandxdd.survey.resprec.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>作答明细VO。
 *
 * @author howcurious
 */
@Data
public class DtlRecVO {

    /**
     * <p>问卷编号。
     */
    private String examCd;

    /**
     * <p>题目编号。
     */
    private String quesCd;

    /**
     * <p>答案编号列表。
     */
    private List<String> answList;

    /**
     * <p>最后一题标识。
     */
    private String lastInd;

    /**
     * <p>问卷作答分数。
     */
    private Integer respScre;

    /**
     * <p>问卷作答用时。
     */
    private Integer respSpnd;
}
