package org.osivia.services.person.card.portlet.repository;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.person.card.portlet.model.PersonEditionForm;
import org.osivia.services.person.card.portlet.model.PersonNuxeoProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

/**
 * Person card portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see PersonCardRepository
 */
@Repository
public class PersonCardRepositoryImpl implements PersonCardRepository {

    /** Occupation Nuxeo document property. */
    private static final String OCCUPATION_PROPERTY = "ttc_userprofile:profession";
    /** Institution Nuxeo document property. */
    private static final String INSTITUTION_PROPERTY = "ttc_userprofile:institution";
    /** Phone Nuxeo document property. */
    private static final String PHONE_PROPERTY = "userprofile:phonenumber";
    /** Mobile phone Nuxeo document property. */
    private static final String MOBILE_PHONE_PROPERTY = "ttc_userprofile:mobile";
    /** Bio Nuxeo document property. */
    private static final String BIO_PROPERTY = "ttc_userprofile:bio";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Person service. */
    @Autowired
    private PersonUpdateService personService;


    /**
     * Constructor.
     */
    public PersonCardRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PersonNuxeoProfile getNuxeoProfile(PortalControllerContext portalControllerContext, Person person) throws PortletException {
        // Nuxeo profile document
        Document document;
        if (person == null) {
            document = null;
        } else {
            try {
                document = (Document) this.personService.getEcmProfile(portalControllerContext, person);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }

        return this.convertNuxeoProfile(document);
    }


    /**
     * Convert Nuxeo profile document to person Nuxeo profile.
     * 
     * @param document Nuxeo document
     * @return person Nuxeo profile
     * @throws PortletException
     */
    protected PersonNuxeoProfile convertNuxeoProfile(Document document) throws PortletException {
        // Person Nuxeo profile
        PersonNuxeoProfile profile;

        if (document == null) {
            profile = null;
        } else {
            profile = this.applicationContext.getBean(PersonNuxeoProfile.class);
            profile.setOccupation(document.getString(OCCUPATION_PROPERTY));
            profile.setInstitution(document.getString(INSTITUTION_PROPERTY));
            profile.setPhone(document.getString(PHONE_PROPERTY));
            profile.setMobilePhone(document.getString(MOBILE_PHONE_PROPERTY));
            profile.setBio(document.getString(BIO_PROPERTY));
        }

        return profile;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getNuxeoProperties(PortalControllerContext portalControllerContext, PersonEditionForm form) throws PortletException {
        // Nuxeo properties
        Map<String, String> properties = new HashMap<>();

        properties.put(OCCUPATION_PROPERTY, StringUtils.trimToEmpty(form.getOccupation()));
        properties.put(INSTITUTION_PROPERTY, StringUtils.trimToEmpty(form.getInstitution()));
        properties.put(PHONE_PROPERTY, StringUtils.trimToEmpty(form.getPhone()));
        properties.put(MOBILE_PHONE_PROPERTY, StringUtils.trimToEmpty(form.getMobilePhone()));
        properties.put(BIO_PROPERTY, StringUtils.trimToEmpty(form.getBio()));

        return properties;
    }

}
