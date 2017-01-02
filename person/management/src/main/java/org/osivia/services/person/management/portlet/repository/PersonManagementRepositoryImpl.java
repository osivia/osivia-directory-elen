/**
 * 
 */
package org.osivia.services.person.management.portlet.repository;

import java.util.List;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Loïc Billon
 *
 */
@Repository
public class PersonManagementRepositoryImpl implements
		PersonManagementRepository {

	@Autowired
	private PersonService personService;
	
	/* (non-Javadoc)
	 * @see org.osivia.services.person.management.portlet.repository.PersonManagementRepository#searchPersons(org.osivia.portal.api.context.PortalControllerContext)
	 */
	@Override
	public List<Person> searchPersons(
			PortalControllerContext portalControllerContext,String filter) {
		
		
        // Criteria
        Person criteria = this.personService.getEmptyPerson();

        String tokenizedFilter = filter + "*";
        String tokenizedFilterSubStr = "*" + filter + "*";

        criteria.setUid(tokenizedFilter);
        criteria.setSn(tokenizedFilter);
        criteria.setGivenName(tokenizedFilter);
        criteria.setMail(tokenizedFilter);

        criteria.setDisplayName(tokenizedFilterSubStr);
        

        return this.personService.findByCriteria(criteria);
	}

	
}
