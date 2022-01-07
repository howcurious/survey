package cn.nbbandxdd.survey.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>异常信息Entity。
 *
 * @author howcurious
 */
@Data
@AllArgsConstructor
public class ExceptionEntity {

    /**
     * 错误码。
     */
    private String errCode;

    /**
     * 错误信息。
     */
    private String errMsg;
}
