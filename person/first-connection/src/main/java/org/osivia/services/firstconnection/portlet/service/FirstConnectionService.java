package org.osivia.services.firstconnection.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.firstconnection.portlet.model.UserForm;

/**
 * First connection portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface FirstConnectionService {

    /**
     * Get user form.
     * 
     * @param portalControllerContext portal controller context
     * @return user form
     * @throws PortletException
     */
    UserForm getUserForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save user form.
     * 
     * @param portalControllerContext portal controller context
     * @param form user form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, UserForm form) throws PortletException;


    /**
     * Get redirection URL.
     * 
     * @param portalControllerContext portal controller context
     * @return redirection URL
     */
    String getRedirectionUrl(PortalControllerContext portalControllerContext) throws PortletException;

}
