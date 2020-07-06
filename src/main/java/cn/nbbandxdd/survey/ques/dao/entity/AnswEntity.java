package cn.nbbandxdd.survey.ques.dao.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AnswEntity {

	private String quesCd;
	
	private String answCd;
	
	private String typCd;
	
	private String dsc;
	
	private Integer scre;
	
	private String lastMantUsr;
	
	private String lastMantDat;
	
	private Timestamp lastMantTmstp;
}
