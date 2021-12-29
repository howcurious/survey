package cn.nbbandxdd.survey.ques.controller.vo;

import lombok.Data;

/**
 * <p>答案VO。
 *
 * @author howcurious
 */
@Data
public class AnswVO {

    /**
     * <p>题目编号。
     */
    private String quesCd;

    /**
     * <p>答案编号。
     */
    private String answCd;

    /**
     * <p>描述。
     */
    private String dsc;

    /**
     * <p>分数。
     */
    private Integer scre;
}
