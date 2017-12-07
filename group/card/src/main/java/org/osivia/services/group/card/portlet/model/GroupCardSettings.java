package org.osivia.services.group.card.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Group card settings java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupCardSettings {

    /** Resource loader stub indicator. */
    private boolean stub;


    /**
     * Constructor.
     */
    public GroupCardSettings() {
        super();
    }


    /**
     * Getter for stub.
     * 
     * @return the stub
     */
    public boolean isStub() {
        return stub;
    }

    /**
     * Setter for stub.
     * 
     * @param stub the stub to set
     */
    public void setStub(boolean stub) {
        this.stub = stub;
    }

}
