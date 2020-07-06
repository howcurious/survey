package cn.nbbandxdd.survey.common.validation.category.examtypcd;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.ICommonConstDefine;

public class ExamTypCdValidator implements ConstraintValidator<ExamTypCd, String> {

	@Override
	public void initialize(ExamTypCd examTypCd) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		return null == value || ICommonConstDefine.EXAM_TYP_CD_SET.contains(value);
	}
}
