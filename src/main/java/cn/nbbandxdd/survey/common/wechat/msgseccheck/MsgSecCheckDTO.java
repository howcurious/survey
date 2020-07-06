package cn.nbbandxdd.survey.common.wechat.msgseccheck;

import lombok.Data;

@Data
public class MsgSecCheckDTO {

	private String content;
	
	private String errcode;
	
	private String errMsg;
}
