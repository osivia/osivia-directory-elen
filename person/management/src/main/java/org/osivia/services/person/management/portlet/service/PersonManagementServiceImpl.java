/**
 * 
 */
package org.osivia.services.person.management.portlet.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.services.person.management.portlet.repository.PersonManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Loïc Billon
 *
 */
@Service
public class PersonManagementServiceImpl implements PersonManagementService {

	@Autowired
	private PersonManagementRepository repository;
	
	@Autowired
	private PersonService personService;
	
	/* (non-Javadoc)
	 * @see org.osivia.services.person.management.portlet.service.PersonManagementService#searchPersons(org.osivia.portal.api.context.PortalControllerContext, java.lang.String, boolean)
	 */
	@Override
	public List<Person> searchPersons(
			PortalControllerContext portalControllerContext, String filter) {


        if (StringUtils.isNotBlank(filter)) {
	        // Persons
	        return this.repository.searchPersons(portalControllerContext, filter);
	        

        }
        return new ArrayList<Person>();
        
	}


}
