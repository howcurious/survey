package cn.nbbandxdd.survey.common.wechat.code2session;

import cn.nbbandxdd.survey.admin_user.repository.AdminUserRepository;
import cn.nbbandxdd.survey.admin_user.repository.entity.AdminUserInfoEntity;
import cn.nbbandxdd.survey.admin_user.service.AdminUserService;
import cn.nbbandxdd.survey.common.ICommonConstDefine;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * <p>登录凭证校验。
 *
 * <ul>
 * <li>获取登录凭证校验结果，使用 {@link #get(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Component
public class Code2Session {

    /**
     * <p>小程序appId。
     */
    @Value("${wechat.app-id}")
    private String appId;

    /**
     * <p>小程序appSecret。
     */
    @Value("${wechat.app-secret}")
    private String appSecret;

    /**
     * <p>激活配置，默认：dev。
     */
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Autowired
    private AdminUserService adminUserService;

    /**
     * <p>获取登录凭证校验结果。
     *
     * @param jsCode 登录时获取的code
     * @return 登录凭证校验DTO
     */
    public Mono<Code2SessionDTO> get(String jsCode) {

        if (!StringUtils.equals("prod", activeProfile)) {

            Code2SessionDTO dto = new Code2SessionDTO();
            dto.setOpenid(jsCode);
            dto.setErrcode(ICommonConstDefine.WECHAT_ERRCODE_SUCCESS);

            return Mono.just(dto);
        }

        return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> {

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                configurer.customCodecs().registerWithDefaultConfig(new Jackson2JsonDecoder(mapper, MediaType.TEXT_PLAIN));
            }).build()).build()
            .get()
            .uri("https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={appSecret}&js_code={jsCode}&grant_type=authorization_code",
                appId, appSecret, jsCode)
            .retrieve()
            .bodyToMono(Code2SessionDTO.class)
            .timeout(Duration.ofSeconds(20));
    }

    public Mono<Code2SessionDTO> loginByNameAndPsw(String userName, String password) {
        return adminUserService.getAdminUserByName(userName).
                flatMap(en -> Mono.just(BCrypt.checkpw(password, en.getUserPwd())))
                .map(valid -> {
                    Code2SessionDTO dto = new Code2SessionDTO();
                    if (valid) {
                        dto.setAdminUserName(userName);
                        dto.setErrcode(ICommonConstDefine.WECHAT_ERRCODE_SUCCESS);
                    } else {
                        dto.setErrcode(ICommonConstDefine.ADMIN_LOGIN_ERROR);
                        dto.setErrmsg("用户名或密码错误");
                    }
                    return dto;
                });
    }
}
