package org.osivia.services.person.card.portlet.model;

import org.osivia.directory.v2.model.ext.Avatar;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Person card edition form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonEditionForm {

    /** Avatar. */
    private Avatar avatar;
    /** Title. */
    private String title;
    /** First name. */
    private String firstName;
    /** Last name. */
    private String lastName;
    /** Occupation. */
    private String occupation;
    /** Institution. */
    private String institution;
    /** Phone. */
    private String phone;
    /** Mobile phone. */
    private String mobilePhone;
    /** Mail. */
    private String mail;
    /** Bio. */
    private String bio;


    /**
     * Constructor.
     */
    public PersonEditionForm() {
        super();
    }


    /**
     * Getter for avatar.
     * 
     * @return the avatar
     */
    public Avatar getAvatar() {
        return avatar;
    }

    /**
     * Setter for avatar.
     * 
     * @param avatar the avatar to set
     */
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for firstName.
     * 
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for firstName.
     * 
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for lastName.
     * 
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for lastName.
     * 
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * Getter for mail.
     * 
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Setter for mail.
     * 
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
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
