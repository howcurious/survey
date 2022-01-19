package cn.nbbandxdd.survey.common.wechat.accesstoken;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * <p>接口调用凭证生成器。
 *
 * <ul>
 * <li>获取接口调用凭证，使用 {@link #get()}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Slf4j
@Component
public class AccessTokenGenerator {

    /**
     * <p>获取小程序全局唯一后台接口调用凭据。
     */
    private final GetAccessToken getAccessToken;

    /**
     * <p>reactiveRedisOperations。
     */
    private final ReactiveRedisOperations<String, String> reactiveRedisOperations;

    /**
     * <p>构造器。
     *
     * @param getAccessToken 获取小程序全局唯一后台接口调用凭据
     * @param reactiveRedisOperations reactiveRedisOperations
     */
    public AccessTokenGenerator(GetAccessToken getAccessToken, ReactiveRedisOperations<String, String> reactiveRedisOperations) {

        this.getAccessToken = getAccessToken;
        this.reactiveRedisOperations = reactiveRedisOperations;
    }

    /**
     * <p>设置接口调用凭证。
     */
    @Scheduled(fixedDelay = 1800000L)
    public void set() {

        getAccessToken.get().flatMap(dto -> {

            if (dto.getErrcode() != null && !StringUtils.equals(dto.getErrcode(), ICommonConstDefine.WECHAT_ERRCODE_SUCCESS)) {

                log.error("获取微信接口校验凭证失败，{}：{}。", dto.getErrcode(), dto.getErrmsg());
            }

            if (StringUtils.isNotBlank(dto.getAccess_token())) {

                return reactiveRedisOperations.opsForValue().set(ICommonConstDefine.REDIS_KEY_ACCESS_TOKEN, dto.getAccess_token());
            }

            return Mono.empty();
        })
        .subscribeOn(Schedulers.immediate())
        .subscribe();
    }

    /**
     * <p>获取接口调用凭证。
     *
     * @return 接口调用凭证
     */
    public Mono<String> get() {

        return reactiveRedisOperations.opsForValue().get(ICommonConstDefine.REDIS_KEY_ACCESS_TOKEN);
    }
}
