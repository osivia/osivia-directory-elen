package org.osivia.services.person.card.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Person Nuxeo profile.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonNuxeoProfile {

    /** Occupation. */
    private String occupation;
    /** Institution. */
    private String institution;
    /** Phone. */
    private String phone;
    /** Mobile phone. */
    private String mobilePhone;
    /** Bio. */
    private String bio;


    /**
     * Constructor.
     */
    public PersonNuxeoProfile() {
        super();
    }


    /**
     * Getter for occupation.
     * 
     * @return the occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Setter for occupation.
     * 
     * @param occupation the occupation to set
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * Getter for institution.
     * 
     * @return the institution
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * Setter for institution.
     * 
     * @param institution the institution to set
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * Getter for phone.
     * 
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter for phone.
     * 
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Getter for mobilePhone.
     * 
     * @return the mobilePhone
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Setter for mobilePhone.
     * 
     * @param mobilePhone the mobilePhone to set
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * Getter for bio.
     * 
     * @return the bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * Setter for bio.
     * 
     * @param bio the bio to set
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

}
