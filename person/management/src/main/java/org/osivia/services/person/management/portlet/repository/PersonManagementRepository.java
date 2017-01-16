/**
 * 
 */
package org.osivia.services.person.management.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Person management portlet repository interface.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 */
public interface PersonManagementRepository {

    /**
     * Search persons.
     * 
     * @param portalControllerContext portal controller context
     * @param filter search filter
     * @return persons
     * @throws PortletException
     */
    List<Person> searchPersons(PortalControllerContext portalControllerContext, String filter) throws PortletException;

}
