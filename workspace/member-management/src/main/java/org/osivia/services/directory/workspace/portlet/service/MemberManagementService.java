package org.osivia.services.directory.workspace.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import net.sf.json.JSONArray;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.workspace.portlet.model.AddForm;
import org.osivia.services.directory.workspace.portlet.model.MembersContainer;

/**
 * Member management service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface MemberManagementService {

    /**
     * Get workspace members container.
     * 
     * @param portalControllerContext portal controller context
     * @return members container
     * @throws PortletException
     */
    MembersContainer getMembersContainer(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Search members.
     * 
     * @param portalControllerContext portal controller context
     * @param filter search filter
     * @return members
     * @throws PortletException
     */
    JSONArray searchMembers(PortalControllerContext portalControllerContext, String filter) throws PortletException;


    /**
     * Update workspace members container.
     * 
     * @param portalControllerContext portal controller context
     * @param container members container
     * @throws PortletException
     */
    void update(PortalControllerContext portalControllerContext, MembersContainer container) throws PortletException;


    /**
     * Delete member.
     * 
     * @param portalControllerContext portal controller context
     * @param container member container
     * @param name member name
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, MembersContainer container, String name) throws PortletException;


    /**
     * Add members.
     * 
     * @param portalControllerContext portal controller context
     * @param container members container
     * @param form form
     * @throws PortletException
     */
    void add(PortalControllerContext portalControllerContext, MembersContainer container, AddForm form) throws PortletException;


	/**
	 * Return allowed roles in the workspace
	 * 
	 * @param workspaceId
	 * @return
	 */
	List<WorkspaceRole> getAllowedRoles(String workspaceId);

}
