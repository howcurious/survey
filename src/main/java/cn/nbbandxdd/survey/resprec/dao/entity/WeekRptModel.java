package cn.nbbandxdd.survey.resprec.dao.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class WeekRptModel {

	private String dprtNam;
	
	private Date first;
	
	private Date now;
	
	private List<RespRecExamStatEntity> examStat;
	
	private List<RespRecGrpStatEntity> grpStat;
	
	private List<RespRecUsrStatEntity> usrCntStat;
	
	private List<RespRecUsrStatEntity> usrAvgScreStat;
}
