package cn.nbbandxdd.survey.ques.controller.vo;

import lombok.Data;

/**
 * <p>题目VO。
 *
 * @author howcurious
 */
@Data
public class QuesVO {

    /**
     * <p>题目编号。
     */
    private String quesCd;

    /**
     * <p>题目类型。
     */
    private String typCd;

    /**
     * <p>描述。
     */
    private String dsc;
}
