package org.osivia.services.person.card.portlet.model;

import java.util.List;

import javax.naming.Name;

import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Person card options java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class PersonCardOptions {

    /** Person UID. */
    private String uid;
    /** Person DN. */
    private Name dn;
    /** Person. */
    private Person person;
    /** Person titles. */
    private List<PersonTitle> titles;
    /** Editable person card indicator. */
    private boolean editable;
    /** Password edition mode. */
    private PersonPasswordEditionMode passwordEditionMode;
    /** Deletable person indicator. */
    private boolean deletable;


    /**
     * Constructor.
     */
    public PersonCardOptions() {
        super();
    }


    /**
     * Getter for uid.
     * 
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter for uid.
     * 
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter for dn.
     * 
     * @return the dn
     */
    public Name getDn() {
        return dn;
    }

    /**
     * Setter for dn.
     * 
     * @param dn the dn to set
     */
    public void setDn(Name dn) {
        this.dn = dn;
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
     * Getter for titles.
     * 
     * @return the titles
     */
    public List<PersonTitle> getTitles() {
        return titles;
    }

    /**
     * Setter for titles.
     * 
     * @param titles the titles to set
     */
    public void setTitles(List<PersonTitle> titles) {
        this.titles = titles;
    }

    /**
     * Getter for editable.
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Setter for editable.
     * 
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Getter for passwordEditionMode.
     * 
     * @return the passwordEditionMode
     */
    public PersonPasswordEditionMode getPasswordEditionMode() {
        return passwordEditionMode;
    }

    /**
     * Setter for passwordEditionMode.
     * 
     * @param passwordEditionMode the passwordEditionMode to set
     */
    public void setPasswordEditionMode(PersonPasswordEditionMode passwordEditionMode) {
        this.passwordEditionMode = passwordEditionMode;
    }

    /**
     * Getter for deletable.
     * 
     * @return the deletable
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * Setter for deletable.
     * 
     * @param deletable the deletable to set
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

}
