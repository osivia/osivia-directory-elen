package org.osivia.services.directory.workspace.portlet.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.directory.workspace.portlet.model.AddForm;
import org.osivia.services.directory.workspace.portlet.model.MembersContainer;
import org.osivia.services.directory.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Workspace member management service implementation.
 *
 * @author Cédric Krommenhoek
 * @see MemberManagementService
 */
@Service
public class MemberManagementServiceImpl implements MemberManagementService {

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public MemberManagementServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MembersContainer getMembersContainer(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Members container
        MembersContainer container = new MembersContainer(workspaceId);

        // Members
        List<WorkspaceMember> members = this.workspaceService.getAllMembers(workspaceId);
        container.setMembers(members);

        return container;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PortalControllerContext portalControllerContext, MembersContainer container) throws PortletException {
        // Deleted member
        List<WorkspaceMember> deleted = new ArrayList<WorkspaceMember>();
        for (WorkspaceMember member : container.getMembers()) {
            if (member.isDeleted()) {
                deleted.add(member);
                this.workspaceService.removeMember(container.getWorkspaceId(), member.getMember().getDn());
            } else {
                this.workspaceService.addOrModifyMember(container.getWorkspaceId(), member.getMember().getDn(), member.getRole());
            }
        }
        container.getMembers().removeAll(deleted);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray searchMembers(PortalControllerContext portalControllerContext, String filter) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        Person search = this.personService.getEmptyPerson();
        search.setUid(filter + "*");
        search.setDisplayName(filter + "*");
        search.setSn(filter + "*");
        search.setGivenName(filter + "*");
        List<Person> persons = this.personService.findByCriteria(search);


        // Results JSON array
        JSONArray array = new JSONArray();

        for (Person p : persons) {
            JSONObject object = new JSONObject();
            object.put("id", p.getUid());
            object.put("displayName", p.getDisplayName());
            object.put("mail", p.getMail());
            object.put("avatar", p.getAvatar().getUrl());

            array.add(object);
        }

        // Create user
        JSONObject create = new JSONObject();
        create.put("id", filter);
        create.put("displayName", bundle.getString("CREATE_NEW_MEMBER", filter));
        create.put("extra", bundle.getString("CREATE_NEW_MEMBER_HELP"));
        create.put("create", true);
        array.add(create);

        return array;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, MembersContainer container, String name) throws PortletException {
        List<WorkspaceMember> members = container.getMembers();
        if (CollectionUtils.isNotEmpty(members)) {
            for (WorkspaceMember member : members) {
                if (StringUtils.equals(member.getMember().getUid(), name)) {
                    member.setDeleted(true);
                    break;
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void add(PortalControllerContext portalControllerContext, MembersContainer container, AddForm form) throws PortletException {
        if (CollectionUtils.isNotEmpty(form.getNames())) {
            for (String name : form.getNames()) {
                Person person = this.personService.getPerson(name);

                if (person == null) {
                    person = this.personService.getEmptyPerson();
                    person.setUid(name);
                    person.setSn(name);
                    person.setCn(name);
                    person.setDisplayName(name);
                    person.setMail(name);
                    this.personService.create(person);
                }

                WorkspaceMember newMember = this.workspaceService.addOrModifyMember(container.getWorkspaceId(), person.getDn(), form.getRole());

                boolean add = true;

                // Synchronisation avec la liste en session
                for (WorkspaceMember currentMember : container.getMembers()) {
                    if (currentMember.getMember().getUid().equals(newMember.getMember().getUid())) {
                        // Si changement de rôle, on retire le membre pour le rajouter avec son nouveau rôle
                        currentMember.setRole(form.getRole());
                        add = false;
                    }
                }
                if (add) {
                    container.getMembers().add(newMember);
                }
            }

            this.update(portalControllerContext, container);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkspaceRole> getAllowedRoles(String workspaceId) {
        List<WorkspaceRole> roles = new ArrayList<WorkspaceRole>();

        List<CollabProfile> profiles = this.workspaceService.findByWorkspaceId(workspaceId);
        for (CollabProfile cp : profiles) {
            if (cp.getType() == WorkspaceGroupType.security_group) {
                roles.add(cp.getRole());
            }
        }

        return roles;
    }

}
