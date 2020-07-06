package cn.nbbandxdd.survey.common.validation.category.examnotfinished;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.CommonUtils;

public class ExamNotFinishedValidator implements ConstraintValidator<ExamNotFinished, String> {

	@Override
	public void initialize(ExamNotFinished examFinished) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		return new Date().before(CommonUtils.fillbackEndTime(value));
	}
}
