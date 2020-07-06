package cn.nbbandxdd.survey.ques.service.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.category.answcanbeseen.AnswCanBeSeen;
import cn.nbbandxdd.survey.common.validation.category.examanswered.ExamAnswered;
import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import lombok.Data;

@Data
public class QuesByExpVO {

	@NotBlank(groups = SelectGroup.class, message = "问卷编号不能为空。")
	@Size(min = 17, max = 17, groups = SelectGroup.class, message = "问卷编号格式错误。")
	@ExamAnswered(groups = SelectGroup.class, message = "问卷尚未作答。")
	@AnswCanBeSeen(groups = SelectGroup.class, message = "问卷答案尚不可见。")
	private String examCd;
	
	private String quesCd;
	
	private String typCd;
	
	private String dsc;
	
	private String comm;
	
	private List<AnswVO> answList;
	
	private List<String> selList;
}
