package cn.nbbandxdd.survey.common.jwt;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils implements InitializingBean {

	private static JwtUtils instance;
	
	public static JwtUtils instance() {
		
		return instance;
	}
	
	@Value("${jwt.key}")
	private String key;
	
	private SecretKey secretKey;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");

		instance = this;
	}
	
	public static String fillbackOpenidFromToken(String token) {
		
		return Jwts
			.parserBuilder().setSigningKey(instance().secretKey).build()
			.parseClaimsJws(token).getBody().getSubject();
	}
	
	public static String generateTokenFromOpenid(String openid) {
		
		return Jwts.builder()
			.setSubject(openid).setIssuedAt(new Date())
			.signWith(instance().secretKey, SignatureAlgorithm.HS256)
			.compact();
	}
}
