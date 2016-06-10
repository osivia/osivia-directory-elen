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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;

import org.osivia.directory.v2.dao.CollabProfileDao;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceMemberImpl;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Impl of the workspace service
 * @author Loïc Billon
 * @since 4.4
 */
@Service("workspaceService")
public class WorkspaceServiceImpl implements WorkspaceService {


	@Autowired
	private ApplicationContext context;


	@Autowired
	private PersonService personService;


	@Autowired
	private CollabProfile sample;


	@Autowired
	private CollabProfileDao dao;


	@Override
    public List<CollabProfile> findByWorkspaceId(String workspaceId) {

		CollabProfile searchProfile = this.context.getBean(CollabProfile.class);
		searchProfile.setWorkspaceId(workspaceId);

		return this.findByCriteria(searchProfile);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<CollabProfile> findByCriteria(CollabProfile profile) {

		return this.dao.findByCriteria(profile);
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#getAllMembers(java.lang.String)
	 */
	@Override
	//@Cacheable(key = "#workspaceId", value = { "membersByWksCache" })
	public List<WorkspaceMember> getAllMembers(String workspaceId) {

		List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

		// Get the members
		List<Person> allPers = new ArrayList<Person>();
		for(CollabProfile cp : list) {
			if(cp.getType() == WorkspaceGroupType.space_group) {

				Person searchPers = this.context.getBean(Person.class);

				List<Name> profiles = new ArrayList<Name>();
				profiles.add(cp.getDn());
				searchPers.setPortalPersonProfile(profiles);

				// find all the members
				allPers = this.personService.findByCriteria(searchPers);

			}
		}

		// For each member, get his security group and local groups
		List<WorkspaceMember> members = new ArrayList<WorkspaceMember>();;
		for(Person p : allPers) {

			WorkspaceMemberImpl member = new WorkspaceMemberImpl(p);

			for(CollabProfile cp : list) {


				if(cp.getType() == WorkspaceGroupType.security_group) {
					if(cp.getUniqueMember().contains(p.getDn())) {
						member.setRole(cp.getRole());
					}
				}
				else if(cp.getType() == WorkspaceGroupType.local_group) {
					if(cp.getUniqueMember().contains(p.getDn())) {
						member.getLocalGroups().add(cp);
					}
				}
			}

			members.add(member);
		}

		return members;
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#create(java.lang.String, java.lang.String)
	 */
	@Override
	public void create(String workspaceId, String description, Person owner) {

		Map<WorkspaceRole, String> roles = new HashMap<WorkspaceRole, String>();
		for(WorkspaceRole role : WorkspaceRole.values()) {
            // TODO [CKR] : internationalize display name with role key
            String displayName = role.getId();

            roles.put(role, displayName);
		}

		this.create(workspaceId, description, roles, owner);
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#create(java.lang.String, java.lang.String)
	 */
	@Override
	public void create(String workspaceId, String description, Map<WorkspaceRole, String> roles, Person owner) {

		// Creation of the member group
		CollabProfile members = this.context.getBean(CollabProfile.class);
		String cn = workspaceId + "_members";
		members.setCn(cn);
		members.setWorkspaceId(workspaceId);
		members.setType(WorkspaceGroupType.space_group);
		members.setDescription(description);
		members.setDn(this.sample.buildDn(cn));

		this.dao.create(members);

		// The owner is a member of the workspace
		this.attachPerson(owner, members);

		// Création of security groups
		for(Map.Entry<WorkspaceRole, String> entry : roles.entrySet()) {
            WorkspaceRole role = entry.getKey();
            String displayName = entry.getValue();

			CollabProfile roleGroup = this.context.getBean(CollabProfile.class);
            String cnRole = workspaceId + "_" + role.getId();
			roleGroup.setCn(cnRole);
			roleGroup.setWorkspaceId(workspaceId);
			roleGroup.setType(WorkspaceGroupType.security_group);
            roleGroup.setRole(role);
            roleGroup.setDisplayName(displayName);
			roleGroup.setDn(this.sample.buildDn(cnRole));

			this.dao.create(roleGroup);

			// Define the owner
            if (WorkspaceRole.OWNER.equals(role)) {
				this.attachPerson(owner, roleGroup);
			}
		}

		// Creation of the pending group
		CollabProfile pending = this.context.getBean(CollabProfile.class);
		cn = workspaceId + "_pending";
		pending.setCn(cn);
		pending.setWorkspaceId(workspaceId);
		pending.setType(WorkspaceGroupType.pending_group);
		pending.setDn(this.sample.buildDn(cn));

		this.dao.create(pending);

	}


	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#create(java.lang.String, java.lang.String)
	 */
	@Override
	//@CacheEvict(key = "#workspaceId", value = "membersByWksCache")
	public void delete(String workspaceId) {


		List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

		List<Person> allPers = new ArrayList<Person>();
		for(CollabProfile cp : list) {
			if(cp.getType() == WorkspaceGroupType.space_group) {

				Person searchPers = this.context.getBean(Person.class);

				List<Name> profilesDn = new ArrayList<Name>();
				profilesDn.add(cp.getDn());
				searchPers.setPortalPersonProfile(profilesDn);

				// find all the members
				allPers = this.personService.findByCriteria(searchPers);

			}
		}

		// unlink all groups to all persons
		for(Person p : allPers) {
			for(CollabProfile cp : list) {
				this.detachPerson(p, cp);
			}
		}

		// remove all groups about this workspace
		for(CollabProfile cp : list) {

			this.dao.delete(cp);

		}

	}



	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#addOrModifyMember(javax.naming.Name, org.osivia.directory.v2.model.Workspace.WorkspaceRole)
	 */
	@Override
	//@CacheEvict(key = "#workspaceId", value = "membersByWksCache")
	public void addOrModifyMember(String workspaceId, Name memberDn, WorkspaceRole role) {


		List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

		for(CollabProfile cp : list) {

			// add this new member in the members group (if needed)
			if(cp.getType() == WorkspaceGroupType.space_group) {

				this.attachPerson(memberDn, cp);
			}

			if((cp.getType() == WorkspaceGroupType.security_group) && (cp.getRole() == role)) {
				this.attachPerson(memberDn, cp);
			}

			if((cp.getType() == WorkspaceGroupType.security_group) && (cp.getRole() != role)) {
				this.detachPerson(memberDn, cp);
			}

		}


	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#removeMember(javax.naming.Name)
	 */
	@Override
	//@CacheEvict(key = "#workspaceId", value = "membersByWksCache")
	public void removeMember(String workspaceId, Name memberDn) {

		List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

		for(CollabProfile cp : list) {

			this.detachPerson(memberDn, cp);
		}

	}

	private void attachPerson(Person person, CollabProfile profile) {

		if(!(profile.getUniqueMember().contains(person.getDn()))) {

			profile.getUniqueMember().add(person.getDn());

			this.dao.update(profile);
		}

		if(!(person.getPortalPersonProfile().contains(profile.getDn()))) {
			person.getPortalPersonProfile().add(profile.getDn());

			this.personService.update(person);
		}
	}

	private void attachPerson(Name memberDn, CollabProfile cp) {

		Person person = this.personService.getPerson(memberDn);
		this.attachPerson(person, cp);
	}

	/**
	 * @param p
	 * @param profile
	 */
	private void detachPerson(Person person, CollabProfile profile) {

		if(profile.getUniqueMember().contains(person.getDn())) {

			profile.getUniqueMember().remove(person.getDn());

			this.dao.update(profile);
		}

		if(person.getPortalPersonProfile().contains(profile.getDn())) {
			person.getPortalPersonProfile().remove(profile.getDn());

			this.personService.update(person);
		}

	}

	private void detachPerson(Name memberDn, CollabProfile cp) {

		Person person = this.personService.getPerson(memberDn);
		this.detachPerson(person, cp);
	}




	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#create(java.lang.String, java.lang.String)
	 */
	@Override
	public void createLocalGroup(String workspaceId, String description) {


		List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

		// search the max ID setted in the group and add 1.
		int i = 1;
		for(CollabProfile cp : list) {

			if(cp.getType() == WorkspaceGroupType.local_group) {
				String cpSuffix = cp.getCn().replace(workspaceId + "_", "");

				int parseInt = Integer.parseInt(cpSuffix);
				if(parseInt >= i) {
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
		localGroup.setDescription(description);
		localGroup.setDn(this.sample.buildDn(cn));

		this.dao.create(localGroup);
	}

	@Override
	public void addMemberToLocalGroup(String workspaceId, Name localGroupDn, Name member) {


		List<CollabProfile> list = this.findByWorkspaceId(workspaceId);

		for(CollabProfile cp : list) {
			if(cp.getType() == WorkspaceGroupType.space_group) {
				if (cp.getUniqueMember().contains(member)) {

					CollabProfile localGroup = this.dao.findByDn(localGroupDn);

					if(localGroup.getType() == WorkspaceGroupType.local_group) {
						this.attachPerson(member, localGroup);
					}

				}
			}
		}
	}

	@Override
	public void removeMemberFromLocalGroup(String workspaceId, Name localGroupDn, Name member) {

		CollabProfile localGroup = this.dao.findByDn(localGroupDn);
		if(localGroup.getType() == WorkspaceGroupType.local_group) {
			this.detachPerson(member, localGroup);
		}
	}

	@Override
	public void removeLocalGroup(String workspaceId, Name dn) {

		CollabProfile groupToRemove = this.dao.findByDn(dn);

		Person searchPers = this.context.getBean(Person.class);

		List<Name> profilesDn = new ArrayList<Name>();
		profilesDn.add(groupToRemove.getDn());
		searchPers.setPortalPersonProfile(profilesDn);

		// find all the members and unlink them
		List<Person> personToDetach = this.personService.findByCriteria(searchPers);
		for(Person p : personToDetach) {
			this.detachPerson(p, groupToRemove);
		}


		this.dao.delete(groupToRemove);

	}

}
