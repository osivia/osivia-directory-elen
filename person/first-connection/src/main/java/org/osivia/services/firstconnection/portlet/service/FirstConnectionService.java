package org.osivia.services.firstconnection.portlet.service;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.firstconnection.portlet.model.UserForm;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;

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
     * Validate password rules.
     *
     * @param errors   validation errors
     * @param field    password field name
     * @param password password value
     */
    void validatePasswordRules(Errors errors, String field, String password);


    /**
     * Get password rules informations DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param password                password, may be null
     * @return DOM element
     */
    Element getPasswordRulesInformation(PortalControllerContext portalControllerContext, String password) throws PortletException;


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
