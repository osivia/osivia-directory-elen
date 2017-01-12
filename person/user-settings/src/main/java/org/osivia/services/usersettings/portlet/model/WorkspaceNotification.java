package org.osivia.services.usersettings.portlet.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Workspace notification
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkspaceNotification {

    /** Workspace Nuxeo document. */
    private Document workspace;
    /** Workspace title. */
    private String title;
    /** Workspace description. */
    private String description;
    /** Workspace vignetteUrl. */
    private String vignetteUrl;
    /** Workspace notification periodicity. */
    private WorkspaceNotificationPeriodicity periodicity;


    /**
     * Constructor.
     */
    public WorkspaceNotification() {
        super();
    }


    /**
     * Getter for workspace.
     * 
     * @return the workspace
     */
    public Document getWorkspace() {
        return workspace;
    }

    /**
     * Setter for workspace.
     * 
     * @param workspace the workspace to set
     */
    public void setWorkspace(Document workspace) {
        this.workspace = workspace;
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
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for vignetteUrl.
     * 
     * @return the vignetteUrl
     */
    public String getVignetteUrl() {
        return vignetteUrl;
    }

    /**
     * Setter for vignetteUrl.
     * 
     * @param vignetteUrl the vignetteUrl to set
     */
    public void setVignetteUrl(String vignetteUrl) {
        this.vignetteUrl = vignetteUrl;
    }

    /**
     * Getter for periodicity.
     * 
     * @return the periodicity
     */
    public WorkspaceNotificationPeriodicity getPeriodicity() {
        return periodicity;
    }

    /**
     * Setter for periodicity.
     * 
     * @param periodicity the periodicity to set
     */
    public void setPeriodicity(WorkspaceNotificationPeriodicity periodicity) {
        this.periodicity = periodicity;
    }

}
