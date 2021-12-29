package cn.nbbandxdd.survey.common.wechat.msgseccheck;

import cn.nbbandxdd.survey.common.wechat.accesstoken.AccessTokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * <p>检查一段文本是否含有违法违规内容。
 *
 * <ul>
 * <li>获取检查一段文本是否含有违法违规内容结果，使用 {@link #get(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Component
public class MsgSecCheck {

    /**
     * <p>激活配置，默认：dev。
     */
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * <p>接口调用凭证生成器。
     */
    private final AccessTokenGenerator accessTokenGenerator;

    /**
     * <p>构造器。
     *
     * @param accessTokenGenerator 接口调用凭证生成器
     */
    public MsgSecCheck(AccessTokenGenerator accessTokenGenerator) {

        this.accessTokenGenerator = accessTokenGenerator;
    }

    /**
     * <p>获取检查一段文本是否含有违法违规内容结果。
     *
     * @param content 需检测的文本内容
     * @return 检查一段文本是否含有违法违规内容DTO
     */
    public Mono<MsgSecCheckDTO> get(String content) {

        if (!StringUtils.equals("prod", activeProfile)) {

            MsgSecCheckDTO dto = new MsgSecCheckDTO();
            dto.setErrcode("0");

            return Mono.just(dto);
        }

        return accessTokenGenerator.get().flatMap(accessToken -> {

            MsgSecCheckDTO dto = new MsgSecCheckDTO();
            dto.setContent(content);

            return WebClient.create()
                .post()
                .uri("https://api.weixin.qq.com/wxa/msg_sec_check?access_token={accessToken}", accessToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(MsgSecCheckDTO.class)
                .timeout(Duration.ofSeconds(20));
        });
    }
}
