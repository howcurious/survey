package cn.nbbandxdd.survey.ques.service.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.UpdateGroup;
import lombok.Data;

@Data
public class AnswVO {

	private String quesCd;
	
	private String answCd;
	
	@JsonIgnore
	private String typCd;
	
	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class},
		message = "请填写选项描述。")
	private String dsc;
	
	@Min(value = 0, groups = {InsertGroup.class, UpdateGroup.class},
		message = "选项分值至少为0。")
	@Max(value = 100, groups = {InsertGroup.class, UpdateGroup.class},
		message = "选项分值至多为100。")
	private Integer scre;
}
