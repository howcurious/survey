package cn.nbbandxdd.survey.resprec.dao.entity;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class DtlRecEntity {

	private String openId;
	
	private String examCd;
	
	private String quesCd;
	
	private String answCd;
	
	private List<String> answList;
	
	private Integer scre;
	
	private String lastInd;
	
	private Integer respScre;
	
	private Integer respSpnd;
	
	private Timestamp lastMantTmstp;
}
