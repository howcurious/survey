package cn.nbbandxdd.survey.usrinfo.dao.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UsrInfoEntity {

	private String openId;
	
	private String dprtNam;
	
	private String grpNam;
	
	private String usrNam;
	
	private Timestamp regTmstp;
}
