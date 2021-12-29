package cn.nbbandxdd.survey.common.exception;

/**
 * <p>内部逻辑校验异常。
 *
 * @author howcurious
 */
public class SurveyValidationException extends RuntimeException {

    /**
     * <p>形参为message的构造器。
     *
     * @param message message
     */
    public SurveyValidationException(String message) {

        super(message);
    }
}
