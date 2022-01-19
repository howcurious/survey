package cn.nbbandxdd.survey.common.wechat.accesstoken;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * <p>获取小程序全局唯一后台接口调用凭据。
 *
 * <ul>
 * <li>获取小程序全局唯一后台接口调用凭据，使用 {@link #get()}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Component
public class GetAccessToken {

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

    /**
     * <p>获取小程序全局唯一后台接口调用凭据（access_token）。
     *
     * @return 接口调用凭证DTO
     */
    public Mono<GetAccessTokenDTO> get() {

        if (!StringUtils.equals("prod", activeProfile)) {

            GetAccessTokenDTO dto = new GetAccessTokenDTO();
            dto.setAccess_token("TEST_ACCESS_TOKEN");
            dto.setErrcode(ICommonConstDefine.WECHAT_ERRCODE_SUCCESS);

            return Mono.just(dto);
        }

        return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> {

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                configurer.customCodecs().registerWithDefaultConfig(new Jackson2JsonDecoder(mapper, MediaType.TEXT_PLAIN));
            }).build()).build()
            .get()
            .uri("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}",
                appId, appSecret)
            .retrieve()
            .bodyToMono(GetAccessTokenDTO.class)
            .timeout(Duration.ofSeconds(20));
    }
}
