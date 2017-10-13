/**
 * 
 */
package org.osivia.services.person.management.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.person.management.portlet.model.PersonManagementForm;
import org.osivia.services.person.management.portlet.model.User;

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
     * Get users.
     * 
     * @param portalControllerContext portal controller context
     * @param filter search filter
     * @return users
     * @throws PortletException
     */
    List<User> getUsers(PortalControllerContext portalControllerContext, String filter) throws PortletException;


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
