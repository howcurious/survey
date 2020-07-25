package cn.nbbandxdd.survey.common.exception;

/**
 * <p>微信小程序内容安全校验异常。
 * 
 * @author howcurious
 */
public class SurveyMsgSecCheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 无参构造器。
	 */
	public SurveyMsgSecCheckException() {
		
		super();
	}
	
	/**
	 * <p>形参为message的构造器。
	 * 
	 * @param message message
	 */
	public SurveyMsgSecCheckException(String message) {
		
		super(message);
	}
	
	/**
	 * <p>形参为message，cause的构造器。
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public SurveyMsgSecCheckException(String message, Throwable cause) {
		
		super(message, cause);
	}
}
