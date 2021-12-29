package cn.nbbandxdd.survey.exam.controller.vo;

import lombok.Data;

/**
 * <p>问卷与题目类型间关系VO。
 *
 * @author howcurious
 */
@Data
public class ExamQuesTypRlnVO {

    /**
     * <p>题目类型，与问卷编号{@code examCd}组成组合主键。
     */
    private String quesTypCd;

    /**
     * <p>题目类型数量。
     */
    private Integer cnt;

    /**
     * <p>题目类型分数。
     */
    private Integer scre;
}
