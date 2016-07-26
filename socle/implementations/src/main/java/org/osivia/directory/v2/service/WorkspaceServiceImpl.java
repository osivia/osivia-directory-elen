/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.osivia.directory.v2.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Name;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.dao.CollabProfileDao;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceMemberImpl;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Implementation of the workspace service.
 *
 * @author Loïc Billon
 * @since 4.4
 * @see WorkspaceService
 */
@Service("workspaceService")
public class WorkspaceServiceImpl implements WorkspaceService {

    /** Application context. */
    @Autowired
    private ApplicationContext context;

    /** Person service. */
    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    /** Collab profile sample. */
    @Autowired
    @Qualifier("collabProfile")
    private CollabProfile sample;

    /** Collab profile DAO. */
    @Autowired
    @Qualifier("collabProfileDao")
    private CollabProfileDao dao;


    /**
     * Constructor.
     */
    public WorkspaceServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CollabProfile getEmptyProfile() {
        return this.context.getBean(CollabProfile.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CollabProfile getProfile(String cn) {
        Name dn = this.getSample().buildDn(cn);

        return this.getProfile(dn);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CollabProfile getProfile(Name dn) {
        return this.getDao().findByDn(dn);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollabProfile> findByWorkspaceId(String workspaceId) {
        CollabProfile searchProfile = this.context.getBean(CollabProfile.class);
        searchProfile.setWorkspaceId(workspaceId);

        return this.findByCriteria(searchProfile);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<CollabProfile> findByCriteria(CollabProfile profile) {
        return this.getDao().findByCriteria(profile);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    // @Cacheable(key = "#workspaceId", value = { "membersByWksCache" })
    public List<WorkspaceMember> getAllMembers(String workspaceId) {
        List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

        // Get the members
        List<Person> allPers = new ArrayList<Person>();
        for (CollabProfile cp : list) {
            if (cp.getType() == WorkspaceGroupType.space_group) {
                Person searchPers = this.context.getBean(Person.class);

                List<Name> profiles = new ArrayList<Name>();
                profiles.add(cp.getDn());
                searchPers.setProfiles(profiles);

                // find all the members
                allPers = this.getPersonService().findByCriteria(searchPers);
            }
        }

        // For each member, get his security group and local groups
        List<WorkspaceMember> members = new ArrayList<WorkspaceMember>();
        for (Person p : allPers) {
            WorkspaceMemberImpl member = new WorkspaceMemberImpl(p);

            for (CollabProfile cp : list) {
                if (cp.getType() == WorkspaceGroupType.security_group) {
                    if (cp.getUniqueMember().contains(p.getDn())) {
                        member.setRole(cp.getRole());
                    }
                } else if (cp.getType() == WorkspaceGroupType.local_group) {
                    if (cp.getUniqueMember().contains(p.getDn())) {
                        member.getLocalGroups().add(cp);
                    }
                }
            }

            members.add(member);
        }

        return members;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WorkspaceMember getMember(String workspaceId, String uid) {
        WorkspaceMember result = null;

        // Workspace members
        List<WorkspaceMember> members = this.getAllMembers(workspaceId);

        for (WorkspaceMember member : members) {
            if (StringUtils.equals(member.getMember().getUid(), uid)) {
                result = member;
                break;
            }
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(String workspaceId, Person owner) {
        this.create(workspaceId, Arrays.asList(WorkspaceRole.values()), owner);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(String workspaceId, List<WorkspaceRole> roles, Person owner) {
        // Creation of the member group
        CollabProfile members = this.context.getBean(CollabProfile.class);
        String cn = workspaceId + "_members";
        members.setCn(cn);
        members.setWorkspaceId(workspaceId);
        members.setType(WorkspaceGroupType.space_group);
        members.setDn(this.getSample().buildDn(cn));

        this.getDao().create(members);

        // The owner is a member of the workspace
        this.attachPerson(owner, members);

        // Création of security groups
        for (WorkspaceRole entry : roles) {
            CollabProfile roleGroup = this.context.getBean(CollabProfile.class);
            String cnRole = workspaceId + "_" + entry.getId();
            roleGroup.setCn(cnRole);
            roleGroup.setWorkspaceId(workspaceId);
            roleGroup.setType(WorkspaceGroupType.security_group);
            roleGroup.setRole(entry);
            roleGroup.setDn(this.getSample().buildDn(cnRole));

            this.getDao().create(roleGroup);

            // Define the owner
            if (WorkspaceRole.OWNER.equals(entry)) {
                this.attachPerson(owner, roleGroup);
            }
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    // @CacheEvict(key = "#workspaceId", value = "membersByWksCache")
    public void delete(String workspaceId) {
        List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

        List<Person> allPers = new ArrayList<Person>();
        for (CollabProfile cp : list) {
            if (cp.getType() == WorkspaceGroupType.space_group) {
                Person searchPers = this.context.getBean(Person.class);

                List<Name> profilesDn = new ArrayList<Name>();
                profilesDn.add(cp.getDn());
                searchPers.setProfiles(profilesDn);

                // find all the members
                allPers = this.getPersonService().findByCriteria(searchPers);
            }
        }

        // unlink all groups to all persons
        for (Person p : allPers) {
            for (CollabProfile cp : list) {
                this.detachPerson(p, cp);
            }
        }

        // remove all groups about this workspace
        for (CollabProfile cp : list) {
            this.getDao().delete(cp);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    // @CacheEvict(key = "#workspaceId", value = "membersByWksCache")
    public WorkspaceMember addOrModifyMember(String workspaceId, Name memberDn, WorkspaceRole role) {
        List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

        for (CollabProfile cp : list) {
            // add this new member in the members group (if needed)
            if (cp.getType() == WorkspaceGroupType.space_group) {
                this.attachPerson(memberDn, cp);
            }

            if ((cp.getType() == WorkspaceGroupType.security_group) && (cp.getRole() == role)) {
                this.attachPerson(memberDn, cp);
            }

            if ((cp.getType() == WorkspaceGroupType.security_group) && (cp.getRole() != role)) {
                this.detachPerson(memberDn, cp);
            }
        }

        Person person = this.getPersonService().getPerson(memberDn);
        WorkspaceMemberImpl member = new WorkspaceMemberImpl(person);
        member.setRole(role);

        return member;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    // @CacheEvict(key = "#workspaceId", value = "membersByWksCache")
    public void removeMember(String workspaceId, Name memberDn) {
        List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

        for (CollabProfile cp : list) {
            this.detachPerson(memberDn, cp);
        }
    }


    /**
     * Attach person to a collab profile.
     * 
     * @param person person
     * @param profile collab profile
     */
    private void attachPerson(Person person, CollabProfile profile) {
        if (!(profile.getUniqueMember().contains(person.getDn()))) {
            profile.getUniqueMember().add(person.getDn());

            this.getDao().update(profile);
        }

        if (!(person.getProfiles().contains(profile.getDn()))) {
            person.getProfiles().add(profile.getDn());

            this.getPersonService().update(person);
        }
    }


    /**
     * Attach person to a collab profile.
     * 
     * @param memberDn member DN
     * @param cp collab profile
     */
    private void attachPerson(Name memberDn, CollabProfile cp) {
        Person person = this.getPersonService().getPerson(memberDn);
        this.attachPerson(person, cp);
    }


    /**
     * Detach person from a collab profile.
     * 
     * @param p person
     * @param profile collab profile
     */
    private void detachPerson(Person person, CollabProfile profile) {
        if (profile.getUniqueMember().contains(person.getDn())) {

            profile.getUniqueMember().remove(person.getDn());

            this.getDao().update(profile);
        }

        if (person.getProfiles().contains(profile.getDn())) {
            person.getProfiles().remove(profile.getDn());

            this.getPersonService().update(person);
        }
    }


    /**
     * Detach person from a collab profile.
     * 
     * @param memberDn member DN
     * @param cp collab profile
     */
    private void detachPerson(Name memberDn, CollabProfile cp) {
        Person person = this.getPersonService().getPerson(memberDn);
        this.detachPerson(person, cp);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CollabProfile createLocalGroup(String workspaceId, String displayName, String description) {
        List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

        // search the max ID setted in the group and add 1.
        int i = 1;
        for (CollabProfile cp : list) {
            if (cp.getType() == WorkspaceGroupType.local_group) {
                String cpSuffix = cp.getCn().replace(workspaceId + "_", "");

                int parseInt = Integer.parseInt(cpSuffix);
                if (parseInt >= i) {
                    i = parseInt + 1;
                }
            }
        }

        // local group creation
        CollabProfile localGroup = this.context.getBean(CollabProfile.class);
        String cn = workspaceId + "_" + Integer.toString(i);
        localGroup.setCn(cn);
        localGroup.setWorkspaceId(workspaceId);
        localGroup.setType(WorkspaceGroupType.local_group);
        localGroup.setDisplayName(displayName);
        if (StringUtils.isNotBlank(description)) {
            localGroup.setDescription(description);
        }
        localGroup.setDn(this.getSample().buildDn(cn));

        this.getDao().create(localGroup);

        return localGroup;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMemberToLocalGroup(String workspaceId, Name localGroupDn, Name memberDn) {
        List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

        for (CollabProfile cp : list) {
            if (cp.getType() == WorkspaceGroupType.space_group) {
                if (cp.getUniqueMember().contains(memberDn)) {
                    CollabProfile localGroup = this.dao.findByDn(localGroupDn);

                    if (localGroup.getType() == WorkspaceGroupType.local_group) {
                        this.attachPerson(memberDn, localGroup);
                    }
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMemberToLocalGroup(String workspaceId, String localGroupCn, String memberUid) {
        Name localGroupDn = this.getEmptyProfile().buildDn(localGroupCn);
        Name memberDn = this.getPersonService().getEmptyPerson().buildDn(memberUid);
        this.addMemberToLocalGroup(workspaceId, localGroupDn, memberDn);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMemberFromLocalGroup(String workspaceId, Name localGroupDn, Name memberDn) {
        CollabProfile localGroup = this.getDao().findByDn(localGroupDn);
        if (localGroup.getType() == WorkspaceGroupType.local_group) {
            this.detachPerson(memberDn, localGroup);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMemberFromLocalGroup(String workspaceId, String localGroupCn, String memberUid) {
        Name localGroupDn = this.getEmptyProfile().buildDn(localGroupCn);
        Name memberDn = this.getPersonService().getEmptyPerson().buildDn(memberUid);
        this.removeMemberFromLocalGroup(workspaceId, localGroupDn, memberDn);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyLocalGroup(CollabProfile localGroup) {
        this.getDao().update(localGroup);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLocalGroup(String workspaceId, Name dn) {
        CollabProfile groupToRemove = this.getDao().findByDn(dn);

        Person searchPers = this.context.getBean(Person.class);

        List<Name> profilesDn = new ArrayList<Name>();
        profilesDn.add(groupToRemove.getDn());
        searchPers.setProfiles(profilesDn);

        // find all the members and unlink them
        List<Person> personToDetach = this.getPersonService().findByCriteria(searchPers);
        for (Person p : personToDetach) {
            this.detachPerson(p, groupToRemove);
        }

        this.getDao().delete(groupToRemove);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLocalGroup(String workspaceId, String cn) {
        Name dn = this.getSample().buildDn(cn);
        this.removeLocalGroup(workspaceId, dn);
    }
    
    
    protected PersonService getPersonService() {
    	return personService;
    }

    protected CollabProfile getSample() {
    	return sample;
    }
    
    protected CollabProfileDao getDao() {
    	return dao;
    }
}
