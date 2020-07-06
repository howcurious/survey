package cn.nbbandxdd.survey.pubserno.dao.entity;

import lombok.Data;

@Data
public class PubSerNoEntity {

	private String serNoTyp;
	
	private Integer bgnSerNo;

	private Integer curSerNo;
	
	private Integer endSerNo;
	
	private Integer stpSprd;
	
	private Integer fmtOutLen;
}
