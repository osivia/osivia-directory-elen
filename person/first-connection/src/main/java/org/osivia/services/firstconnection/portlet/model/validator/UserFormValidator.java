package org.osivia.services.firstconnection.portlet.model.validator;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.firstconnection.portlet.model.UserForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class UserFormValidator implements Validator {

    /** Password minimun length. */
    private static final int PASSWORD_MIN_LENGTH = 6;


    /**
     * Constructor.
     */
    public UserFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return UserForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
    	
        // User form
        UserForm form = (UserForm) target;
    	
        ValidationUtils.rejectIfEmpty(errors, "firstName", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "lastName", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "email", "NotEmpty");
        
        boolean emailValide = Pattern.matches(
				"^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", form.getEmail());
		if (!emailValide) {
			errors.rejectValue("email", "wrong.mail");
		}
        
        if(form.isMustChangePassword()) {
        
	        ValidationUtils.rejectIfEmpty(errors, "password", "NotEmpty");
	        ValidationUtils.rejectIfEmpty(errors, "passwordConfirmation", "NotEmpty");
	
	        if (StringUtils.isNotEmpty(form.getPassword()) && (form.getPassword().length() < PASSWORD_MIN_LENGTH)) {
	            errors.rejectValue("password", "TooShort", new Object[]{PASSWORD_MIN_LENGTH}, null);
	        }
	        if (StringUtils.isNotEmpty(form.getPasswordConfirmation()) && !StringUtils.equals(form.getPassword(), form.getPasswordConfirmation())) {
	            errors.rejectValue("passwordConfirmation", "Unmatching");
	        }
        }
    }

}
