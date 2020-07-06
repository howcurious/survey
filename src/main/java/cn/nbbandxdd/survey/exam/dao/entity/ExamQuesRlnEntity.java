package cn.nbbandxdd.survey.exam.dao.entity;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class ExamQuesRlnEntity {

	private String examCd;
	
	private List<String> quesCds;
	
	private Integer seqNo;
	
	private String lastMantUsr;
	
	private String lastMantDat;
	
	private Timestamp lastMantTmstp;
}
