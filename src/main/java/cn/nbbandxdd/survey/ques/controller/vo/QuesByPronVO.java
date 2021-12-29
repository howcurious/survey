package cn.nbbandxdd.survey.ques.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>出题者视角题目VO。
 *
 * @author howcurious
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuesByPronVO extends QuesVO {

    /**
     * <p>题解。
     */
    private String comm;

    /**
     * <p>答案列表。
     */
    private List<AnswVO> answList;
}
