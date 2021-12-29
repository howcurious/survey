package cn.nbbandxdd.survey.common.wechat.accesstoken;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * <p>接口调用凭证生成器。
 *
 * <ul>
 * <li>获取接口调用凭证，使用 {@link #get()}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Component
public class AccessTokenGenerator {

    /**
     * <p>激活配置，默认：dev。
     */
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * <p>获取接口调用凭证。
     *
     * @return 接口调用凭证
     */
    public Mono<String> get() {

        if (!StringUtils.equals("prod", activeProfile)) {

            return Mono.just(StringUtils.EMPTY);
        }

        return WebClient.create()
            .get()
            .uri("http://accesstoken:8080/accesstoken/accesstoken/get")
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(20));
    }
}
