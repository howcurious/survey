package cn.nbbandxdd.survey.common.validation.category.questypcd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = QuesTypCdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QuesTypCd {

	String message() default "{cn.nbbandxdd.survey.common.validation.category.questypcd.QuesTypCd.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
