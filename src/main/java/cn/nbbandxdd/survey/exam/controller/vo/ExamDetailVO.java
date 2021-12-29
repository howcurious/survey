package cn.nbbandxdd.survey.exam.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>问卷详情VO。
 *
 * @author howcurious
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExamDetailVO extends ExamVO {

    /**
     * <p>问卷题目数量。
     */
    private Integer quesCnt;

    /**
     * <p>问卷与题目类型间关系列表。
     */
    private List<ExamQuesTypRlnVO> quesTypRlnList;
}
