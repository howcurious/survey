package cn.nbbandxdd.survey.common.validation.category.examstart;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.CommonUtils;

public class ExamStartValidator implements ConstraintValidator<ExamStart, String> {

	@Override
	public void initialize(ExamStart examNotStart) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		return new Date().after(CommonUtils.fillbackBgnTime(value));
	}
}
