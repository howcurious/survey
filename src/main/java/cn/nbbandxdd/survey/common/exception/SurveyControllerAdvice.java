package cn.nbbandxdd.survey.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class SurveyControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionEntity> methodArgumentNotValidExceptionHandler(
		MethodArgumentNotValidException e) {
		
		ExceptionEntity entity = new ExceptionEntity();
		entity.setErrCode(ICommonConstDefine.SYS_ERROR_PARAM_NOT_VALID_COD);
		if (e.getBindingResult().getFieldError() != null) {
			
			entity.setErrMsg(e.getBindingResult().getFieldError().getDefaultMessage());
		} else if (e.getBindingResult().getGlobalError() != null) {
			
			entity.setErrMsg(e.getBindingResult().getGlobalError().getDefaultMessage());
		} else {
			
			entity.setErrMsg(ICommonConstDefine.SYS_ERROR_PARAM_NOT_VALID_MSG);
		}

		return new ResponseEntity<>(entity, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SurveyValidationException.class)
	public ResponseEntity<ExceptionEntity> surveyValidationExceptionHandler(
		SurveyValidationException e) {
		
		ExceptionEntity entity = new ExceptionEntity();
		entity.setErrCode(ICommonConstDefine.SYS_ERROR_DEFAULT_COD);
		entity.setErrMsg(e.getMessage());

		return new ResponseEntity<>(entity, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SurveyMsgSecCheckException.class)
	public ResponseEntity<ExceptionEntity> surveyMsgSecCheckExceptionHandler(
		SurveyMsgSecCheckException e) {
		
		ExceptionEntity entity = new ExceptionEntity();
		entity.setErrCode(ICommonConstDefine.SYS_ERROR_WECHAT_MSG_SEC_CHECK_COD);
		entity.setErrMsg(ICommonConstDefine.SYS_ERROR_WECHAT_MSG_SEC_CHECK_MSG);

		return new ResponseEntity<>(entity, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionEntity> exceptionHandler(Exception e) {
		
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement ste : e.getStackTrace()) {
			
			sb.append(String.format(" at %s \n", ste.toString()));
		}
		
		log.error(e.getMessage());
		log.error(sb.toString());
		
		ExceptionEntity entity = new ExceptionEntity();
		entity.setErrCode(ICommonConstDefine.SYS_ERROR_DEFAULT_COD);
		entity.setErrMsg(ICommonConstDefine.SYS_ERROR_DEFAULT_MSG);

		return new ResponseEntity<>(entity, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
