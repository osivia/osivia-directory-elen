package org.osivia.services.usersettings.portlet.repository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.usersettings.portlet.model.UserSettingsForm;
import org.osivia.services.usersettings.portlet.model.WorkspaceNotification;
import org.osivia.services.usersettings.portlet.model.WorkspaceNotificationPeriodicity;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * User settings portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see UserSettingsRepository
 * @see ApplicationContextAware
 */
@Repository
public class UserSettingsRepositoryImpl implements UserSettingsRepository, ApplicationContextAware {

    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public UserSettingsRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Document getUserProfile(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        // User principal
        Principal principal = portalControllerContext.getHttpServletRequest().getUserPrincipal();

        // User profile
        Document userProfile;
        if (principal == null) {
            userProfile = null;
        } else {
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(GetUserProfileCommand.class, principal.getName());

            userProfile = (Document) nuxeoController.executeNuxeoCommand(command);
        }

        return userProfile;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areNotificationsEnabled(Document userProfile) {
        // Notifications enabled indicator
        boolean notificationsEnabled;
        if (userProfile == null) {
            notificationsEnabled = false;
        } else {
            notificationsEnabled = BooleanUtils.isTrue(userProfile.getProperties().getBoolean(NOTIFICATIONS_ENABLED_PROPERTY));
        }

        return notificationsEnabled;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkspaceNotification> getWorkspaceNotifications(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // User principal
        Principal principal = portalControllerContext.getRequest().getUserPrincipal();

        // Workspace notifications
        List<WorkspaceNotification> notifications;
        
        if (principal == null) {
            notifications = new ArrayList<WorkspaceNotification>(0);
        } else {
            // User name
            String user = principal.getName();
            // User workspaces
            List<Document> workspaces = this.getUserWorkspaces(nuxeoController);

            notifications = new ArrayList<WorkspaceNotification>(workspaces.size());

            for (Document workspace : workspaces) {
                WorkspaceNotification notification = this.applicationContext.getBean(WorkspaceNotification.class);

                // Document
                notification.setWorkspace(workspace);
                // Title
                notification.setTitle(workspace.getTitle());
                // Description
                notification.setDescription(workspace.getString("dc:description"));

                // Vignette URL
                PropertyMap vignette = workspace.getProperties().getMap("ttc:vignette");
                String vignetteUrl;
                if (vignette == null) {
                    vignetteUrl = null;
                } else {
                    vignetteUrl = nuxeoController.createFileLink(workspace, "ttc:vignette");
                }
                notification.setVignetteUrl(vignetteUrl);

                // Periodicity
                String periodicityId = this.getWorkspaceNotificationPeriodicityId(workspace, user);
                WorkspaceNotificationPeriodicity periodicity = WorkspaceNotificationPeriodicity.fromId(periodicityId);
                notification.setPeriodicity(periodicity);

                notifications.add(notification);
            }
        }


        return notifications;
    }


    /**
     * Get user workspaces.
     * 
     * @param portalControllerContext portal controller context
     * @return user workspaces
     * @throws PortletException
     */
    private List<Document> getUserWorkspaces(NuxeoController nuxeoController) throws PortletException {
        // User principal
        Principal principal = nuxeoController.getRequest().getUserPrincipal();

        // User workspaces
        List<Document> workspaces;
        if (principal == null) {
            workspaces = new ArrayList<Document>(0);
        } else {
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(GetUserWorkspacesCommand.class, principal.getName());

            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);
            workspaces = documents.list();
        }
        
        return workspaces;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceNotificationPeriodicityId(Document workspace, String user) {
        // Workspace members
        PropertyList members = workspace.getProperties().getList(WORKSPACE_MEMBERS_PROPERTY);

        // Periodicity identifier
        String periodicityId = null;

        if (members != null) {
            for (int i = 0; i < members.size(); i++) {
                PropertyMap member = members.getMap(i);
                if (StringUtils.equals(user, member.getString(WORKSPACE_MEMBER_NAME_PROPERTY))) {
                    periodicityId = member.getString(WORKSPACE_MEMBER_NOTIFICATION_PERIODICITY_PROPERTY);
                    break;
                }
            }
        }

        return periodicityId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserSettings(PortalControllerContext portalControllerContext, UserSettingsForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // User principal
        Principal principal = portalControllerContext.getRequest().getUserPrincipal();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateUserSettingsCommand.class, form, principal.getName());
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
