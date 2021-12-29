package cn.nbbandxdd.survey.login.controller;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.exception.SurveyValidationException;
import cn.nbbandxdd.survey.common.jwt.JwtUtils;
import cn.nbbandxdd.survey.common.wechat.code2session.Code2Session;
import cn.nbbandxdd.survey.login.controller.vo.LoginVO;
import cn.nbbandxdd.survey.usrinfo.service.UsrInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * <p>登录Controller。
 *
 * <ul>
 * <li>登录，使用{@link #login(ServerRequest)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Configuration
public class LoginController {

    /**
     * <p>登录凭证校验。
     */
    private final Code2Session code2Session;

    /**
     * <p>用户信息Service。
     */
    private final UsrInfoService usrInfoService;

    /**
     * <p>构造器。
     *
     * @param code2Session 登录凭证校验
     * @param usrInfoService 用户信息Service
     */
    public LoginController(Code2Session code2Session, UsrInfoService usrInfoService) {

        this.code2Session = code2Session;
        this.usrInfoService = usrInfoService;
    }

    /**
     * <p>路由函数。
     */
    @Bean
    public RouterFunction<?> loginRouterFunction() {

        return route(POST("/login"), this::login);
    }

    /**
     * <p>登录。
     *
     * <p>输入字段：
     * <ul>
     * <li>{@code code}：临时登录凭证（必输），微信小程序端通过接口wx.login获得。校验失败情形：空白字段。</li>
     * </ul>
     *
     * <p>输出字段：
     * <ul>
     * <li>{@code token}：除登录接口外，其它接口在被调用前均校验token，微信小程序端需在请求报文头中添加“authorization: Bearer myToken”。</li>
     * <li>{@code isRegistered}：已注册标识。0：否，1：是。</li>
     * </ul>
     */
    private Mono<ServerResponse> login(ServerRequest request) {

        Mono<LoginVO> body = request.bodyToMono(LoginVO.class)
            .flatMap(one -> code2Session.get(one.getCode()))
            .flatMap(dto -> {

                if (!StringUtils.equals(ICommonConstDefine.WECHAT_ERRCODE_SUCCESS, dto.getErrcode())) {

                    return Mono.error(new SurveyValidationException(String.format("微信登录校验失败，%s：%s。", dto.getErrcode(), dto.getErrmsg())));
                }

                return usrInfoService.existsByKey(dto.getOpenid())
                    .map(isRegistered -> {

                        LoginVO loginVO = new LoginVO();
                        loginVO.setToken(JwtUtils.generateTokenFromOpenid(dto.getOpenid()));
                        loginVO.setIsRegistered(isRegistered);

                        return loginVO;
                    });
            });
        return ServerResponse.ok().body(body, LoginVO.class);
    }
}
