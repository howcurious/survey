package cn.nbbandxdd.survey.usrinfo.service.vo;

import javax.validation.constraints.NotBlank;

import cn.nbbandxdd.survey.common.validation.category.dprtnamandgrpnam.DprtNamAndGrpNam;
import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.UpdateGroup;
import lombok.Data;

@Data
@DprtNamAndGrpNam(groups = {InsertGroup.class, UpdateGroup.class},
	message = "部门或职能组错误。")
public class UsrInfoVO {

	private String dprtNam;
	
	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class},
		message = "请选择职能组。")
	private String grpNam;
	
	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class},
		message = "请填写姓名。")
	private String usrNam;
}
