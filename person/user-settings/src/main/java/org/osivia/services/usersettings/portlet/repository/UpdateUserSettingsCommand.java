package org.osivia.services.usersettings.portlet.repository;

import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.usersettings.portlet.model.UserSettingsForm;
import org.osivia.services.usersettings.portlet.model.WorkspaceNotification;
import org.osivia.services.usersettings.portlet.model.WorkspaceNotificationPeriodicity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Update user settings Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateUserSettingsCommand implements INuxeoCommand {

    /** Portlet repository. */
    @Autowired
    private UserSettingsRepository repository;

    /** User settings form. */
    private final UserSettingsForm form;
    /** Current user name. */
    private final String user;


    /**
     * Constructor.
     * 
     * @param form user settings form
     * @param user current user name
     */
    public UpdateUserSettingsCommand(UserSettingsForm form, String user) {
        super();
        this.form = form;
        this.user = user;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Update user profile
        this.updateUserProfile(documentService);

        if (this.form.isNotificationsEnabled()) {
            // Update user workspaces
            this.updateUserWorkspaces(documentService);
        }

        return null;
    }


    /**
     * Update user profile.
     * 
     * @param documentService document service
     * @throws Exception
     */
    private void updateUserProfile(DocumentService documentService) throws Exception {
        // User profile
        Document userProfile = this.form.getUserProfile();

        // Notifications enabled indicator
        boolean notificationsEnabled = this.form.isNotificationsEnabled();

        // Notifications enabled indicator initial value
        boolean notificationsEnabledInitial = this.repository.areNotificationsEnabled(userProfile);

        if (notificationsEnabled != notificationsEnabledInitial) {
            documentService.setProperty(userProfile, UserSettingsRepository.NOTIFICATIONS_ENABLED_PROPERTY, String.valueOf(notificationsEnabled));
        }
    }


    /**
     * Update user workspaces.
     * 
     * @param documentService document service
     * @throws Exception
     */
    private void updateUserWorkspaces(DocumentService documentService) throws Exception {
        for (WorkspaceNotification notification : this.form.getWorkspaceNotifications()) {
            // Workspace
            Document workspace = notification.getWorkspace();
            // Periodicity
            WorkspaceNotificationPeriodicity periodicity = notification.getPeriodicity();
            // Periodicity identifier initial value
            String periodicityIdInitial = this.repository.getWorkspaceNotificationPeriodicityId(workspace, this.user);

            if (!StringUtils.equals(periodicityIdInitial, periodicity.getId())) {
                // Members
                PropertyList members = workspace.getProperties().getList(UserSettingsRepository.WORKSPACE_MEMBERS_PROPERTY);

                // Updated properties
                PropertyMap properties = new PropertyMap();
                properties.set("ttcs:spaceMembers", this.generateUpdatedMembersJSON(members, periodicity));

                documentService.update(workspace, properties);
            }
        }
    }


    /**
     * Generate updated members JSON content.
     *
     * @param members members
     * @return JSON
     */
    private String generateUpdatedMembersJSON(PropertyList members, WorkspaceNotificationPeriodicity periodicity) {
        JSONArray array = new JSONArray();

        for (int i = 0; i < members.size(); i++) {
            PropertyMap member = members.getMap(i);
            String login = member.getString("login");

            JSONObject object = new JSONObject();
            for (Entry<String, Object> entry : member.getMap().entrySet()) {
                String key = entry.getKey();
                Object value;

                if (StringUtils.equals(UserSettingsRepository.WORKSPACE_MEMBER_NOTIFICATION_PERIODICITY_PROPERTY, key)
                        && StringUtils.equals(this.user, login)) {
                    value = periodicity.getId();
                } else {
                    value = entry.getValue();
                }

                object.put(key, value);
            }
            array.add(object);
        }
        
        return array.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
