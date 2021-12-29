package cn.nbbandxdd.survey.exam.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>问卷与题目间关系VO。
 *
 * @author howcurious
 */
@Data
public class ExamQuesRlnVO {

    /**
     * <p>问卷编号。
     */
    private String examCd;

    /**
     * <p>题目编号列表。
     */
    private List<String> quesCds;
}
