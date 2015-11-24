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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Component
public class Habilitation {

	public enum level {
		LECTEUR, GESTIONNAIRE, ADMINISTRATEUR, NONHABILITE;
		public String getString() {
			return this.name();
		}
	}

	@Autowired
	private FicheProfilConfig config;
	
	@Autowired
	private Person personne;

	private level role;

	public level getRole() {
		return role;
	}

	public void setRole(level role) {
		this.role = role;
	}

	public boolean isAdmin(Person user) {
		if (user.hasRole(config.getRoleAdministrateur())) {
			return true;
		} else {
			return false;
		}
	}

	public level findRoleUser(Person user, Profil profil) {

		level role = level.NONHABILITE;

		if (user.hasRole(config.getRoleAdministrateur())) {
			role = level.ADMINISTRATEUR;
		} else {
			if (profil.isManagedBy(user)) {
				role = level.GESTIONNAIRE;
			} else {
				if (profil.isMember(user.getDn())) {
					role = level.LECTEUR;
				} else {
					if (!role.equals(Habilitation.level.LECTEUR)) {
						role = level.LECTEUR;
					}
				}
			}
		}

		return role;
	}

}
