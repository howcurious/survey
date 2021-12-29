package cn.nbbandxdd.survey.ques.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>回看者视角题目VO。
 *
 * @author howcurious
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuesByExpVO extends QuesVO {

    /**
     * <p>题解。
     */
    private String comm;

    /**
     * <p>答案列表。
     */
    private List<AnswVO> answList;

    /**
     * <p>选择列表。
     */
    private List<String> selList;
}
