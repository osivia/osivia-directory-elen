package org.osivia.services.group.creation.plugin.service;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;

/**
 * Group creation plugin service interface.
 * 
 * @author Julien Barberet
 */
public interface GroupCreationPluginService {

    /** DisplayName filter variable name parameter. */
    String DISPLAYNAME_FILTER_VARIABLE_NAME = "displayNameVariableName";
    /** Description filter variable name parameter. */
    String DESCRIPTION_FILTER_VARIABLE_NAME = "descriptionVariableName";


    /**
     * Create group.
     * 
     * @param context form filter context
     * @param executor form filter executor
     * @throws FormFilterException
     */
    void createGroup(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException;

}
