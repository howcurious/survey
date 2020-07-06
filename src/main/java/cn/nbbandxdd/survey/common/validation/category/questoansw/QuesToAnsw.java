package cn.nbbandxdd.survey.common.validation.category.questoansw;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = QuesToAnswValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QuesToAnsw {

	String message() default "{cn.nbbandxdd.survey.common.validation.category.questoansw.QuesToAnsw.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
