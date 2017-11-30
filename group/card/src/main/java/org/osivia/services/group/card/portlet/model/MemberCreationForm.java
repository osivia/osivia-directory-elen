package org.osivia.services.group.card.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Member creation form java-bean.
 * 
 * @author Julien Barberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MemberCreationForm {

    /** Pending invitations. */
    private List<Member> pendingMembers;
    /** Warning indicator. */
    private boolean warning;


    /**
     * Constructor.
     */
    public MemberCreationForm() {
        super();
    }
    
    /**
     * Getter for pendingMembers.
     * @return the pendingMembers
     */
    public List<Member> getPendingMembers() {
        return pendingMembers;
    }

    /**
     * Setter for pendingMembers.
     * @param pendingMembers the pendingMembers to set
     */
    public void setPendingMembers(List<Member> pendingMembers) {
        this.pendingMembers = pendingMembers;
    }




    /**
     * Getter for warning.
     * 
     * @return the warning
     */
    public boolean isWarning() {
        return warning;
    }

    /**
     * Setter for warning.
     * 
     * @param warning the warning to set
     */
    public void setWarning(boolean warning) {
        this.warning = warning;
    }

}
