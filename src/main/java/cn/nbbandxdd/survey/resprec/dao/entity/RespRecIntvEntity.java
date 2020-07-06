package cn.nbbandxdd.survey.resprec.dao.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RespRecIntvEntity {

	String dprtNam;
	
	String lastMantUsr;
	
	Timestamp bgnTimeBgn;
	
	Timestamp bgnTimeEnd;
	
	Integer othIntCond;
}
