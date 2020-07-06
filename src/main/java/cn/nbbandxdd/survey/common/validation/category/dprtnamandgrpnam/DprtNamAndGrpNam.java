package cn.nbbandxdd.survey.common.validation.category.dprtnamandgrpnam;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DprtNamAndGrpNamValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DprtNamAndGrpNam {

	String message() default "{cn.nbbandxdd.survey.common.validation.category.dprtnamandgrpnam.DprtNamAndGrpNam.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
