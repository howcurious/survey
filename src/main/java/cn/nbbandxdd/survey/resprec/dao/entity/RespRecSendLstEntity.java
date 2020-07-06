package cn.nbbandxdd.survey.resprec.dao.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RespRecSendLstEntity {

	private String openId;
	
	private String dprtNam;
	
	private String mailAddr;
	
	private String mailSubj;
	
	private Timestamp lastSendTmstp;
	
	private String errMsg;
}
