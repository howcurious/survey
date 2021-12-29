package cn.nbbandxdd.survey.exam.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>问卷新增VO。
 *
 * @author howcurious
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExamInsertVO extends ExamVO {

    /**
     * <p>问卷与题目类型间关系列表。
     */
    private List<ExamQuesTypRlnVO> quesTypRlnList;
}
