package cn.nbbandxdd.survey.common.exception;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>异常统一处理。存在少量异常不进行统一处理，如：Jwt相关异常等。
 *
 * <ul>
 * <li>内部逻辑校验异常处理，使用{@link #surveyValidationExceptionHandler(SurveyValidationException)}。</li>
 * <li>微信小程序内容安全校验异常处理，使用{@link #surveyMsgSecCheckExceptionHandler()}。</li>
 * <li>原因不明异常处理，使用{@link #exceptionHandler(Exception)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Slf4j
@RestControllerAdvice
public class SurveyControllerAdvice {

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
     * @return {@code ResponseEntity<ExceptionEntity>}
     * @see SurveyMsgSecCheckException
     */
    @ExceptionHandler(SurveyMsgSecCheckException.class)
    public ResponseEntity<ExceptionEntity> surveyMsgSecCheckExceptionHandler() {

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
        StackTraceElement[] stes = e.getStackTrace();
        for (int i = 0; i < Math.min(stes.length, 10); ++i) {

            sb.append(String.format(" at %s \n", stes[i].toString()));
        }

        log.error(e.getMessage());
        log.error(sb.toString());

        ExceptionEntity entity = new ExceptionEntity();
        entity.setErrCode(ICommonConstDefine.SYS_ERROR_DEFAULT_COD);
        entity.setErrMsg(ICommonConstDefine.SYS_ERROR_DEFAULT_MSG);

        return new ResponseEntity<>(entity, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
