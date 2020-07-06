package cn.nbbandxdd.survey.common.wechat.msgseccheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cn.nbbandxdd.survey.common.wechat.accesstoken.AccessTokenGenerator;
import lombok.Data;

@Profile("prod")
@Component
public class MsgSecCheck {

	private String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=%s";
	
	@Autowired
	private RestTemplate textPlainRestTemplate;
	
	@Autowired
	private AccessTokenGenerator accessTokenGenerator;
	
	public MsgSecCheckDTO get(String content) {
		
		MsgSecCheckReq req = new MsgSecCheckReq();
		req.setContent(content);
		
		return textPlainRestTemplate.postForObject(
			String.format(url, accessTokenGenerator.get()), req, MsgSecCheckDTO.class);
	}
	
	@Data
	class MsgSecCheckReq {
		
		private String content;
	}
}
