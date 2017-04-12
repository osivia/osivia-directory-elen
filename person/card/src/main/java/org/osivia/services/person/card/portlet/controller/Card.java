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
package org.osivia.services.person.card.portlet.controller;

import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.urls.Link;
import org.osivia.services.person.card.portlet.service.LevelChgPwd;
import org.osivia.services.person.card.portlet.service.LevelDeletion;
import org.osivia.services.person.card.portlet.service.LevelEdition;

/**
 * Main form object
 * @author Loïc Billon
 *
 */
public class Card {

	private NuxeoProfile nxProfile;

	private Person userConsulte;


	private LevelEdition levelEdition;

	private LevelDeletion levelDeletion;
	
	private LevelChgPwd levelChgPwd;
	
	
	private boolean self = false;
		
	private Link avatar;
	
	
	/**
	 * @return the nxProfile
	 */
	public NuxeoProfile getNxProfile() {
		return nxProfile;
	}

	/**
	 * @param nxProfile the nxProfile to set
	 */
	public void setNxProfile(NuxeoProfile nxProfile) {
		this.nxProfile = nxProfile;
	}

	public Person getUserConsulte() {
		return userConsulte;
	}
	public void setUserConsulte(Person userConsulte) {
		this.userConsulte = userConsulte;
	}

	

	/**
	 * @return the levelEdition
	 */
	public LevelEdition getLevelEdition() {
		return levelEdition;
	}

	/**
	 * @param levelEdition the levelEdition to set
	 */
	public void setLevelEdition(LevelEdition levelEdition) {
		this.levelEdition = levelEdition;
	}
	
	

	/**
	 * @return the levelDeletion
	 */
	public LevelDeletion getLevelDeletion() {
		return levelDeletion;
	}

	/**
	 * @param levelDeletion the levelDeletion to set
	 */
	public void setLevelDeletion(LevelDeletion levelDeletion) {
		this.levelDeletion = levelDeletion;
	}

	/**
	 * @return the levelChgPwd
	 */
	public LevelChgPwd getLevelChgPwd() {
		return levelChgPwd;
	}

	/**
	 * @param levelChgPwd the levelChgPwd to set
	 */
	public void setLevelChgPwd(LevelChgPwd levelChgPwd) {
		this.levelChgPwd = levelChgPwd;
	}

	public boolean isSelf() {
		return self;
	}
	public void setSelf(boolean self) {
		this.self = self;
	}


	/**
	 * @return the avatar
	 */
	public Link getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(Link avatar) {
		this.avatar = avatar;
	}


}
