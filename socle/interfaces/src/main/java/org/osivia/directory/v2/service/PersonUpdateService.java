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

import org.osivia.directory.v2.model.ext.Avatar;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;

import javax.naming.Name;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Loïc Billon
 *
 */
public interface PersonUpdateService extends PersonService {


	/**
	 * Create a person
	 * @param p a person
	 */
	public void create(Person p);

	/**
	 * Update a person
	 * @param p a person
	 */
	public void update(Person p);

	/**
	 * Check if a password is correct
	 * @param currentPassword currentPassword
	 * @return authenticated or not
	 */
	public boolean verifyPassword(String uid, String currentPassword);


    /**
     * Check if a password validate some character rules.
     *
     * @param password password
     * @return error messages
     */
	Map<String, String> validatePasswordRules(String password);

	/**
	 * Get password rules informations: rule message and validated indicator
	 * @param password password
	 * @return informations
	 */
	Map<String, Boolean> getPasswordRulesInformation(String password);


    /**
	 * Update the password of a person
	 * @param p a person
	 * @param newPassword
	 */
	public void updatePassword(Person p, String newPassword);

	/**
	 * @param portalControllerContext
	 * @param p
	 * @param avatar
	 * @param properties
	 * @throws PortalException
	 */
	void update(PortalControllerContext portalControllerContext, Person p,
			Avatar avatar, Map<String, String> properties)
			throws PortalException;

	/**
	 * Delete a person and its relations (workspace profiles ...)
	 * @param userConsulte
	 */
	public void delete(Person userConsulte);

	/**
	 * Get a person and bypass the cache (used for updating profiles).
	 * @param dn the person DN
	 * @return the person
	 */
	public Person getPersonNoCache(Name dn);

	/**
	 * Return a list of persons with no connection date (has never logged on portal)
	 * @param creationDate
	 * @return list of person
	 */
	List<Person> findByNoConnectionDate(Date creationDate);

	/**
	 * Return a list of persons with a validity date less than the one passed
	 * @param d the date
	 * @return list of person
	 */
	public List<Person> findByValidityDate(Date d);


	/**
	 * Return a list of persons (filtered by connection date)
	 * @param d the date
	 * @param connectedOnly filter by connection date
	 * @return list of person
	 */
	public List<Person> findByCriteria(Person search, boolean connectedOnly);

	/**
	 * Return a list of persons who not logged in since the reference date
	 * @param referenceDate
	 * @return
	 */
	public List<Person> findInactives(Date referenceDate);

	
}
