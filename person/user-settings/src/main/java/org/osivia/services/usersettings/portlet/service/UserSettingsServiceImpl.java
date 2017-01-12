package org.osivia.services.usersettings.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.usersettings.portlet.model.UserSettingsForm;
import org.osivia.services.usersettings.portlet.model.WorkspaceNotification;
import org.osivia.services.usersettings.portlet.repository.UserSettingsRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * User settings portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see UserSettingsService
 * @see ApplicationContextAware
 */
@Service
public class UserSettingsServiceImpl implements UserSettingsService, ApplicationContextAware {

    /** Application context. */
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private UserSettingsRepository repository;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public UserSettingsServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserSettingsForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        UserSettingsForm form = this.applicationContext.getBean(UserSettingsForm.class);

        // User profile
        Document userProfile = this.repository.getUserProfile(portalControllerContext);
        form.setUserProfile(userProfile);

        // Are notifications enabled?
        boolean notificationsEnabled = this.repository.areNotificationsEnabled(userProfile);
        form.setNotificationsEnabled(notificationsEnabled);

        // Workspace notifications
        List<WorkspaceNotification> notifications = this.repository.getWorkspaceNotifications(portalControllerContext);
        form.setWorkspaceNotifications(notifications);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveUserSettings(PortalControllerContext portalControllerContext, UserSettingsForm form) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        this.repository.updateUserSettings(portalControllerContext, form);

        // Notification
        String message = bundle.getString("USER_SETTINGS_SAVE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
