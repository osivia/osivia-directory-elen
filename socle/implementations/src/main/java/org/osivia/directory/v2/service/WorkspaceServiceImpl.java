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
	public CollabProfile getProfile(String cn) {
		Name dn = sample.buildDn(cn);
		
		return getProfile(dn);
	}

	@Override
	public CollabProfile getProfile(Name dn) {
		return dao.findByDn(dn);
	}
	
	@Override
	public List<CollabProfile> findByWorkspaceId(String workspaceId) {

		CollabProfile searchProfile = context.getBean(CollabProfile.class);
		searchProfile.setWorkspaceId(workspaceId);
		
		return findByCriteria(searchProfile);
	}
	
	@SuppressWarnings("unchecked")
	public List<CollabProfile> findByCriteria(CollabProfile profile) {
		
		return dao.findByCriteria(profile);
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#getAllMembers(java.lang.String)
	 */
	@Override
	//@Cacheable(key = "#workspaceId", value = { "membersByWksCache" })
	public List<WorkspaceMember> getAllMembers(String workspaceId) {
		
		List<CollabProfile> list = findByWorkspaceId(workspaceId);
		
		// Get the members
		List<Person> allPers = new ArrayList<Person>();
		for(CollabProfile cp : list) {
			if(cp.getType() == WorkspaceGroupType.space_group) {
				
				Person searchPers = context.getBean(Person.class);
				
				List<Name> profiles = new ArrayList<Name>();
				profiles.add(cp.getDn());
				searchPers.setPortalPersonProfile(profiles);
				
				// find all the members
				allPers = personService.findByCriteria(searchPers);
				
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
	public void create(String workspaceId, Person owner) {

		
		create(workspaceId, Arrays.asList(WorkspaceRole.values()), owner);
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#create(java.lang.String, java.lang.String)
	 */
	@Override
	public void create(String workspaceId, List<WorkspaceRole> roles, Person owner) {
		
		// Creation of the member group
		CollabProfile members = context.getBean(CollabProfile.class);
		String cn = workspaceId + "_members";
		members.setCn(cn);
		members.setWorkspaceId(workspaceId);
		members.setType(WorkspaceGroupType.space_group);
		members.setDn(sample.buildDn(cn));
		
		dao.create(members);
		
		// The owner is a member of the workspace
		attachPerson(owner, members);
		
		// Création of security groups
		for(WorkspaceRole entry : roles) {
		
			
			CollabProfile roleGroup = context.getBean(CollabProfile.class);
			String cnRole = workspaceId + "_" + entry.getId();
			roleGroup.setCn(cnRole);
			roleGroup.setWorkspaceId(workspaceId);
			roleGroup.setType(WorkspaceGroupType.security_group);
			roleGroup.setRole(entry);
			roleGroup.setDn(sample.buildDn(cnRole));
			
			dao.create(roleGroup);
			
			// Define the owner
            if (WorkspaceRole.OWNER.equals(entry)) {
				this.attachPerson(owner, roleGroup);
			}
		}
		
		// Creation of the pending group
		CollabProfile pending = context.getBean(CollabProfile.class);
		cn = workspaceId + "_pending";
		pending.setCn(cn);
		pending.setWorkspaceId(workspaceId);
		pending.setType(WorkspaceGroupType.pending_group);
		pending.setDn(sample.buildDn(cn));
		
		dao.create(pending);

	}
	
	
	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#create(java.lang.String, java.lang.String)
	 */
	@Override
	//@CacheEvict(key = "#workspaceId", value = "membersByWksCache")
	public void delete(String workspaceId) {

		
		List<CollabProfile> list = findByWorkspaceId(workspaceId);
		
		List<Person> allPers = new ArrayList<Person>();
		for(CollabProfile cp : list) {
			if(cp.getType() == WorkspaceGroupType.space_group) {
				
				Person searchPers = context.getBean(Person.class);
				
				List<Name> profilesDn = new ArrayList<Name>();
				profilesDn.add(cp.getDn());
				searchPers.setPortalPersonProfile(profilesDn);
				
				// find all the members
				allPers = personService.findByCriteria(searchPers);

			}
		}
		
		// unlink all groups to all persons		
		for(Person p : allPers) {
			for(CollabProfile cp : list) {
				detachPerson(p, cp);
			}
		}
		
		// remove all groups about this workspace
		for(CollabProfile cp : list) {
			
			dao.delete(cp);
			
		}
		
	}



	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#addOrModifyMember(javax.naming.Name, org.osivia.directory.v2.model.Workspace.WorkspaceRole)
	 */
	@Override
	//@CacheEvict(key = "#workspaceId", value = "membersByWksCache")
	public WorkspaceMember addOrModifyMember(String workspaceId, Name memberDn, WorkspaceRole role) {

		
		List<CollabProfile> list = findByWorkspaceId(workspaceId);
		
		for(CollabProfile cp : list) {
			
			// add this new member in the members group (if needed)
			if(cp.getType() == WorkspaceGroupType.space_group) {
				
				attachPerson(memberDn, cp);
			}
			
			if(cp.getType() == WorkspaceGroupType.security_group && cp.getRole() == role) {
				attachPerson(memberDn, cp);
			}
			
			if(cp.getType() == WorkspaceGroupType.security_group && cp.getRole() != role) {
				detachPerson(memberDn, cp);
			}
			
		}

		Person person = personService.getPerson(memberDn);
		WorkspaceMemberImpl member = new WorkspaceMemberImpl(person);
		member.setRole(role);
		
		return member;
		
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#removeMember(javax.naming.Name)
	 */
	@Override
	//@CacheEvict(key = "#workspaceId", value = "membersByWksCache")
	public void removeMember(String workspaceId, Name memberDn) {
		
		List<CollabProfile> list = findByWorkspaceId(workspaceId);
		
		for(CollabProfile cp : list) {

			detachPerson(memberDn, cp);
		}

	}

	private void attachPerson(Person person, CollabProfile profile) {
		
		if(!(profile.getUniqueMember().contains(person.getDn()))) {
			
			profile.getUniqueMember().add(person.getDn());
			
			dao.update(profile);
		}
		
		if(!(person.getPortalPersonProfile().contains(profile.getDn()))) {
			person.getPortalPersonProfile().add(profile.getDn());
			
			personService.update(person);
		}
	}
	
	private void attachPerson(Name memberDn, CollabProfile cp) {
		
		Person person = personService.getPerson(memberDn);
		attachPerson(person, cp);
	}
	
	/**
	 * @param p
	 * @param profile
	 */
	private void detachPerson(Person person, CollabProfile profile) {

		if(profile.getUniqueMember().contains(person.getDn())) {
			
			profile.getUniqueMember().remove(person.getDn());
			
			dao.update(profile);
		}
		
		if(person.getPortalPersonProfile().contains(profile.getDn())) {
			person.getPortalPersonProfile().remove(profile.getDn());
			
			personService.update(person);
		}
		
	}
	
	private void detachPerson(Name memberDn, CollabProfile cp) {
		
		Person person = personService.getPerson(memberDn);
		detachPerson(person, cp);
	}	



	
	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.WorkspaceService#create(java.lang.String, java.lang.String)
	 */
	@Override
	public CollabProfile createLocalGroup(String workspaceId, String displayName, String description) {
		
		
		List<CollabProfile> list = findByWorkspaceId(workspaceId);
		
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
		CollabProfile localGroup = context.getBean(CollabProfile.class);
		String cn = workspaceId + "_" + Integer.toString(i);
		localGroup.setCn(cn);
		localGroup.setWorkspaceId(workspaceId);
		localGroup.setType(WorkspaceGroupType.local_group);
		localGroup.setDisplayName(displayName);
		if(StringUtils.isNotBlank(description)) {
			localGroup.setDescription(description);
		}
		localGroup.setDn(sample.buildDn(cn));
		
		dao.create(localGroup);
		
		return localGroup;
	}
	
	@Override
	public void addMemberToLocalGroup(String workspaceId, Name localGroupDn, Name member) {
		
		
		List<CollabProfile> list = findByWorkspaceId(workspaceId);
		
		for(CollabProfile cp : list) {
			if(cp.getType() == WorkspaceGroupType.space_group) {
				if (cp.getUniqueMember().contains(member)) {
					
					CollabProfile localGroup = dao.findByDn(localGroupDn);
					
					if(localGroup.getType() == WorkspaceGroupType.local_group) {
						attachPerson(member, localGroup);
					}
					
				}
			}
		}
	}
	
	@Override
	public void removeMemberFromLocalGroup(String workspaceId, Name localGroupDn, Name member) {
		
		CollabProfile localGroup = dao.findByDn(localGroupDn);
		if(localGroup.getType() == WorkspaceGroupType.local_group) {
			detachPerson(member, localGroup);
		}
	}
	
	@Override
	public void removeLocalGroup(String workspaceId, Name dn) {
		
		CollabProfile groupToRemove = dao.findByDn(dn);
		
		Person searchPers = context.getBean(Person.class);
		
		List<Name> profilesDn = new ArrayList<Name>();
		profilesDn.add(groupToRemove.getDn());
		searchPers.setPortalPersonProfile(profilesDn);
		
		// find all the members and unlink them
		List<Person> personToDetach = personService.findByCriteria(searchPers);
		for(Person p : personToDetach) {
			detachPerson(p, groupToRemove);
		}
		
		
		dao.delete(groupToRemove);
		
	}

}
