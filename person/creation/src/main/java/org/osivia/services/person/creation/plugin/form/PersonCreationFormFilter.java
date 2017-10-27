package org.osivia.services.person.creation.plugin.form;

import java.util.HashMap;
import java.util.Map;

import org.osivia.services.person.creation.plugin.service.PersonCreationPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Person creation form filter.
 * 
 * @author Cédric Krommenhoek
 * @see FormFilter
 */
@Component
public class PersonCreationFormFilter implements FormFilter {

    /** Form filter identifier. */
    private static final String IDENTIFIER = "PERSON_CREATION";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "PERSON_CREATION_FORM_FILTER_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = null;


    /** Plugin service. */
    @Autowired
    private PersonCreationPluginService service;


    /**
     * Constructor.
     */
    public PersonCreationFormFilter() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return IDENTIFIER;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelKey() {
        return LABEL_INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        Map<String, FormFilterParameterType> parameters = new HashMap<>();
        parameters.put(PersonCreationPluginService.MAIL_FILTER_VARIABLE_NAME, FormFilterParameterType.TEXT);
        parameters.put(PersonCreationPluginService.PASSWORD_FILTER_VARIABLE_NAME, FormFilterParameterType.TEXT);
        return parameters;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildren() {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        this.service.createPerson(context, executor);
    }

}
