/**
 * 
 */
package org.osivia.services.person.management.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.person.management.portlet.model.PersonManagementForm;

/**
 * Person management portlet service interface.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 */
public interface PersonManagementService {

    /** Region property. */
    String REGION_PROPERTY = "template.auxiliary.region";


    /**
     * Get person management form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    PersonManagementForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Search users.
     * 
     * @param portalControllerContext portal controller context
     * @param form person management form
     * @throws PortletException
     */
    void search(PortalControllerContext portalControllerContext, PersonManagementForm form) throws PortletException;


    /**
     * Search users.
     * 
     * @param portalControllerContext portal controller context
     * @param form person management form
     * @param filters search filters
     * @throws PortletException
     */
    void search(PortalControllerContext portalControllerContext, PersonManagementForm form, String filters) throws PortletException;


    /**
     * Select user.
     * 
     * @param portalControllerContext portal controller context
     * @param form person management form
     * @throws PortletException
     */
    void select(PortalControllerContext portalControllerContext, PersonManagementForm form) throws PortletException;


    /**
     * Resolve view path.
     * 
     * @param portalControllerContext portal controller context
     * @param name view name
     * @return view path
     * @throws PortletException
     */
    String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException;

}
