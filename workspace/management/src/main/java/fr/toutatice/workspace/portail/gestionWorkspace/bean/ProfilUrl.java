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
package fr.toutatice.workspace.portail.gestionWorkspace.bean;

import fr.toutatice.outils.ldap.entity.Profil;

public class ProfilUrl implements Comparable<Object>{
	
	private Profil profil;
	private String url;
	public Profil getProfil() {
		return profil;
	}
	public void setProfil(Profil profil) {
		this.profil = profil;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public ProfilUrl(Profil profil, String url) {
		super();
		this.profil = profil;
		this.url = url;
	}
	
	public int compareTo(Object obj) {
		ProfilUrl p = (ProfilUrl) obj;
		return this.getProfil().getDisplayName().toLowerCase().compareTo(p.getProfil().getDisplayName().toLowerCase());
	}

}
