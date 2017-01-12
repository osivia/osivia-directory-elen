package org.osivia.services.usersettings.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.usersettings.portlet.model.UserSettingsForm;
import org.osivia.services.usersettings.portlet.model.WorkspaceNotification;

/**
 * User settings portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface UserSettingsRepository {

    /** Notifications enabled indicator document property. */
    String NOTIFICATIONS_ENABLED_PROPERTY = "ttc_userprofile:newsSubscription";
    /** Workspace members document property. */
    String WORKSPACE_MEMBERS_PROPERTY = "ttcs:spaceMembers";
    /** Workspace member name document property. */
    String WORKSPACE_MEMBER_NAME_PROPERTY = "login";
    /** Workspace member notification periodicity document property. */
    String WORKSPACE_MEMBER_NOTIFICATION_PERIODICITY_PROPERTY = "newsPeriod";


    /**
     * Get user profile Nuxeo document.
     * 
     * @param portalControllerContext portal controller context
     * @return user profile
     * @throws PortletException
     */
    Document getUserProfile(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * Check if notifications are enabled.
     * 
     * @param userProfile user profile Nuxeo document
     * @return true if notifications are enabled
     */
    boolean areNotificationsEnabled(Document userProfile);


    /**
     * Get workspace notifications.
     * @param portalControllerContext portal controller context
     * @return workspace notifications
     * @throws PortletException
     */
    List<WorkspaceNotification> getWorkspaceNotifications(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get workspace notification periodicity.
     * 
     * @param workspace workspace Nuxeo document
     * @param user user name
     * @return workspace notification periodicity identifier
     */
    String getWorkspaceNotificationPeriodicityId(Document workspace, String user);


    /**
     * Update user settings.
     * 
     * @param portalControllerContext portal controller context
     * @param form user settings form
     * @throws PortletException
     */
    void updateUserSettings(PortalControllerContext portalControllerContext, UserSettingsForm form) throws PortletException;

}
