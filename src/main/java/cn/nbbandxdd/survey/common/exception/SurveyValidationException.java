package cn.nbbandxdd.survey.common.exception;

public class SurveyValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SurveyValidationException() {
		
		super();
	}
	
	public SurveyValidationException(String message) {
		
		super(message);
	}
	
	public SurveyValidationException(String message, Throwable cause) {
		
		super(message, cause);
	}
}
