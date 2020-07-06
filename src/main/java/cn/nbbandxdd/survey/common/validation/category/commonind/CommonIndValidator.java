package cn.nbbandxdd.survey.common.validation.category.commonind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.ICommonConstDefine;

public class CommonIndValidator implements ConstraintValidator<CommonInd, String> {

	private boolean isMandatory;
	
	@Override
	public void initialize(CommonInd commonInd) {
		
		isMandatory = commonInd.isMandatory();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

		if (!isMandatory && null == value) {
			
			return true;
		}
		return ICommonConstDefine.COMMON_IND_SET.contains(value);
	}
}
