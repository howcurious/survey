package cn.nbbandxdd.survey.common.exception;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>异常统一处理。存在少量异常不进行统一处理，如：Jwt相关异常等。
 *
 * @author howcurious
 */
@Slf4j
@Component
public class SurveyErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * <p>objectMapper。
     */
    private final ObjectMapper objectMapper;

    /**
     * <p>构造器。
     *
     * @param objectMapper objectMapper
     */
    public SurveyErrorWebExceptionHandler(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    /**
     * <p>handle。
     *
     * @param exchange exchange
     * @param ex ex
     * @return 无
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBufferFactory factory = exchange.getResponse().bufferFactory();
        DataBuffer buffer;
        if (ex instanceof SurveyTokenException) {

            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

            try {

                buffer = factory.wrap(objectMapper.writeValueAsBytes(new ExceptionEntity(ICommonConstDefine.SYS_ERROR_JWT_NOT_VALID_COD, ICommonConstDefine.SYS_ERROR_JWT_NOT_VALID_MSG)));
            } catch (JsonProcessingException e) {

                buffer = factory.wrap("SurveyTokenException".getBytes());
            }
        } else if (ex instanceof SurveyValidationException) {

            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

            try {

                buffer = factory.wrap(objectMapper.writeValueAsBytes(new ExceptionEntity(ICommonConstDefine.SYS_ERROR_PARAM_NOT_VALID_COD, ex.getMessage())));
            } catch (JsonProcessingException e) {

                buffer = factory.wrap("SurveyValidationException".getBytes());
            }
        } else if (ex instanceof  SurveyMsgSecCheckException) {

            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

            try {

                buffer = factory.wrap(objectMapper.writeValueAsBytes(new ExceptionEntity(ICommonConstDefine.SYS_ERROR_WECHAT_MSG_SEC_CHECK_COD, ICommonConstDefine.SYS_ERROR_WECHAT_MSG_SEC_CHECK_MSG)));
            } catch (JsonProcessingException e) {

                buffer = factory.wrap("SurveyMsgSecCheckException".getBytes());
            }
        } else {

            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

            try {

                buffer = factory.wrap(objectMapper.writeValueAsBytes(new ExceptionEntity(ICommonConstDefine.SYS_ERROR_DEFAULT_COD, ICommonConstDefine.SYS_ERROR_DEFAULT_MSG)));
            } catch (JsonProcessingException e) {

                buffer = factory.wrap("UnknownException".getBytes());
            }

            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stes = ex.getStackTrace();
            for (int i = 0; i < Math.min(stes.length, 10); ++i) {

                sb.append(String.format(" at %s \n", stes[i].toString()));
            }

            log.error(ex.getMessage());
            log.error(sb.toString());
        }

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
