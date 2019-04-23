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
package org.osivia.directory.v2.dao;

import java.util.Date;
import java.util.List;

import javax.naming.Name;

import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.ldap.NameNotFoundException;

/**
 * @author Loïc Billon
 *
 */
public interface PersonDao {

	/**
	 * @param dn
	 * @return
	 * @throws NameNotFoundException 
	 */
	Person getPerson(Name dn) throws NameNotFoundException;

	/**
	 * @param ps
	 * @return
	 */
	List<Person> findByCriteria(Person p);

	/**
	 * @param p
	 */
	void create(Person p);

	/**
	 * @param p
	 */
	void update(Person p);

	/**
	 * @param uid
	 * @param currentPassword
	 * @return
	 */
	boolean verifyPassword(String uid, String currentPassword);

	/**
	 * @param p
	 * @param newPassword
	 */
	void updatePassword(Person p, String newPassword);

	/**
	 * @param userConsulte
	 */
	void delete(Person userConsulte);
	
	/**
	 * Get a person and bypass the cache (used for updating profiles).
	 * @param dn the person DN
	 * @return the person
	 */
	Person getPersonNoCache(Name dn);

	/**
 	 * Return a list of persons with no connection date (has never logged on portal)
 	 * @param p the person search
	 * @return
	 */
	List<Person> findByNoConnectionDate(Person p);
	
	/**
	 * Return a list of persons with a validity date less than the one passed 
	 * @param d the date
	 * @return list of person
	 */	
	List<Person> findByValidityDate(Date d);

}
