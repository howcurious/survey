package cn.nbbandxdd.survey.resprec.controller.vo;

import lombok.Data;

/**
 * <p>作答排名VO。
 *
 * @author howcurious
 */
@Data
public class UsrRankVO {

    /**
     * <p>问卷编号。
     */
    private String examCd;

    /**
     * <p>部门名。
     */
    private String dprtNam;

    /**
     * <p>分组名。
     */
    private String grpNam;

    /**
     * <p>用户名。
     */
    private String usrNam;

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
