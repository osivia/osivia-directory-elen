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


import fr.toutatice.outils.ldap.entity.Person;



public class PersonUrl implements Comparable{

	private Person personne;
	private fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut statut;
	private String url;
	private boolean explicitMember;
	private boolean clicable;
	private boolean modifiable;
	
	public Person getPersonne() {
		return personne;
	}
	public void setPersonne(Person personne) {
		this.personne = personne;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public boolean isClicable() {
		return clicable;
	}
	public void setClicable(boolean clicable) {
		this.clicable = clicable;
	}
	public boolean isExplicitMember() {
		return explicitMember;
	}
	public void setExplicitMember(boolean explicitMember) {
		this.explicitMember = explicitMember;
	}
	public boolean isModifiable() {
		return modifiable;
	}
	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}
	public fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut getStatut() {
		return statut;
	}
	public void setStatut(
			fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut statut) {
		this.statut = statut;
	}
	
	
	public PersonUrl(Person personne, String url, boolean explicitMember, boolean clicable,boolean modifiable, fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut statut) {
		super();
		this.personne = personne;
		this.url = url;
		this.explicitMember = explicitMember;
		this.clicable = clicable;
		this.modifiable = modifiable;
		this.statut = statut;
	}
	
	public int compareTo(Object obj) {
		PersonUrl p = (PersonUrl) obj;
		return this.getPersonne().getCn().toLowerCase().compareTo(p.getPersonne().getCn().toLowerCase());
	}
	
	
}
