package org.osivia.services.usersettings.portlet.model;

import java.util.Arrays;
import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User settings form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSettingsForm {

    /** User profile Nuxeo document. */
    private Document userProfile;
    /** Notifications enabled indicator. */
    private boolean notificationsEnabled;
    /** Workspace notifications. */
    private List<WorkspaceNotification> workspaceNotifications;

    /** Workspace notification periodicities. */
    private final List<WorkspaceNotificationPeriodicity> workspaceNotificationPeriodicities;


    /**
     * Constructor.
     */
    public UserSettingsForm() {
        super();
        this.workspaceNotificationPeriodicities = Arrays.asList(WorkspaceNotificationPeriodicity.values());
    }


    /**
     * Getter for userProfile.
     * 
     * @return the userProfile
     */
    public Document getUserProfile() {
        return userProfile;
    }

    /**
     * Setter for userProfile.
     * 
     * @param userProfile the userProfile to set
     */
    public void setUserProfile(Document userProfile) {
        this.userProfile = userProfile;
    }

    /**
     * Getter for notificationsEnabled.
     * 
     * @return the notificationsEnabled
     */
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    /**
     * Setter for notificationsEnabled.
     * 
     * @param notificationsEnabled the notificationsEnabled to set
     */
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    /**
     * Getter for workspaceNotifications.
     * 
     * @return the workspaceNotifications
     */
    public List<WorkspaceNotification> getWorkspaceNotifications() {
        return workspaceNotifications;
    }

    /**
     * Setter for workspaceNotifications.
     * 
     * @param workspaceNotifications the workspaceNotifications to set
     */
    public void setWorkspaceNotifications(List<WorkspaceNotification> workspaceNotifications) {
        this.workspaceNotifications = workspaceNotifications;
    }

    /**
     * Getter for workspaceNotificationPeriodicities.
     * 
     * @return the workspaceNotificationPeriodicities
     */
    public List<WorkspaceNotificationPeriodicity> getWorkspaceNotificationPeriodicities() {
        return workspaceNotificationPeriodicities;
    }

}
