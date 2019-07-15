package org.osivia.services.firstconnection.portlet.model.validator;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.firstconnection.portlet.model.UserForm;
import org.osivia.services.firstconnection.portlet.service.FirstConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * User form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class UserFormValidator implements Validator {

    /**
     * Password minimun length.
     */
    private static final int PASSWORD_MIN_LENGTH = 6;


    /**
     * Portlet service.
     */
    @Autowired
    private FirstConnectionService service;


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

        if (form.isMustChangePassword()) {
            ValidationUtils.rejectIfEmpty(errors, "password", "NotEmpty");
            ValidationUtils.rejectIfEmpty(errors, "passwordConfirmation", "NotEmpty");

            this.service.validatePasswordRules(errors, "password", form.getPassword());

            if (StringUtils.isNotEmpty(form.getPasswordConfirmation()) && !StringUtils.equals(form.getPassword(), form.getPasswordConfirmation())) {
                errors.rejectValue("passwordConfirmation", "Unmatching");
            }
        }
    }

}
