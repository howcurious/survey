package cn.nbbandxdd.survey.resprec.dao.entity;

import lombok.Data;

@Data
public class RespRecGrpStatEntity {

	private String examCd;
	
	private Integer examCnt;
	
	private String dprtNam;
	
	private String grpNam;
	
	private Integer cnt;
	
	private Integer totCnt;
	
	private Double ptpnRate;
	
	private Double avgScre;
	
	private Double avgSpnd;
}
