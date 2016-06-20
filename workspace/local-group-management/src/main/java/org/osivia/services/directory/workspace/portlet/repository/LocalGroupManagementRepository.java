package org.osivia.services.directory.workspace.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.workspace.portlet.model.LocalGroup;
import org.osivia.services.directory.workspace.portlet.model.LocalGroupEditionForm;
import org.osivia.services.directory.workspace.portlet.model.LocalGroups;
import org.osivia.services.directory.workspace.portlet.model.Member;

/**
 * Workspace local group management repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface LocalGroupManagementRepository {

    /**
     * Get workspace identifier.
     * 
     * @param portalControllerContext portal controller context
     * @return workspace identifier
     * @throws PortletException
     */
    String getWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get local groups.
     *
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return local groups
     * @throws PortletException
     */
    List<LocalGroup> getLocalGroups(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Set local groups.
     *
     * @param portalControllerContext portal controller context
     * @param localGroups local groups
     * @throws PortletException
     */
    void setLocalGroups(PortalControllerContext portalControllerContext, LocalGroups localGroups) throws PortletException;


    /**
     * Get local group edition form from its identifier.
     *
     * @param portalControllerContext portal controller context
     * @param id local group identifier
     * @return form
     * @throws PortletException
     */
    LocalGroupEditionForm getLocalGroupEditionForm(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Set local group.
     *
     * @param portalControllerContext portal controller context
     * @param form local group edition form
     * @throws PortletException
     */
    void setLocalGroup(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException;


    /**
     * Delete local group
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param id local group identifier
     * @throws PortletException
     */
    void deleteLocalGroup(PortalControllerContext portalControllerContext, String workspaceId, String id) throws PortletException;


    /**
     * Create local group.
     *
     * @param portalControllerContext portal controller context
     * @param localGroups local groups
     * @param form local group creation form
     * @return created local group
     * @throws PortletException
     */
    LocalGroup createLocalGroup(PortalControllerContext portalControllerContext, LocalGroups localGroups, LocalGroup form) throws PortletException;


    /**
     * Get all members.
     *
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return members
     * @throws PortletException
     */
    List<Member> getAllMembers(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;

}
