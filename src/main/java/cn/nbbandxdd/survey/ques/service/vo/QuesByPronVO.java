package cn.nbbandxdd.survey.ques.service.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.category.questypcd.QuesTypCd;
import cn.nbbandxdd.survey.common.validation.group.DeleteGroup;
import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import cn.nbbandxdd.survey.common.validation.group.UpdateGroup;
import lombok.Data;

@Data
public class QuesByPronVO {
	
	@NotBlank(groups = {SelectGroup.class, DeleteGroup.class, UpdateGroup.class},
		message = "题目编号不能为空。")
	@Size(min = 17, max = 17, groups = {SelectGroup.class, DeleteGroup.class, UpdateGroup.class},
		message = "题目编号格式错误。")
	private String quesCd;
	
	@QuesTypCd(groups = {InsertGroup.class, UpdateGroup.class},
		message = "题目类型错误。")
	private String typCd;
	
	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class},
		message = "请填写题目描述。")
	private String dsc;
	
	private String comm;
	
	@NotEmpty(groups = {InsertGroup.class, UpdateGroup.class},
		message = "选项不能为空。")
	private List<AnswVO> answList;
}
