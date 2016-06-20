package org.osivia.services.directory.workspace.portlet.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.services.directory.workspace.portlet.model.LocalGroup;
import org.osivia.services.directory.workspace.portlet.model.LocalGroupEditionForm;
import org.osivia.services.directory.workspace.portlet.model.LocalGroupListItem;
import org.osivia.services.directory.workspace.portlet.model.LocalGroups;
import org.osivia.services.directory.workspace.portlet.model.Member;
import org.osivia.services.directory.workspace.portlet.repository.LocalGroupManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Workspace local group management repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see LocalGroupManagementRepository
 */
@Repository
public class LocalGroupManagementRepositoryImpl implements LocalGroupManagementRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;
    
    /** Person service. */
    @Autowired
    private PersonService personService;


    /**
     * Constructor.
     */
    public LocalGroupManagementRepositoryImpl() throws NamingException {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(basePath);

        // Nuxeo document
        Document document = documentContext.getDoc();

        return document.getString("webc:url");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalGroup> getLocalGroups(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Criteria
        CollabProfile criteria = workspaceService.getEmptyProfile();
        criteria.setWorkspaceId(workspaceId);
        criteria.setType(WorkspaceGroupType.local_group);

        // Search
        List<CollabProfile> groups = workspaceService.findByCriteria(criteria);

        // Local groups
        List<LocalGroup> localGroups;
        if (CollectionUtils.isEmpty(groups)) {
            localGroups = new ArrayList<LocalGroup>(0);
        } else {
            localGroups = new ArrayList<LocalGroup>(groups.size());
            for (CollabProfile group : groups) {
                LocalGroup localGroup = createLocalGroup(group);

                localGroups.add(localGroup);
            }
        }

        return localGroups;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocalGroups(PortalControllerContext portalControllerContext, LocalGroups localGroups) throws PortletException {
        // Deleted local groups
        List<LocalGroup> deleted = new ArrayList<LocalGroup>();
        for (LocalGroup group : localGroups.getGroups()) {
            LocalGroupListItem localGroup = (LocalGroupListItem) group;
            if (localGroup.isDeleted()) {
                workspaceService.removeLocalGroup(localGroups.getWorkspaceId(), localGroup.getId());

                deleted.add(group);
            }
        }

        // Update model
        localGroups.getGroups().removeAll(deleted);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroupEditionForm getLocalGroupEditionForm(PortalControllerContext portalControllerContext, String id) throws PortletException {
        // Workspace identifier
        String workspaceId = this.getWorkspaceId(portalControllerContext);

        // Search local group
        CollabProfile group = workspaceService.getProfile(id);

        // Search members
        Person criteria = personService.getEmptyPerson();
        criteria.setPortalPersonProfile(Arrays.asList(new Name[]{group.getDn()}));
        List<Person> persons = personService.findByCriteria(criteria);


        // Members
        List<Member> members;
        if (CollectionUtils.isEmpty(persons)) {
            members = new ArrayList<Member>(0);
        } else {
            members = new ArrayList<Member>(persons.size());
            for (Person person : persons) {
                Member member = createMember(person);
                members.add(member);
            }
        }


        // Form
        LocalGroupEditionForm form = this.applicationContext.getBean(LocalGroupEditionForm.class);
        form.setId(group.getCn());
        form.setWorkspaceId(workspaceId);
        form.setDisplayName(group.getDisplayName());
        form.setDescription(group.getDescription());
        form.setMembers(members);
        
        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocalGroup(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException {
        // Workspace identifier
        String workspaceId = form.getWorkspaceId();

        // Local group CN
        String cn = form.getId();

        // Update local group properties
        CollabProfile group = this.workspaceService.getProfile(cn);
        group.setDisplayName(form.getDisplayName());
        group.setDescription(StringUtils.trimToNull(form.getDescription()));
        
        this.workspaceService.modifyLocalGroup(group);
        
        // Update local group members
        for (Member member : form.getMembers()) {
            if (member.isAdded()) {
                if (!member.isDeleted()) {
                    // Don't add deleted member
                    this.workspaceService.addMemberToLocalGroup(workspaceId, cn, member.getId());
                }
            } else if (member.isDeleted()) {
                this.workspaceService.removeMemberFromLocalGroup(workspaceId, cn, member.getId());
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLocalGroup(PortalControllerContext portalControllerContext, String workspaceId, String id) throws PortletException {
        this.workspaceService.removeLocalGroup(workspaceId, id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroup createLocalGroup(PortalControllerContext portalControllerContext, LocalGroups localGroups, LocalGroup form) throws PortletException {
        CollabProfile group = workspaceService.createLocalGroup(localGroups.getWorkspaceId(), form.getDisplayName(), null);

        return this.createLocalGroup(group);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Member> getAllMembers(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Workspace members
        List<WorkspaceMember> workspaceMembers = this.workspaceService.getAllMembers(workspaceId);

        // Members
        List<Member> members;
        if (CollectionUtils.isEmpty(workspaceMembers)) {
            members = new ArrayList<Member>(0);
        } else {
            members = new ArrayList<Member>(workspaceMembers.size());
            for (WorkspaceMember workspaceMember : workspaceMembers) {
                Member member = this.createMember(workspaceMember.getMember());
                members.add(member);
            }
        }

        return members;
    }


    /**
     * Create local group.
     * 
     * @param group workspace group
     * @return local group
     */
    private LocalGroup createLocalGroup(CollabProfile group) {
        LocalGroupListItem localGroup;
        if (group == null) {
            localGroup = null;
        } else {
            localGroup = applicationContext.getBean(LocalGroupListItem.class);
            localGroup.setId(group.getCn());
            localGroup.setDisplayName(group.getDisplayName());
            localGroup.setDescription(group.getDescription());

            // Members count
            List<?> names = group.getUniqueMember();
            localGroup.setMembersCount(names.size());
        }

        return localGroup;
    }


    /**
     * Create member.
     * 
     * @param person person
     * @return member
     */
    private Member createMember(Person person) {
        Member member = this.applicationContext.getBean(Member.class);
        member.setId(person.getUid());
        member.setDisplayName(person.getDisplayName());
        member.setAvatar(person.getAvatar().getUrl());
        member.setMail(person.getMail());
        return member;
    }

}
