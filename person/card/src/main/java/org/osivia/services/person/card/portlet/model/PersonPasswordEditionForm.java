package org.osivia.services.person.card.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Person password edition form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonPasswordEditionForm {

    /** Current password. */
    private String current;
    /** Update password value. */
    private String update;
    /** Update password confirmation. */
    private String confirmation;

    /** Person UID. */
    private String uid;
    /** Overwrite password indicator. */
    private boolean overwrite;


    /**
     * Constructor.
     */
    public PersonPasswordEditionForm() {
        super();
    }


    /**
     * Getter for current.
     * 
     * @return the current
     */
    public String getCurrent() {
        return current;
    }

    /**
     * Setter for current.
     * 
     * @param current the current to set
     */
    public void setCurrent(String current) {
        this.current = current;
    }

    /**
     * Getter for update.
     * 
     * @return the update
     */
    public String getUpdate() {
        return update;
    }

    /**
     * Setter for update.
     * 
     * @param update the update to set
     */
    public void setUpdate(String update) {
        this.update = update;
    }

    /**
     * Getter for confirmation.
     * 
     * @return the confirmation
     */
    public String getConfirmation() {
        return confirmation;
    }

    /**
     * Setter for confirmation.
     * 
     * @param confirmation the confirmation to set
     */
    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
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
     * Getter for overwrite.
     * 
     * @return the overwrite
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * Setter for overwrite.
     * 
     * @param overwrite the overwrite to set
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

}
