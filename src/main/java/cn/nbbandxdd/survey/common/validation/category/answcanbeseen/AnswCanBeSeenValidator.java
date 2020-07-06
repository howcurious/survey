package cn.nbbandxdd.survey.common.validation.category.answcanbeseen;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.common.ICommonConstDefine;

public class AnswCanBeSeenValidator implements ConstraintValidator<AnswCanBeSeen, String> {

	@Override
	public void initialize(AnswCanBeSeen answCanBeSeen) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		if (ICommonConstDefine.COMMON_IND_NO.equals(CommonUtils.fillbackAnswImmInd(value))) {
			
			return new Date().after(CommonUtils.fillbackEndTime(value));
		}
		return true;
	}
}
