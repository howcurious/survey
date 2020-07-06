package cn.nbbandxdd.survey.common.validation.category.listelement;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class ListElementValidator implements ConstraintValidator<ListElement, List<String>> {

	private int min;
	
	private int max;
	
	@Override
	public void initialize(ListElement commonElement) {
		
		min = commonElement.min();
		max = commonElement.max();
	}

	@Override
	public boolean isValid(List<String> value, ConstraintValidatorContext constraintValidatorContext) {

		if (null == value) {
			
			return false;
		}
		
		for (String one : value) {
			
			int len = StringUtils.length(one);
			if (len < min || len > max) {
				
				return false;
			}
		}
		
		return true;
	}
}
