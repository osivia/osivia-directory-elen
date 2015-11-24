package fr.toutatice.identite.portail.gestionGroupes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.toutatice.outils.ldap.entity.Application;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public class Habilitation {

	protected static final Log logger = LogFactory
			.getLog("fr.toutatice.services");

	public enum level {
		LECTEUR, ADMINISTRATEUR, NONHABILITE;
		public String getString() {
			return this.name();
		}
	}

	private String roleAdministrateur;
	

	private level role;

	public String getRoleAdministrateur() {
		return roleAdministrateur;
	}

	public void setRoleAdministrateur(String roleAdministrateur) {
		this.roleAdministrateur = roleAdministrateur;
	}

	

	public level getRole() {
		return role;
	}

	public void setRole(level role) {
		this.role = role;
	}

	public level findRoleUser(Person user) {

		level role = level.NONHABILITE;

		if (user.hasRole(roleAdministrateur)) {
			role = level.ADMINISTRATEUR;
		} 
		else {
				role = level.LECTEUR;
		}

		return role;
	}

}
