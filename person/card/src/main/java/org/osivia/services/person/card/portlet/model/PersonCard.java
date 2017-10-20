package org.osivia.services.person.card.portlet.model;

import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Person card java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonCard {

    /** Person. */
    private Person person;
    /** Person Nuxeo profile. */
    private PersonNuxeoProfile nuxeoProfile;


    /**
     * Constructor.
     */
    public PersonCard() {
        super();
    }


    /**
     * Getter for person.
     * 
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Setter for person.
     * 
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Getter for nuxeoProfile.
     * 
     * @return the nuxeoProfile
     */
    public PersonNuxeoProfile getNuxeoProfile() {
        return nuxeoProfile;
    }

    /**
     * Setter for nuxeoProfile.
     * 
     * @param nuxeoProfile the nuxeoProfile to set
     */
    public void setNuxeoProfile(PersonNuxeoProfile nuxeoProfile) {
        this.nuxeoProfile = nuxeoProfile;
    }

}
