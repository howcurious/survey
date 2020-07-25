package cn.nbbandxdd.survey.common.validation.category.answcanbeseen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

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
 * @see AnswCanBeSeenValidator
 * @see cn.nbbandxdd.survey.exam.dao.entity.ExamEntity
 */
@Constraint(validatedBy = AnswCanBeSeenValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnswCanBeSeen {

	/**
	 * <p>message。
	 * 
	 * @return String 校验失败信息
	 */
	String message() default "{cn.nbbandxdd.survey.common.validation.category.answcanbeseen.AnswCanBeSeen.message}";

	/**
	 * <p>groups。
	 * 
	 * @return Class<?>[] 校验组
	 */
	Class<?>[] groups() default {};

	/**
	 * <p>payload。
	 * 
	 * @return Class<? extends javax.validation.Payload>[] payload
	 */
	Class<? extends Payload>[] payload() default {};
}
