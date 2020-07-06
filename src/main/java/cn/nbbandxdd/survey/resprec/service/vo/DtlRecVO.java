package cn.nbbandxdd.survey.resprec.service.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.category.commonind.CommonInd;
import cn.nbbandxdd.survey.common.validation.category.examnotfinished.ExamNotFinished;
import cn.nbbandxdd.survey.common.validation.category.examstart.ExamStart;
import cn.nbbandxdd.survey.common.validation.category.listelement.ListElement;
import cn.nbbandxdd.survey.common.validation.category.questoansw.QuesToAnsw;
import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import lombok.Data;

@QuesToAnsw(groups = InsertGroup.class, message = "题目不可重复作答。")
@Data
public class DtlRecVO {
	
	@NotBlank(groups = InsertGroup.class, message = "问卷编号不能为空。")
	@Size(min = 17, max = 17, groups = InsertGroup.class,
		message = "问卷编号格式错误。")
	@ExamStart(groups = InsertGroup.class, message = "问卷尚未开始。")
	@ExamNotFinished(groups = InsertGroup.class, message = "问卷已经结束。")
	private String examCd;
	
	@NotBlank(groups = InsertGroup.class, message = "题目编号不能为空。")
	@Size(min = 17, max = 17, groups = InsertGroup.class,
		message = "题目编号格式错误。")
	private String quesCd;
	
	@NotNull(groups = InsertGroup.class, message = "选项编号不能为空。")
	@ListElement(min = 0, max = 2, groups = InsertGroup.class,
		message = "选项编号格式错误。")
	private List<String> answList;

	@CommonInd(isMandatory = false, groups = InsertGroup.class, message = "最后一题标识格式错误。")
	private String lastInd;
	
	private Integer respScre;
	
	private Integer respSpnd;
}
