package org.osivia.services.person.card.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.person.card.portlet.model.PersonCard;
import org.osivia.services.person.card.portlet.model.PersonCardOptions;
import org.osivia.services.person.card.portlet.model.PersonEditionForm;
import org.osivia.services.person.card.portlet.model.PersonPasswordEditionForm;
import org.springframework.validation.Errors;

/**
 * Person card portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface PersonCardService {

    /** Person UID window property. */
    String PERSON_UID_WINDOW_PROPERTY = "uidFichePersonne";


    /**
     * Get portlet options.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    PersonCardOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get person card.
     * 
     * @param portalControllerContext portal controller context
     * @return person card
     * @throws PortletException
     */
    PersonCard getPersonCard(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Delete person.
     * 
     * @param portalControllerContext portal controller context
     * @param options portlet options
     * @throws PortletException
     */
    void deletePerson(PortalControllerContext portalControllerContext, PersonCardOptions options) throws PortletException;


    /**
     * Get person edition form.
     * 
     * @param portalControllerContext portal controller context
     * @return edition form
     * @throws PortletException
     */
    PersonEditionForm getEditionForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Upload avatar.
     * 
     * @param portalControllerContext portal controller context
     * @param form person edition form
     * @throws PortletException
     * @throws IOException
     */
    void uploadAvatar(PortalControllerContext portalControllerContext, PersonEditionForm form) throws PortletException, IOException;


    /**
     * Delete avatar.
     * 
     * @param portalControllerContext portal controller context
     * @param form person edition form
     * @throws PortletException
     * @throws IOException
     */
    void deleteAvatar(PortalControllerContext portalControllerContext, PersonEditionForm form) throws PortletException, IOException;


    /**
     * Save person.
     * 
     * @param portalControllerContext portal controller context
     * @param options portlet options
     * @param form person edition form
     * @throws PortletException
     */
    void savePerson(PortalControllerContext portalControllerContext, PersonCardOptions options, PersonEditionForm form) throws PortletException;


    /**
     * Get person password edition form.
     * 
     * @param portalControllerContext portal controller context
     * @return password edition form
     * @throws PortletException
     */
    PersonPasswordEditionForm getPasswordEditionForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update person password.
     * 
     * @param portalControllerContext portal controller context
     * @param options portlet options
     * @param form person password edition form
     * @throws PortletException
     */
    void updatePassword(PortalControllerContext portalControllerContext, PersonCardOptions options, PersonPasswordEditionForm form) throws PortletException;

    
    /**
     * Validate password rules.
     *
     * @param errors   validation errors
     * @param field    password field name
     * @param password password value
     */
    void validatePasswordRules(Errors errors, String field, String password);    

	/**
	 * Get password rules informations DOM element.
	 *
	 * @param portalControllerContext portal controller context
	 * @param password                password, may be null
	 * @return DOM element
	 */
	Element getPasswordRulesInformation(PortalControllerContext portalControllerContext, String password);

}
