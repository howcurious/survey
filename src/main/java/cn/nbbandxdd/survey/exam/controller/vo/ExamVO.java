package cn.nbbandxdd.survey.exam.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>问卷VO。
 *
 * @author howcurious
 */
@Data
public class ExamVO {

    /**
     * <p>问卷编号。
     */
    private String examCd;

    /**
     * <p>问卷类型。
     */
    private String typCd;

    /**
     * <p>重复作答标识。
     */
    private String rpetInd;

    /**
     * <p>倒计时标识。
     */
    private String cntdwnInd;

    /**
     * <p>立即展示答案标识。
     */
    private String answImmInd;

    /**
     * <p>标题。
     */
    private String ttl;

    /**
     * <p>描述。
     */
    private String dsc;

    /**
     * <p>起始时间。
     */
    private LocalDateTime bgnTime;

    /**
     * <p>截止时间。
     */
    private LocalDateTime endTime;
}
