package cn.nbbandxdd.survey.common.validation.category.examanswered;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.CommonUtils;

public class ExamAnsweredValidator implements ConstraintValidator<ExamAnswered, String> {

	@Override
	public void initialize(ExamAnswered examCompleted) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		return CommonUtils.validateRespRec(value);
	}
}
