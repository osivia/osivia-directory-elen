package org.osivia.services.group.management.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.group.management.portlet.model.GroupManagementForm;

/**
 * Group management portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface GroupManagementService {

    /** Minimum filter input length. */
    int FILTER_MINIMUM_LENGTH = 3;
    /** Region property. */
    String REGION_PROPERTY = "template.auxiliary.region";


    /**
     * Get group management form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    GroupManagementForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Select group.
     * 
     * @param portalControllerContext portal controller context
     * @param form group management form
     * @throws PortletException
     */
    void select(PortalControllerContext portalControllerContext, GroupManagementForm form) throws PortletException;


    /**
     * Search groups.
     * 
     * @param portalControllerContext portal controller context
     * @param form group management form
     * @throws PortletException
     */
    void search(PortalControllerContext portalControllerContext, GroupManagementForm form) throws PortletException;


    /**
     * Search groups.
     * 
     * @param portalControllerContext portal controller context
     * @param form group management form
     * @param filters search filters
     * @throws PortletException
     */
    void search(PortalControllerContext portalControllerContext, GroupManagementForm form, String filters) throws PortletException;


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
