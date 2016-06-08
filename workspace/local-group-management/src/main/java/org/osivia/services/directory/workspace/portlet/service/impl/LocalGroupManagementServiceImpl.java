package org.osivia.services.directory.workspace.portlet.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.directory.workspace.portlet.model.LocalGroup;
import org.osivia.services.directory.workspace.portlet.model.LocalGroups;
import org.osivia.services.directory.workspace.portlet.model.Member;
import org.osivia.services.directory.workspace.portlet.repository.LocalGroupManagementRepository;
import org.osivia.services.directory.workspace.portlet.service.LocalGroupManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Workspace local group management service implementation.
 *
 * @author Cédric Krommenhoek
 * @see LocalGroupManagementService
 */
@Service
public class LocalGroupManagementServiceImpl implements LocalGroupManagementService {

    /** Local group management repository. */
    @Autowired
    private LocalGroupManagementRepository repository;


    /**
     * Constructor.
     */
    public LocalGroupManagementServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroups getLocalGroups(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getLocalGroups(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroup getLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException {
        return this.repository.getLocalGroup(portalControllerContext, id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareDeletion(PortalControllerContext portalControllerContext, LocalGroups localGroups, String id) throws PortletException {
        List<LocalGroup> groups = localGroups.getGroups();
        if (CollectionUtils.isNotEmpty(groups)) {
            for (LocalGroup group : groups) {
                if (StringUtils.equals(id, group.getId())) {
                    group.setDeleted(true);
                    break;
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveLocalGroups(PortalControllerContext portalControllerContext, LocalGroups localGroups) throws PortletException {
        this.repository.setLocalGroups(portalControllerContext, localGroups);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createLocalGroup(PortalControllerContext portalControllerContext, LocalGroups localGroups, LocalGroup form) throws PortletException {
        this.repository.createLocalGroup(portalControllerContext, localGroups, form);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Member> getMembers(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getAllMembers(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMembersToLocalGroup(PortalControllerContext portalControllerContext, LocalGroup localGroup) throws PortletException {
        List<Member> members = localGroup.getMembers();
        if (members == null) {
            members = new ArrayList<Member>();
            localGroup.setMembers(members);
        }

        List<String> addedMembers = localGroup.getAddedMembers();
        if (CollectionUtils.isNotEmpty(addedMembers)) {
            for (String name : addedMembers) {
                Member member = new Member();
                member.setId(name);

                if (!members.contains(member)) {
                    member = this.repository.getMember(portalControllerContext, name);
                    members.add(member);
                }
            }
            addedMembers.clear();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveLocalGroup(PortalControllerContext portalControllerContext, LocalGroup localGroup) throws PortletException {
        // Remove deleted members
        if (CollectionUtils.isNotEmpty(localGroup.getMembers())) {
            List<Member> deleted = new ArrayList<Member>();
            for (Member member : localGroup.getMembers()) {
                if (member.isDeleted()) {
                    deleted.add(member);
                }
            }
            localGroup.getMembers().removeAll(deleted);
        }

        this.repository.setLocalGroup(portalControllerContext, localGroup);
    }


    /**
     * {@inheritDoc
     */
    @Override
    public void deleteLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException {
        this.repository.deleteLocalGroup(portalControllerContext, id);
    }

}
