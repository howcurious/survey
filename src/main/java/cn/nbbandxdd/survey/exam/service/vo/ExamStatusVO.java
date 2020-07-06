package cn.nbbandxdd.survey.exam.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import lombok.Data;

@Data
public class ExamStatusVO {

	@NotBlank(groups = SelectGroup.class, message = "问卷编号不能为空。")
	@Size(min = 17, max = 17, groups = SelectGroup.class,
		message = "问卷编号格式错误。")
	private String examCd;
	
	private String status;
}
