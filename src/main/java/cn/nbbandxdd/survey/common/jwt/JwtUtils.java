package cn.nbbandxdd.survey.common.jwt;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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

    private SecretKey secretKey;

    /**
     * afterPropertiesSet。
     */
    @Override
    public void afterPropertiesSet() {

        secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");

        instance = this;
    }

    /**
     * <p>依据token生成openId。
     *
     * @param token Jwt
     * @return String openId
     */
    public static String fillbackOpenidFromToken(String token) {

        return Jwts
            .parserBuilder().setSigningKey(instance().secretKey).build()
            .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * <p>依据openId生成token。
     *
     * @param openid openId
     * @return String Jwt
     */
    public static String generateTokenFromOpenid(String openid) {

        return Jwts.builder()
            .setSubject(openid).setIssuedAt(new Date())
            .signWith(instance().secretKey, SignatureAlgorithm.HS256)
            .compact();
    }
}
