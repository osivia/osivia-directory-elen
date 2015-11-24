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

public class PersonStatut {

	public enum statut {AJOUTER,SUPPRIMER,RAS;
	public String getString() {
    return this.name();
	}
	}
	
	
	public PersonStatut(Person person,fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut statut) {
		super();
		this.person = person;
		this.statut = statut;
	}
	

	private Person person;
	private PersonStatut.statut statut;
	
	public void setPerson(Person person) {
		this.person = person;
	}
	public Person getPerson() {
		return person;
	}
	public void setStatut(PersonStatut.statut statut) {
		this.statut = statut;
	}
	public PersonStatut.statut getStatut() {
		return statut;
	}
	
	
	
}
