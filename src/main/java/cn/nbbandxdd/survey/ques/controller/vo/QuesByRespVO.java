package cn.nbbandxdd.survey.ques.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>答题者视角题目VO。
 *
 * @author howcurious
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuesByRespVO extends QuesVO {

    /**
     * <p>答案列表。
     */
    private List<AnswVO> answList;
}
