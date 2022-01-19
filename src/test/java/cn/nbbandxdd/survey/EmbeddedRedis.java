package cn.nbbandxdd.survey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class EmbeddedRedis {

    private final RedisServer redisServer;

    public EmbeddedRedis(@Value("${spring.redis.port}") final int redisPort) {

        redisServer = new RedisServer(redisPort);
    }

    @PostConstruct
    public void startRedis() {

        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {

        redisServer.stop();
    }
}
