package com.gcmassari.tempmonitor.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.gcmassari.tempmonitor.model.EvaluationParams;

@Component
public class EvalParametersValidator  implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return EvaluationParams.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final String cantBeEmptyError = "Can't be empty";
//		if (errors.hasFieldErrors()) {
//			List<FieldError> fieldErrors = errors.getFieldErrors();
//			for (FieldError fError : fieldErrors) {
//				String code = fError.getCode();
//				System.out.println("--> ***** code="+ code);
//			}
//			errors.rejectValue(
//					"maxTemp",
//					"evalparams.minTempNotLessThanMax",
//					"My exception"
//					);
//		}
		EvaluationParams params = (EvaluationParams) target;
		
		// Value should be present
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "minTemp", "evalparams.emptyField", cantBeEmptyError);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maxTemp", "evalparams.emptyField", cantBeEmptyError);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ignoreDataBefore", "evalparams.emptyField", cantBeEmptyError);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ignoreDataAfter", "evalparams.emptyField", cantBeEmptyError);
		if (errors.hasErrors()) {
			return; // no more error messages
		}
		
		if (params.getMinTemp().compareTo(params.getMaxTemp()) >= 0) {
			errors.rejectValue(
					"minTemp",
					"evalparams.minTempNotLessThanMax",
					"Min temp should be less than max temp"
			);
			errors.rejectValue(
					"maxTemp",
					"evalparams.minTempNotLessThanMax",
					"Min temp should be less than max temp"
					);
		}
	}
	
}
