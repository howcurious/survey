package cn.nbbandxdd.survey.resprec.dao.entity;

import java.util.Date;

import lombok.Data;

@Data
public class RespRecExamStatEntity {

	private String examCd;
	
	private Date bgnTime;
	
	private String ttl;
	
	private Double avgScre;
	
	private Double avgSpnd;
	
	private Integer cnt;
	
	private Integer cntU40;
	
	private Double rateU40;
	
	private Integer cntU70;
	
	private Double rateU70;
	
	private Integer cntU100;
	
	private Double rateU100;
	
	private Integer cnt100;
	
	private Double rate100;
}
