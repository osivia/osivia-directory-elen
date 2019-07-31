package org.osivia.services.person.card.portlet.model.validator;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.services.person.card.portlet.model.PersonPasswordEditionForm;
import org.osivia.services.person.card.portlet.service.PersonCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Person password edition form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class PersonPasswordEditionFormValidator implements Validator {

    /** Person service. */
    @Autowired
    private PersonUpdateService personService;

    @Autowired
    private PersonCardService service;

    /**
     * Constructor.
     */
    public PersonPasswordEditionFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return PersonPasswordEditionForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        // Password edition form
        PersonPasswordEditionForm form = (PersonPasswordEditionForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "update", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmation", "NotEmpty");

        if (!form.isOverwrite()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "current", "NotEmpty");

            if (StringUtils.isNotBlank(form.getCurrent()) && !this.personService.verifyPassword(form.getUid(), form.getCurrent())) {
                errors.rejectValue("current", "Invalid");
            }
        }

        if (StringUtils.isNotBlank(form.getUpdate()) && StringUtils.isNotBlank(form.getConfirmation())
                && !StringUtils.equals(form.getUpdate(), form.getConfirmation())) {
            errors.rejectValue("confirmation", "Invalid");
        }
        
        service.validatePasswordRules(errors, "update", form.getUpdate());
    }

}
