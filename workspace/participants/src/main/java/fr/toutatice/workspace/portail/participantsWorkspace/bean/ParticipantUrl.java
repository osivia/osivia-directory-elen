/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/) and others.
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
 *
 *
 * Contributors:
 *  aguihomat
 * 
 *    
 */
package fr.toutatice.workspace.portail.participantsWorkspace.bean;

import org.osivia.portal.api.urls.Link;





public class ParticipantUrl implements Comparable{

	private String nom;
	private String url;
	private Link avatar;
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ParticipantUrl(String nom, String url) {
		super();
		this.nom = nom;
		this.url = url;
	}
	
	public ParticipantUrl(String nom, String url, Link avatar) {
		super();
		this.nom = nom;
		this.url = url;
		this.avatar = avatar;
	}
	
	public int compareTo(Object o) {
		ParticipantUrl p = (ParticipantUrl) o;
		return this.nom.compareToIgnoreCase(p.nom);
		
	}
	/**
	 * @return the avatar
	 */
	public Link getAvatar() {
		return avatar;
	}
	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Link avatar) {
		this.avatar = avatar;
	}
	
	
	
	
}
