package cn.nbbandxdd.survey.exam.controller.vo;

import lombok.Data;

/**
 * <p>问卷状态VO。
 *
 * @author howcurious
 */
@Data
public class ExamStatusVO {

    /**
     * <p>问卷编号。
     */
    private String examCd;

    /**
     * <p>问卷状态。
     */
    private String status;
}
