package cn.nbbandxdd.survey.common.validation.category.questypcd;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.ICommonConstDefine;

public class QuesTypCdValidator implements ConstraintValidator<QuesTypCd, String> {

	@Override
	public void initialize(QuesTypCd quesTypCd) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		return ICommonConstDefine.QUES_TYP_CD_SET.contains(value);
	}
}
