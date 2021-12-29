package cn.nbbandxdd.survey.resprec.controller.vo;

import lombok.Data;

/**
 * <p>作答记录VO。
 *
 * @author howcurious
 */
@Data
public class RespRecVO {

    /**
     * <p>问卷编号。
     */
    private String examCd;

    /**
     * <p>问卷标题。
     */
    private String ttl;

    /**
     * <p>分数。
     */
    private Integer scre;

    /**
     * <p>用时。
     */
    private Integer spnd;

    /**
     * <p>作答日期。
     */
    private String dat;
}
