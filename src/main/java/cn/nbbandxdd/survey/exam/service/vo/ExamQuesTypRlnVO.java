package cn.nbbandxdd.survey.exam.service.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import cn.nbbandxdd.survey.common.validation.category.questypcd.QuesTypCd;
import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import lombok.Data;

@Data
public class ExamQuesTypRlnVO {

	@QuesTypCd(groups = InsertGroup.class, message = "题目类型错误。")
	private String quesTypCd;
	
	@Min(value = 0, groups = InsertGroup.class, message = "题目个数不小于0。")
	@Max(value = 100, groups = InsertGroup.class, message = "题目个数不大于100。")
	private Integer cnt;
	
	@Min(value = 0, groups = InsertGroup.class, message = "题目分值不小于0。")
	@Max(value = 100, groups = InsertGroup.class, message = "题目分值不大于100。")
	private Integer scre;
}
