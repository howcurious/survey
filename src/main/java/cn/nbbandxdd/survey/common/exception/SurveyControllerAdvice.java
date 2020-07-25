package cn.nbbandxdd.survey.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>异常统一处理。存在少量异常不进行统一处理，如：Jwt相关异常等。
 * 
 * <ul>
 * <li>请求报文参数校验异常处理，使用{@link #methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException)}。</li>
 * <li>内部逻辑校验异常处理，使用{@link #surveyValidationExceptionHandler(SurveyValidationException)}。</li>
 * <li>微信小程序内容安全校验异常处理，使用{@link #surveyMsgSecCheckExceptionHandler(SurveyMsgSecCheckException)}。</li>
 * <li>原因不明异常处理，使用{@link #exceptionHandler(Exception)}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@Slf4j
@ControllerAdvice
public class SurveyControllerAdvice {

	/**
	 * <p>请求报文参数校验异常处理。
	 * 当存在field error时，返回field error错误信息；否则，当存在global error时，返回global error错误信息；否则返回默认错误信息。
	 * 
	 * @param e {@code MethodArgumentNotValidException}
	 * @return {@code ResponseEntity<ExceptionEntity>}
	 * @see MethodArgumentNotValidException
	 */
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
	
	/**
	 * <p>内部逻辑校验异常处理。
	 * 
	 * @param e {@code SurveyValidationException}
	 * @return {@code ResponseEntity<ExceptionEntity>}
	 * @see SurveyValidationException
	 */
	@ExceptionHandler(SurveyValidationException.class)
	public ResponseEntity<ExceptionEntity> surveyValidationExceptionHandler(
		SurveyValidationException e) {
		
		ExceptionEntity entity = new ExceptionEntity();
		entity.setErrCode(ICommonConstDefine.SYS_ERROR_DEFAULT_COD);
		entity.setErrMsg(e.getMessage());

		return new ResponseEntity<>(entity, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * <p>微信小程序内容安全校验异常处理。
	 * 
	 * @param e {@code SurveyMsgSecCheckException}
	 * @return {@code ResponseEntity<ExceptionEntity>}
	 * @see SurveyMsgSecCheckException
	 */
	@ExceptionHandler(SurveyMsgSecCheckException.class)
	public ResponseEntity<ExceptionEntity> surveyMsgSecCheckExceptionHandler(
		SurveyMsgSecCheckException e) {
		
		ExceptionEntity entity = new ExceptionEntity();
		entity.setErrCode(ICommonConstDefine.SYS_ERROR_WECHAT_MSG_SEC_CHECK_COD);
		entity.setErrMsg(ICommonConstDefine.SYS_ERROR_WECHAT_MSG_SEC_CHECK_MSG);

		return new ResponseEntity<>(entity, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * <p>原因不明异常处理。为排查原因不明的异常，在服务器端打印错误日志。
	 * 
	 * @param e {@code Exception}
	 * @return {@code ResponseEntity<ExceptionEntity>}
	 */
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
