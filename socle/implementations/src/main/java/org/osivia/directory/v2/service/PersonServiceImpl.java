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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.dao.PersonDao;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.Avatar;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.repository.GetUserProfileCommand;
import org.osivia.directory.v2.repository.UpdateUserProfileCommand;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.core.cms.CMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;

/**
 * Impl of the person service
 * @author Loïc Billon
 * @since 4.4
 */
@Service("personService")
public class PersonServiceImpl extends LdapServiceImpl implements PersonUpdateService {

	private final static Log ldapLogger = LogFactory.getLog("org.osivia.directory.v2");

	private static final String CARD_INSTANCE = "directory-person-card-instance";

	
	@Autowired
	protected ApplicationContext context;
	
	@Autowired
	protected Person sample;
	
	@Autowired
	protected PersonDao dao;
	
	@Autowired
	protected WorkspaceService workspaceService;

	@Autowired
	protected IPortalUrlFactory urlFactory;
	
	@Override
	public Person getEmptyPerson() {
		return context.getBean(sample.getClass());
	}
	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#getPerson(javax.naming.Name)
	 */
	@Override
	public Person getPerson(Name dn) {
		
		Person p;
		try {
			p = dao.getPerson(dn);
			appendAvatar(p);
			
		} catch (NameNotFoundException e) {
			ldapLogger.warn("Person with dn "+dn+" not found");
			return null;
		}

		return p;
		
	}
	
	@Override
	public Person getPersonNoCache(Name dn) {
		
		Person p;
		try {
			p = dao.getPersonNoCache(dn);
			appendAvatar(p);
			
		} catch (NameNotFoundException e) {
			ldapLogger.warn("Person with dn "+dn+" not found");
			return null;
		}

		return p;
		
	}

	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#getPerson(java.lang.String)
	 */
	@Override
	public Person getPerson(String uid) {

        Name dn = sample.buildDn(uid);

		return getPerson(dn);
	}
	
	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#getPerson(java.lang.String)
	 */
	@Override
	public List<Person> findByCriteria(Person search) {

		List<Person> persons = dao.findByCriteria(search);
		for(Person p : persons) {
			appendAvatar(p);
		}
		return persons;
	}

	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#update(org.osivia.portal.api.directory.v2.model.Person)
	 */
	@Override
	public void create(Person p) {
		
		dao.create(p);
		
		ldapLogger.info("Person created : "+p.getUid());
		
	}

	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#update(org.osivia.portal.api.directory.v2.model.Person)
	 */
	@Override
	public void update(Person p) {
		dao.update(p);
		
		ldapLogger.info("Person updated : "+p.getUid());
		
	}
	
	@Transactional
	public void update(PortalControllerContext portalControllerContext, Person p, Avatar avatar, Map<String, String> properties) throws PortalException {
		
		update(p);
		
		NuxeoController controller = new NuxeoController(portalControllerContext);
		
		Document nuxeoProfile = (Document) getEcmProfile(portalControllerContext, p);
		
		UpdateUserProfileCommand updateCmd = new UpdateUserProfileCommand(nuxeoProfile,properties, avatar);
		controller.executeNuxeoCommand(updateCmd);
		
		if(avatar != null) {
			controller.refreshUserAvatar(p.getUid());
		}
	}
	

	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#verifyPassword(java.lang.String)
	 */
	@Override
	public boolean verifyPassword(String uid, String currentPassword) {
		
		return dao.verifyPassword(uid, currentPassword);
		
	}	
	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#updatePassword(org.osivia.portal.api.directory.v2.model.Person, java.lang.String)
	 */
	@Override
	public void updatePassword(Person p, String newPassword) {

		dao.updatePassword(p, newPassword);
	}
	
	/**
	 * Get avatar url for a person
	 * @param person the person
	 */
	protected void appendAvatar(Person person) {
		// 	Append avatar
		INuxeoService nuxeoService = Locator.findMBean(INuxeoService.class, INuxeoService.MBEAN_NAME);
		INuxeoCustomizer cmsCustomizer = nuxeoService.getCMSCustomizer();
		
        Link userAvatar = new Link("", false);
		try {
			userAvatar = cmsCustomizer.getUserAvatar(null, person.getUid());
		} catch (CMSException e) {
			
		}
        person.setAvatar(userAvatar);
	}
	
	/**
	 * Generate a portlet url for person card
	 */
	public Link getCardUrl(PortalControllerContext portalControllerContext, Person person) throws PortalException {
		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		windowProperties.put("osivia.title", person.getDisplayName());
		windowProperties.put("uidFichePersonne", person.getUid());
		
        String url = urlFactory.getStartPortletUrl(portalControllerContext, CARD_INSTANCE, windowProperties, PortalUrlType.DEFAULT);
        return new Link(url,false);
	}
	
	
	@Override
	public Object getEcmProfile(
			PortalControllerContext portalControllerContext, Person person)
			throws PortalException {
		NuxeoController controller = new NuxeoController(portalControllerContext);
		
		return controller.executeNuxeoCommand(new GetUserProfileCommand(person.getUid()));
		
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.PersonUpdateService#delete(org.osivia.portal.api.directory.v2.model.Person)
	 */
	@Override
	public void delete(Person userConsulte) {
		
		CollabProfile searchProfiles = workspaceService.getEmptyProfile();
		searchProfiles.setType(WorkspaceGroupType.space_group);
		List<Name> name = new ArrayList<Name>();
		name.add(userConsulte.getDn());
		searchProfiles.setUniqueMember(name);
		
		List<CollabProfile> spaces = workspaceService.findByCriteria(searchProfiles);
		for(CollabProfile space : spaces) {
			workspaceService.removeMember(space.getWorkspaceId(), userConsulte.getDn());
		}
		
		dao.delete(userConsulte);
		
		ldapLogger.info("Person deleted : "+userConsulte.getUid());

		
	}

	@Override
	public List<Person> findByNoConnectionDate(Person p) {
		return dao.findByNoConnectionDate(p);
	}

	/* (non-Javadoc)
	 * @see org.osivia.directory.v2.service.PersonUpdateService#findByValidityDate(java.util.Date)
	 */
	@Override
	public List<Person> findByValidityDate(Date d) {
		return dao.findByValidityDate(d);
	}
}
