package cn.nbbandxdd.survey.common.wechat.code2session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Code2Session {

	@Value("${wechat.app-id}")
	private String appid;

	@Value("${wechat.app-secret}")
	private String appsecret;

	private String url = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
	
	@Autowired
	private RestTemplate textPlainRestTemplate;
	
	@Value("${spring.profiles.active:dev}")
	private String activeProfile;
	
	public Code2SessionDTO get(String code) {
		
		if ("prod".equals(activeProfile)) {
			
			return textPlainRestTemplate.getForObject(
				String.format(url, appid, appsecret, code), Code2SessionDTO.class);
		}

		Code2SessionDTO dto = new Code2SessionDTO();
		dto.setOpenid(code);

		return dto;
	}
}
