package cn.nbbandxdd.survey.grpinfo.service.vo;

import javax.validation.constraints.NotBlank;

import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import lombok.Data;

@Data
public class GrpInfoVO {

	@NotBlank(groups = SelectGroup.class, message = "部门名不能为空。")
	private String dprtNam;
	
	private String grpNam;
}
