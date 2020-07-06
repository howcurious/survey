package cn.nbbandxdd.survey.resprec.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.category.examnotfinished.ExamNotFinished;
import cn.nbbandxdd.survey.common.validation.category.examstart.ExamStart;
import cn.nbbandxdd.survey.common.validation.category.examtoanswer.ExamToAnswer;
import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import lombok.Data;

@Data
public class RespRecVO {

	@NotBlank(groups = { InsertGroup.class, SelectGroup.class },
		message = "问卷编号不能为空。")
	@Size(min = 17, max = 17, groups = { InsertGroup.class, SelectGroup.class },
		message = "问卷编号格式错误。")
	@ExamStart(groups = InsertGroup.class, message = "问卷尚未开始。")
	@ExamNotFinished(groups = InsertGroup.class, message = "问卷已经结束。")
	@ExamToAnswer(groups = InsertGroup.class, message = "问卷不可重复作答。")
	private String examCd;
	
	private String usrNam;
	
	private String ttl;
	
	private Integer scre;
	
	private Integer spnd;
	
	private String dat;
}
