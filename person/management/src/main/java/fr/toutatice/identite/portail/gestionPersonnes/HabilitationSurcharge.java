package fr.toutatice.identite.portail.gestionPersonnes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Component
public class HabilitationSurcharge {

	public enum level {
		ADMINISTRATEUR, SUPERADMINISTRATEUR, ASSISTANCE, NONHABILITE;

		public String getString() {
			return this.name();
		}
	}

	@Autowired
	private Person person;
	
	@Autowired
	private GestionPersonnesConfig config;

	private level role;

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");

	public level getRole() {
		return role;
	}

	public void setRole(level role) {
		this.role = role;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public level findRoleUser(Person userConnecte) throws ToutaticeAnnuaireException {

		level role = level.NONHABILITE;
		if (userConnecte.hasRole(config.getRoleSuperAdministrateur())) {
			role = level.SUPERADMINISTRATEUR;
		} else {
			if (userConnecte.hasRole(config.getRoleAdministrateur())) {
				role = level.ADMINISTRATEUR;
			} else {
				if (userConnecte.hasRole(config.getRoleAssistance())) {
					role = level.ASSISTANCE;
				}
			}
		}

		return role;
	}

}
