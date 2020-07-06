package cn.nbbandxdd.survey.common.wechat.accesstoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Profile("prod")
@Component
public class AccessTokenGenerator {

	private String url = "http://accesstoken:8080/accesstoken/accesstoken/get";

	@Autowired
	private RestTemplate textPlainRestTemplate;

	public String get() {

		return textPlainRestTemplate.getForEntity(url, String.class).getBody();
	}
}
