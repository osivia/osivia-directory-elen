package org.osivia.services.person.creation.plugin.service;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;

/**
 * Person creation plugin service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface PersonCreationPluginService {

    /** Mail filter variable name parameter. */
    String MAIL_FILTER_VARIABLE_NAME = "mailVariableName";
    /** Password filter variable name parameter. */
    String PASSWORD_FILTER_VARIABLE_NAME = "passwordVariableName";


    /**
     * Create person.
     * 
     * @param context form filter context
     * @param executor form filter executor
     * @throws FormFilterException
     */
    void createPerson(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException;

}
