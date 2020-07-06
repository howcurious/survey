package cn.nbbandxdd.survey.ques.service.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import lombok.Data;

@Data
public class QuesByRespVO {
	
	@NotBlank(groups = SelectGroup.class, message = "题目编号不能为空。")
	@Size(min = 17, max = 17, groups = SelectGroup.class, message = "题目编号格式错误。")
	private String quesCd;
	
	private String typCd;
	
	private String dsc;
	
	private List<AnswVO> answList;
}
