package org.osivia.services.directory.workspace.portlet.model.validator;

import org.osivia.services.directory.workspace.portlet.model.LocalGroup;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Local group validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class LocalGroupValidator implements Validator {

    /**
     * Constructor.
     */
    public LocalGroupValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(LocalGroup.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayName", "NotEmpty");
    }

}
