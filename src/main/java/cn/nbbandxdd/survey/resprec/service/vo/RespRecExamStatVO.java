package cn.nbbandxdd.survey.resprec.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import lombok.Data;

@Data
public class RespRecExamStatVO {

	@NotBlank(groups = {SelectGroup.class}, message = "问卷编号不能为空。")
	@Size(min = 17, max = 17, groups = {SelectGroup.class},
		message = "问卷编号格式错误。")
	private String examCd;
	
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
