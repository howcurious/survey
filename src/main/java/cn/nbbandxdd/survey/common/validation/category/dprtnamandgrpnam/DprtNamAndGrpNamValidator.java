package cn.nbbandxdd.survey.common.validation.category.dprtnamandgrpnam;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.usrinfo.service.vo.UsrInfoVO;

public class DprtNamAndGrpNamValidator implements ConstraintValidator<DprtNamAndGrpNam, UsrInfoVO> {
	
	@Override
	public void initialize(DprtNamAndGrpNam dprtNamAndGrpNam) {}

	@Override
	public boolean isValid(UsrInfoVO value, ConstraintValidatorContext constraintValidatorContext) {

		if (StringUtils.isBlank(value.getDprtNam())) {
			
			value.setDprtNam(CommonUtils.guessDprtNam(value.getGrpNam()));
			if (StringUtils.isBlank(value.getDprtNam())) {
				
				return false;
			}
			return true;
		}
		
		return CommonUtils.validateGrpInfo(value.getDprtNam(), value.getGrpNam());
	}
}
