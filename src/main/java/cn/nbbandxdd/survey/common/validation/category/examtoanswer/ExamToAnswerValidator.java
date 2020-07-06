package cn.nbbandxdd.survey.common.validation.category.examtoanswer;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.common.ICommonConstDefine;

public class ExamToAnswerValidator implements ConstraintValidator<ExamToAnswer, String> {

	@Override
	public void initialize(ExamToAnswer examCompleted) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		return !CommonUtils.validateRespRec(value) ||
			ICommonConstDefine.COMMON_IND_YES.equals(CommonUtils.fillbackRpetInd(value));
	}
}
