package org.osivia.services.directory.workspace.portlet.model.validator;

import org.apache.commons.collections.CollectionUtils;
import org.osivia.services.directory.workspace.portlet.model.AddForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Add member form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class AddFormValidator implements Validator {

    /**
     * Constructor.
     */
    public AddFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return AddForm.class.equals(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        AddForm form = (AddForm) target;

        if (CollectionUtils.isEmpty(form.getNames())) {
            errors.rejectValue("names", "NotEmpty");
        }
    }

}
