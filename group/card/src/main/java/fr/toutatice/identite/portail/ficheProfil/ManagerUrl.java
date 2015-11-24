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
package fr.toutatice.identite.portail.ficheProfil;



public class ManagerUrl implements Comparable{

	private String displayName;
	private String id;
	private fr.toutatice.identite.portail.ficheProfil.ManagerStatut.statut statut;
	private String url;
	private String dn;
	private boolean droitDelete;
	private boolean clicable;
	private String logo;
	private boolean modifiable;
	
//	private fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut statut;	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public fr.toutatice.identite.portail.ficheProfil.ManagerStatut.statut getStatut() {
		return statut;
	}
	public void setStatut(
			fr.toutatice.identite.portail.ficheProfil.ManagerStatut.statut statut) {
		this.statut = statut;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}

	
	public boolean isDroitDelete() {
		return droitDelete;
	}
	public void setDroitDelete(boolean droitDelete) {
		this.droitDelete = droitDelete;
	}
	public boolean isClicable() {
		return clicable;
	}
	public void setClicable(boolean clicable) {
		this.clicable = clicable;
	}
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public boolean isModifiable() {
		return modifiable;
	}
	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}
	
	
	
//	public fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut getStatut() {
//		return statut;
//	}
//	public void setStatut(
//			fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut statut) {
//		this.statut = statut;
//	}
//	
	
	public ManagerUrl(String displayName,String id, fr.toutatice.identite.portail.ficheProfil.ManagerStatut.statut statut, String dn, String url, String logo, boolean droitDelete, boolean clicable, boolean modifiable) {
		super();
		this.statut = statut;
		this.displayName = displayName;
		this.id = id;
		this.url = url;
		this.dn = dn;
		this.droitDelete = droitDelete;
		this.clicable = clicable;
		this.logo = logo;
		this.modifiable = modifiable;
	}
	
	public int compareTo(Object obj) {
		ManagerUrl m = (ManagerUrl) obj;
		return this.getDisplayName().toLowerCase().compareTo(m.getDisplayName().toLowerCase());
	}
	
	
	
	
	
}
