package org.osivia.services.group.card.portlet.model.validator;

import org.osivia.services.group.card.portlet.model.GroupEditionForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Group edition form validator.
 * 
 * @author Julien Barberet
 * @see Validator
 */
@Component
public class GroupEditionFormValidator implements Validator {

    /**
     * Constructor.
     */
    public GroupEditionFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return GroupEditionForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayName", "NotEmpty");

    }

}
