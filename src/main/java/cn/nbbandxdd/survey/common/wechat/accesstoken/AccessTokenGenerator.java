package cn.nbbandxdd.survey.common.wechat.accesstoken;

import cn.nbbandxdd.survey.common.ICommonConstDefine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;

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
     * <p>redisLockRegistry。
     */
    private final RedisLockRegistry redisLockRegistry;

    /**
     * <p>构造器。
     *
     * @param getAccessToken 获取小程序全局唯一后台接口调用凭据
     * @param reactiveRedisOperations reactiveRedisOperations
     * @param redisConnectionFactory redisConnectionFactory
     */
    public AccessTokenGenerator(GetAccessToken getAccessToken, ReactiveRedisOperations<String, String> reactiveRedisOperations, RedisConnectionFactory redisConnectionFactory) {

        this.getAccessToken = getAccessToken;
        this.reactiveRedisOperations = reactiveRedisOperations;
        this.redisLockRegistry = new RedisLockRegistry(redisConnectionFactory, ICommonConstDefine.REDIS_LOCK_ACCESS_TOKEN);
    }

    /**
     * <p>设置接口调用凭证。
     */
    @Scheduled(fixedDelay = 1800000L)
    public void set() {

        Lock lock = redisLockRegistry.obtain(ICommonConstDefine.REDIS_LOCK_ACCESS_TOKEN);

        lock.lock();
        try {

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            reactiveRedisOperations.opsForValue().get(ICommonConstDefine.REDIS_KEY_ACCESS_TOKEN_TIMESTAMP)
                .defaultIfEmpty(now.minusSeconds(3600L).format(pattern))
                .filter(stmp -> LocalDateTime.parse(stmp, pattern).plusSeconds(1800L).isBefore(now))
                .flatMap(stmp -> getAccessToken.get())
                .flatMap(dto -> {

                    if (dto.getErrcode() != null && !StringUtils.equals(dto.getErrcode(), ICommonConstDefine.WECHAT_ERRCODE_SUCCESS)) {

                        log.error("获取微信接口校验凭证失败，{}：{}。", dto.getErrcode(), dto.getErrmsg());
                        return Mono.empty();
                    }

                    return reactiveRedisOperations.opsForValue().set(ICommonConstDefine.REDIS_KEY_ACCESS_TOKEN, dto.getAccess_token());
                })
                .flatMap(one -> reactiveRedisOperations.opsForValue().set(ICommonConstDefine.REDIS_KEY_ACCESS_TOKEN_TIMESTAMP, now.format(pattern)))
                .subscribeOn(Schedulers.immediate())
                .subscribe();
        } finally {

            lock.unlock();
        }
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
