package org.osivia.services.group.creation.plugin.form;

import java.util.HashMap;
import java.util.Map;

import org.osivia.services.group.creation.plugin.service.GroupCreationPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Group creation form filter.
 * 
 * @author Julien Barberet
 * @see FormFilter
 */
@Component
public class GroupCreationFormFilter implements FormFilter {

    /** Form filter identifier. */
    private static final String IDENTIFIER = "GROUP_CREATION";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "GROUP_CREATION_FORM_FILTER_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = null;


    /** Plugin service. */
    @Autowired
    private GroupCreationPluginService service;


    /**
     * Constructor.
     */
    public GroupCreationFormFilter() {
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
        parameters.put(GroupCreationPluginService.DISPLAYNAME_FILTER_VARIABLE_NAME, FormFilterParameterType.TEXT);
        parameters.put(GroupCreationPluginService.DESCRIPTION_FILTER_VARIABLE_NAME, FormFilterParameterType.TEXT);
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
        this.service.createGroup(context, executor);
    }

}
