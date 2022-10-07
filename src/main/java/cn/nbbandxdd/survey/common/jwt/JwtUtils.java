package cn.nbbandxdd.survey.common.jwt;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private Long expiration;

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

    public static String getUserNameFromToken(String token) {
        // 1. 先验证签名
        JWTVerifier verifier = JWT.require(instance().algorithm).withIssuer(instance().issuer).build();
        verifier.verify(token);

        // 2. 比较过期时间
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expireDate = decodedJWT.getExpiresAt();
        if (expireDate.before(new Date())) {
            log.error("token已经过期");
            throw new JWTDecodeException("token过期");
        }
        return decodedJWT.getSubject();
    }

    public static String generateTokenFromUserName(String userName) {
        return JWT.create()
                .withSubject(userName).withIssuer(instance().issuer).withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + instance().expiration*1000))
                .sign(instance().algorithm);
    }
}
