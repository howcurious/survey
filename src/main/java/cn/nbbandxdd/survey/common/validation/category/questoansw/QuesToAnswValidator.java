package cn.nbbandxdd.survey.common.validation.category.questoansw;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.resprec.service.vo.DtlRecVO;

public class QuesToAnswValidator implements ConstraintValidator<QuesToAnsw, DtlRecVO> {

	@Override
	public void initialize(QuesToAnsw quesCompleted) {}

	@Override
	public boolean isValid(DtlRecVO value, ConstraintValidatorContext constraintValidatorContext) {

		return !CommonUtils.validateDtlRec(value.getExamCd(), value.getQuesCd()) ||
			ICommonConstDefine.COMMON_IND_YES.equals(CommonUtils.fillbackRpetInd(value.getExamCd()));
	}
}
