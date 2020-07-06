package cn.nbbandxdd.survey.exam.dao.entity;

import java.sql.Timestamp;
import java.util.List;

import cn.nbbandxdd.survey.ques.dao.entity.QuesByRespEntity;
import lombok.Data;

@Data
public class ExamEntity {

	private String examCd;
	
	private String typCd;
	
	private String rpetInd;
	
	private String cntdwnInd;
	
	private String answImmInd;

	private String ttl;
	
	private String dsc;
	
	private Timestamp bgnTime;
	
	private Timestamp endTime;
	
	private Integer seqNo;
	
	private Integer quesCnt;
	
	private String lastMantUsr;
	
	private String lastMantDat;
	
	private Timestamp lastMantTmstp;
	
	private List<QuesByRespEntity> quesList;
	
	private List<ExamQuesTypRlnEntity> quesTypRlnList;
}
