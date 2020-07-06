package cn.nbbandxdd.survey.common.wechat.code2session;

import lombok.Data;

@Data
public class Code2SessionDTO {

	private String openid;
	
	private String session_key;
	
	private String errcode;
	
	private String errmsg;
}
