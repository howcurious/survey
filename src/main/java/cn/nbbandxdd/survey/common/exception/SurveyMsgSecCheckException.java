package cn.nbbandxdd.survey.common.exception;

public class SurveyMsgSecCheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SurveyMsgSecCheckException() {
		
		super();
	}
	
	public SurveyMsgSecCheckException(String message) {
		
		super(message);
	}
	
	public SurveyMsgSecCheckException(String message, Throwable cause) {
		
		super(message, cause);
	}
}
