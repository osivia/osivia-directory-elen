package org.osivia.services.directory.workspace.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.workspace.portlet.model.LocalGroup;
import org.osivia.services.directory.workspace.portlet.model.LocalGroups;
import org.osivia.services.directory.workspace.portlet.model.Member;

/**
 * Workspace local group management service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface LocalGroupManagementService {

    /**
     * Get local groups.
     *
     * @param portalControllerContext portal controller context
     * @return local groups
     * @throws PortletException
     */
    LocalGroups getLocalGroups(PortalControllerContext portalControllerContext) throws PortletException;


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
     * Prepare local group deletion.
     *
     * @param portalControllerContext portal controller context
     * @param localGroups local groups
     * @param id deleted local group identifier
     * @throws PortletException
     */
    void prepareDeletion(PortalControllerContext portalControllerContext, LocalGroups localGroups, String id) throws PortletException;


    /**
     * Save local groups.
     *
     * @param portalControllerContext portal controller context
     * @param localGroups local groups
     * @throws PortletException
     */
    void saveLocalGroups(PortalControllerContext portalControllerContext, LocalGroups localGroups) throws PortletException;


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
     * Get members.
     *
     * @param portalControllerContext portal controller context
     * @return members
     * @throws PortletException
     */
    List<Member> getMembers(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Add members to local group.
     *
     * @param portalControllerContext portal controller context
     * @param localGroup local group
     * @throws PortletException
     */
    void addMembersToLocalGroup(PortalControllerContext portalControllerContext, LocalGroup localGroup) throws PortletException;


    /**
     * Save local group.
     *
     * @param portalControllerContext portal controller context
     * @param localGroup local group
     * @throws PortletException
     */
    void saveLocalGroup(PortalControllerContext portalControllerContext, LocalGroup localGroup) throws PortletException;


    /**
     * Delete local group.
     *
     * @param portalControllerContext portal controller context
     * @param id local group identifier
     * @throws PortletException
     */
    void deleteLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException;

}
