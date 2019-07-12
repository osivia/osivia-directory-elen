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
package org.osivia.services.person.card.portlet.service;

import org.dom4j.Element;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.person.card.portlet.controller.Card;
import org.osivia.services.person.card.portlet.controller.FormChgPwd;
import org.osivia.services.person.card.portlet.controller.FormEdition;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * @author Loïc Billon
 *
 */
public interface PersonCardService {

	/**
	 * Return the hability of edit a person
	 * @param userConnecte
	 * @param userConsulte
	 * @return
	 */
	public LevelEdition findLevelEdition(Person userConnecte, Person userConsulte);

	/**
	 * Return the hability of delete a person
	 * @param userConnecte
	 * @param userConsulte
	 * @return
	 */
	public LevelDeletion findLevelDeletion(Person userConnecte, Person userConsulte);

	/**
	 * Return the hability of change the user password
	 * @param userConnecte
	 * @param userConsulte
	 * @return
	 */
	public LevelChgPwd findLevelChgPwd(Person userConnecte, Person userConsulte);


	/**
	 * Load a person card (LDAP and nuxeo)
	 * @param context
	 * @return
	 * @throws PortalException
	 */
	Card loadCard(PortalControllerContext context) throws PortalException;

	/**
	 * Upload and store a temporary file for new avatar
	 * @param portalControllerContext
	 * @param form
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void uploadAvatar(PortalControllerContext portalControllerContext,
			FormEdition form) throws IllegalStateException, IOException;

	/**
	 * Remove avatar
	 * @param portalControllerContext
	 * @param form
	 */
	public void deleteAvatar(PortalControllerContext portalControllerContext,
			FormEdition form);

	/**
	 * Save all modifications
	 * @param portalControllerContext
	 * @param card
	 * @param form
	 * @throws PortalException
	 */
	public void saveCard(PortalControllerContext portalControllerContext,
			Card card, FormEdition form) throws PortalException;


    /**
     * Submit a password change.
     *
     * @param portalControllerContext portal controller context
     * @param card                    person card
     * @param formChgPwd              form
     * @return true if password has been changed
     */
    boolean changePassword(PortalControllerContext portalControllerContext, Card card, FormChgPwd formChgPwd);


    /**
     * Validate password rules.
     *
     * @param errors   validation errors
     * @param field    password field name
     * @param password password value
     */
    void validatePasswordRules(Errors errors, String field, String password);


	/**
	 * Get password rules informations DOM element.
	 *
	 * @param portalControllerContext portal controller context
	 * @param password                password, may be null
	 * @return DOM element
	 */
	Element getPasswordRulesInformation(PortalControllerContext portalControllerContext, String password) throws PortletException;


	/**
	 * Submit a passowrd change (for admins)
	 * @param card
	 * @param formChgPwd
	 */
	public void overwritePassword(Card card, FormChgPwd formChgPwd);

	/**
	 * Delete a person
	 * @param card
	 */
	public void deletePerson(Card card);

	/**
	 * Leave a workspace
	 * @param portalControllerContext
	 * @param card
	 * @param workspaceId
	 */
	public void exit(PortalControllerContext portalControllerContext, Card card, String workspaceId);







}
