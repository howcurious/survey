package cn.nbbandxdd.survey.exam.service.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.nbbandxdd.survey.common.validation.category.commonind.CommonInd;
import cn.nbbandxdd.survey.common.validation.category.examtypcd.ExamTypCd;
import cn.nbbandxdd.survey.common.validation.group.DeleteGroup;
import cn.nbbandxdd.survey.common.validation.group.InsertGroup;
import cn.nbbandxdd.survey.common.validation.group.SelectGroup;
import cn.nbbandxdd.survey.common.validation.group.UpdateGroup;
import cn.nbbandxdd.survey.ques.service.vo.QuesByRespVO;
import lombok.Data;

@Data
public class ExamVO {

	@NotBlank(groups = {SelectGroup.class, DeleteGroup.class, UpdateGroup.class},
		message = "问卷编号不能为空。")
	@Size(min = 17, max = 17, groups = {SelectGroup.class, DeleteGroup.class, UpdateGroup.class},
		message = "问卷编号格式错误。")
	private String examCd;
	
	@ExamTypCd(groups = InsertGroup.class, message = "问卷类型格式错误。")
	private String typCd;
	
	@CommonInd(groups = {InsertGroup.class, UpdateGroup.class},
		message = "请选择是否可重复作答。")
	private String rpetInd;
	
	@CommonInd(isMandatory = false, groups = {InsertGroup.class, UpdateGroup.class},
		message = "请选择是否对每道题进行一分钟倒计时。")
	private String cntdwnInd;

	@CommonInd(isMandatory = false, groups = {InsertGroup.class, UpdateGroup.class},
		message = "请选择是否在作答后立刻显示正确答案。")
	private String answImmInd;

	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class},
		message = "请填写问卷题目。")
	private String ttl;
	
	private String dsc;
	
	private Long bgnTime;
	
	private Long endTime;
	
	private Integer quesCnt;
	
	private List<QuesByRespVO> quesList;
	
	private List<ExamQuesTypRlnVO> quesTypRlnList;
}
