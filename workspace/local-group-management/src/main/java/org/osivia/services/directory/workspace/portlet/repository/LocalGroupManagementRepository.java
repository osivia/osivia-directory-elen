package org.osivia.services.directory.workspace.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.workspace.portlet.model.LocalGroup;
import org.osivia.services.directory.workspace.portlet.model.LocalGroups;
import org.osivia.services.directory.workspace.portlet.model.Member;

/**
 * Workspace local group management repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface LocalGroupManagementRepository {

    /**
     * Get local groups.
     *
     * @param portalControllerContext portal controller context
     * @return local groups
     * @throws PortletException
     */
    LocalGroups getLocalGroups(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Set local groups.
     *
     * @param portalControllerContext portal controller context
     * @param localGroups local groups
     * @throws PortletException
     */
    void setLocalGroups(PortalControllerContext portalControllerContext, LocalGroups localGroups) throws PortletException;


    /**
     * Get local group from his identifier.
     *
     * @param portalControllerContext portal controller context
     * @param id local group identifier
     * @return local group
     * @throws PortletException
     */
    LocalGroup getLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Set local group.
     *
     * @param portalControllerContext portal controller context
     * @param localGroup local group
     * @throws PortletException
     */
    void setLocalGroup(PortalControllerContext portalControllerContext, LocalGroup localGroup) throws PortletException;


    /**
     * Delete local group
     * 
     * @param portalControllerContext portal controller context
     * @param id local group identifier
     * @throws PortletException
     */
    void deleteLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Create local group.
     *
     * @param portalControllerContext portal controller context
     * @param localGroups local groups
     * @param form local group creation form
     * @throws PortletException
     */
    void createLocalGroup(PortalControllerContext portalControllerContext, LocalGroups localGroups, LocalGroup form) throws PortletException;


    /**
     * Get member from his identifier.
     *
     * @param portalControllerContext portal controller context
     * @param id member identifier
     * @return member
     * @throws PortletException
     */
    Member getMember(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Get all members.
     *
     * @param portalControllerContext portal controller context
     * @return members
     * @throws PortletException
     */
    List<Member> getAllMembers(PortalControllerContext portalControllerContext) throws PortletException;

}
