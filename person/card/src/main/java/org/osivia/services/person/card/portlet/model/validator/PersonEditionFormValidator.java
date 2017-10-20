package org.osivia.services.person.card.portlet.model.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.person.card.portlet.model.PersonEditionForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Person edition form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class PersonEditionFormValidator implements Validator {

    /** Mail regex. */
    private static final String MAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    /** Mail pattern. */
    private final Pattern mailPattern;


    /**
     * Constructor.
     */
    public PersonEditionFormValidator() {
        super();

        // Mail pattern
        this.mailPattern = Pattern.compile(MAIL_REGEX);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return PersonEditionForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        // Edition form
        PersonEditionForm form = (PersonEditionForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mail", "NotEmpty");
        
        // Mail pattern matcher
        if (StringUtils.isNotBlank(form.getMail())) {
            Matcher matcher = this.mailPattern.matcher(StringUtils.trim(form.getMail()));
            if (!matcher.matches()) {
                errors.rejectValue("mail", "Invalid");
            }
        }
    }

}
