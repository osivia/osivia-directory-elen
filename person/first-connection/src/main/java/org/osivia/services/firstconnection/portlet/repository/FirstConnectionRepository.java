package org.osivia.services.firstconnection.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * First connection portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface FirstConnectionRepository {

    /** Redirection URL window property name. */
    String REDIRECTION_URL_PROPERTY = "osivia.services.firstConnection.redirectionUrl";


    /**
     * Get redirection URL.
     * 
     * @param portalControllerContext portal controller context
     * @return redirection URL
     * @throws PortletException
     */
    String getRedirectionUrl(PortalControllerContext portalControllerContext) throws PortletException;

}
