package cn.nbbandxdd.survey.resprec.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import lombok.Data;

@Data
public class RespRecUsrRankVO {

	private String dprtNam;
	
	private String grpNam;
	
	private String usrNam;
	
	@NotBlank(groups = SelectGroup.class, message = "问卷编号不能为空。")
	@Size(min = 17, max = 17, groups = SelectGroup.class,
		message = "问卷编号格式错误。")
	private String examCd;

	private Integer scre;
	
	private Integer spnd;
	
	private String dat;
}
