/**
 * 
 */
package org.osivia.services.person.management.portlet.repository;

import java.util.List;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;

/**
 * @author Loïc Billon
 *
 */
public interface PersonManagementRepository {


	/**
	 * Search person
	 * @param portalControllerContext
	 * @param filter search filter
	 * @return list of persons
	 */
	List<Person> searchPersons(PortalControllerContext portalControllerContext, String filter);

}
