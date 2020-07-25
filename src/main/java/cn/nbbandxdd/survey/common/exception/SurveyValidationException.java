package cn.nbbandxdd.survey.common.exception;

/**
 * <p>内部逻辑校验异常。
 * 
 * @author howcurious
 */
public class SurveyValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 无参构造器。
	 */
	public SurveyValidationException() {
		
		super();
	}
	
	/**
	 * <p>形参为message的构造器。
	 * 
	 * @param message message
	 */
	public SurveyValidationException(String message) {
		
		super(message);
	}
	
	/**
	 * <p>形参为message，cause的构造器。
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public SurveyValidationException(String message, Throwable cause) {
		
		super(message, cause);
	}
}
