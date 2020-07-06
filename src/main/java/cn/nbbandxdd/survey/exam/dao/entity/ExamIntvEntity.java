package cn.nbbandxdd.survey.exam.dao.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ExamIntvEntity {

	String lastMantUsr;
	
	Timestamp bgnTimeBgn;
	
	Timestamp bgnTimeEnd;
}
