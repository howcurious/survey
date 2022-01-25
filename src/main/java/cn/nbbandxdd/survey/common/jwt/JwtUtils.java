package cn.nbbandxdd.survey.common.jwt;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Jwt工具类。
 *
 * <ul>
 * <li>获取Jwt工具类单例，使用{@link #instance()}。</li>
 * <li>依据token生成openId，使用{@link #fillbackOpenidFromToken(String)}。</li>
 * <li>依据openId生成token，使用{@link #generateTokenFromOpenid(String)}。</li>
 * </ul>
 *
 * @author howcurious
 */
@Component
public class JwtUtils implements InitializingBean {

    private static JwtUtils instance;

    /**
     * <p>返回Jwt工具类单例。
     *
     * @return {@code JwtUtils}
     */
    public static JwtUtils instance() {

        return instance;
    }

    @Value("${jwt.key}")
    private String key;

    private Algorithm algorithm;

    /**
     * afterPropertiesSet。
     */
    @Override
    public void afterPropertiesSet() {

        algorithm = Algorithm.HMAC256(key);

        instance = this;
    }

    /**
     * <p>依据token生成openId。
     *
     * @param token Jwt
     * @return String openId
     */
    public static String fillbackOpenidFromToken(String token) {

        return JWT.decode(token).getSubject();
    }

    /**
     * <p>依据openId生成token。
     *
     * @param openid openId
     * @return String Jwt
     */
    public static String generateTokenFromOpenid(String openid) {

        return JWT.create()
            .withSubject(openid).withIssuedAt(new Date())
            .sign(instance().algorithm);
    }
}
