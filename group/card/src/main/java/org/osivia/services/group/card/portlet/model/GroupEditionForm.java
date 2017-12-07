package org.osivia.services.group.card.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Group card edition form java-bean.
 * 
 * @author Julien Barberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupEditionForm {

    /** CN */
    private String cn;
    /** Title. */
    private String displayName;
    /** First name. */
    private String description;
    /** Last name. */
    private List<Member> members;
    
    private List<Member> addedMember;
    
    private boolean warning;


    /**
     * Constructor.
     */
    public GroupEditionForm() {
        super();
    }
    
    /**
     * Getter for displayName.
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Setter for displayName.
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Getter for description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Setter for description.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for members.
     * @return the members
     */
    public List<Member> getMembers() {
        return members;
    }
    
    /**
     * Setter for members.
     * @param members the members to set
     */
    public void setMembers(List<Member> members) {
        this.members = members;
    }
 
    /**
     * Getter for cn.
     * @return the cn
     */
    public String getCn() {
        return cn;
    }
    
    /**
     * Setter for cn.
     * @param cn the cn to set
     */
    public void setCn(String cn) {
        this.cn = cn;
    }

    /**
     * Getter for warning.
     * @return the warning
     */
    public boolean isWarning() {
        return warning;
    }

    
    /**
     * Setter for warning.
     * @param warning the warning to set
     */
    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    
    /**
     * Getter for addedMember.
     * @return the addedMember
     */
    public List<Member> getAddedMember() {
        return addedMember;
    }

    
    /**
     * Setter for addedMember.
     * @param addedMember the addedMember to set
     */
    public void setAddedMember(List<Member> addedMember) {
        this.addedMember = addedMember;
    }

}
