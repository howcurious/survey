package cn.nbbandxdd.survey.common.validation.category.answcanbeseen;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.common.ICommonConstDefine;

/**
 * <p>依据问卷编号{@code examCd}校验问卷答案是否可见。
 * 
 * <p>校验逻辑：
 * <ol>
 * <li>如果问卷的立即展示答案标识{@code answImmInd}为是，则校验通过。</li>
 * <li>否则，如果当前时间晚于问卷的截止时间{@code endTime}，则校验通过。</li>
 * <li>否则校验不通过</li>
 * </ol>
 * 
 * @author howcurious
 * @see AnswCanBeSeen
 * @see cn.nbbandxdd.survey.exam.dao.entity.ExamEntity
 */
public class AnswCanBeSeenValidator implements ConstraintValidator<AnswCanBeSeen, String> {

	/**
	 * <p>initialize。
	 */
	@Override
	public void initialize(AnswCanBeSeen answCanBeSeen) {}

	/**
	 * <p>isValid。
	 */
	@Override
	public boolean isValid(String examCd, ConstraintValidatorContext constraintValidatorContext) {

		if (ICommonConstDefine.COMMON_IND_NO.equals(CommonUtils.fillbackAnswImmInd(examCd))) {
			
			return new Date().after(CommonUtils.fillbackEndTime(examCd));
		}
		return true;
	}
}
