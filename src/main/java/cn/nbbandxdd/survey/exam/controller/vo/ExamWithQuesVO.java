package cn.nbbandxdd.survey.exam.controller.vo;

import cn.nbbandxdd.survey.ques.controller.vo.QuesVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>问卷题目VO。
 *
 * @author howcurious
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExamWithQuesVO extends ExamVO {

    /**
     * <p>题目列表。
     */
    private List<QuesVO> quesList;
}
