/**
 *
 */
package org.osivia.services.person.management.portlet.repository;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.services.person.management.portlet.model.PersonManagementForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Person management portlet repository implementation.
 *
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see PersonManagementRepository
 */
@Repository
public class PersonManagementRepositoryImpl implements PersonManagementRepository {

    /** Person service. */
    @Autowired
    private PersonService personService;


    /**
     * Constructor.
     */
    public PersonManagementRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> searchPersons(PortalControllerContext portalControllerContext, PersonManagementForm form) {
        // Search criteria
        Person criteria = this.getSearchCriteria(portalControllerContext, form);

        return this.personService.findByCriteria(criteria);
    }


    /**
     * Get search criteria.
     *
     * @param portalControllerContext portal controller context
     * @param filters search filters
     * @return search criteria
     */
    protected Person getSearchCriteria(PortalControllerContext portalControllerContext, PersonManagementForm form) {
        // Criteria
        Person criteria = this.personService.getEmptyPerson();

        // Filter
        String filter = form.getFilter();

        if (StringUtils.isNotBlank(filter)) {
            // Stripped filter
            String strippedFilter = StringUtils.strip(StringUtils.trim(filter), "*");
            // Tokenized filter
            String tokenizedFilter = strippedFilter + "*";
            // Tokenized filter substring
            String tokenizedFilterSubstring = "*" + tokenizedFilter;

            criteria.setUid(tokenizedFilter);
            criteria.setSn(tokenizedFilter);
            criteria.setGivenName(tokenizedFilter);
            criteria.setMail(tokenizedFilter);
            criteria.setDisplayName(tokenizedFilterSubstring);
        }

        return criteria;
    }

}
