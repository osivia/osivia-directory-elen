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

import java.util.List;

import javax.naming.Name;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.directory.v2.dao.PersonDao;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;

/**
 * Impl of the person service
 * @author Loïc Billon
 * @since 4.4
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {

	private final static Log logger = LogFactory.getLog(PersonServiceImpl.class);

	private final INuxeoService nuxeoService = Locator.findMBean(INuxeoService.class, INuxeoService.MBEAN_NAME);
	
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Person sample;
	
	@Autowired
	private PersonDao dao;

	
	@Override
	public Person getEmptyPerson() {
		return context.getBean(Person.class);
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
			logger.warn("Person with dn "+dn+" not found");
			return null;
		}

		return p;
		
	}

	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#getPerson(java.lang.String)
	 */
	@Override
	public Person getPerson(String uid) {

		Name dn = sample.buildDn(uid);;

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
		
	}

	/* (non-Javadoc)
	 * @see org.osivia.portal.api.directory.v2.service.PersonService#update(org.osivia.portal.api.directory.v2.model.Person)
	 */
	@Override
	public void update(Person p) {
		dao.update(p);
		
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
	 * @param p the person
	 */
	private void appendAvatar(Person p) {
				
		// 	Append avatar
		INuxeoCustomizer cmsCustomizer = nuxeoService.getCMSCustomizer();

        Link userAvatar = cmsCustomizer.getUserAvatar(p.getUid());
        p.setAvatar(userAvatar);

	}
	
}
