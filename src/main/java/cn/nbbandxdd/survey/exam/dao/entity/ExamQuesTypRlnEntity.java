package cn.nbbandxdd.survey.exam.dao.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ExamQuesTypRlnEntity {

	private String examCd;
	
	private String quesTypCd;
	
	private Integer cnt;
	
	private Integer scre;
	
	private String lastMantUsr;
	
	private String lastMantDat;
	
	private Timestamp lastMantTmstp;
}
