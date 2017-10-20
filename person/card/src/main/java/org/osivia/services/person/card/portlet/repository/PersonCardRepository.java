package org.osivia.services.person.card.portlet.repository;

import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.person.card.portlet.model.PersonEditionForm;
import org.osivia.services.person.card.portlet.model.PersonNuxeoProfile;

/**
 * Person card portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface PersonCardRepository {

    /**
     * Get person Nuxeo profile.
     * 
     * @param portalControllerContext portal controller context
     * @param person person
     * @return Nuxeo profile
     * @throws PortletException
     */
    PersonNuxeoProfile getNuxeoProfile(PortalControllerContext portalControllerContext, Person person) throws PortletException;


    /**
     * Get person profile Nuxeo document properties.
     * 
     * @param portalControllerContext portal controller context
     * @param form person edition form
     * @return Nuxeo properties
     * @throws PortletException
     */
    Map<String, String> getNuxeoProperties(PortalControllerContext portalControllerContext, PersonEditionForm form) throws PortletException;

}
