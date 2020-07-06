package cn.nbbandxdd.survey.exam.service.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.category.listelement.ListElement;
import cn.nbbandxdd.survey.common.validation.group.DeleteGroup;
import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import lombok.Data;

@Data
public class ExamQuesRlnVO {

	@NotBlank(groups = {InsertGroup.class, DeleteGroup.class, SelectGroup.class},
		message = "问卷编号不能为空。")
	@Size(min = 17, max = 17, groups = {InsertGroup.class, DeleteGroup.class, SelectGroup.class},
		message = "问卷编号格式错误。")
	private String examCd;
	
	@ListElement(min = 17, max = 17, groups = {InsertGroup.class, DeleteGroup.class},
		message = "题目编号格式错误。")
	private List<String> quesCds;
}
