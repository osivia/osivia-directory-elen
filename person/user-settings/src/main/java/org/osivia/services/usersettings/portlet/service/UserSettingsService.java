package org.osivia.services.usersettings.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.usersettings.portlet.model.UserSettingsForm;

/**
 * User settings service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface UserSettingsService {

    /**
     * Get user settings form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    UserSettingsForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save user settings.
     * 
     * @param portalControllerContext portal controller context
     * @param form user settings form
     * @throws PortletException
     */
    void saveUserSettings(PortalControllerContext portalControllerContext, UserSettingsForm form) throws PortletException;

}
