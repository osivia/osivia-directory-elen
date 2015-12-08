package fr.toutatice.identite.portail.gestionPersonnes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Component
public class HabilitationCreation {

	public enum LevelCreation {
		HABILITE, NONHABILITE;

		public String getString() {
			return this.name();
		}
	}

	@Autowired
	private Person person;
	
	@Autowired
	private GestionPersonnesConfig config;

	private LevelCreation role;

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");

	public LevelCreation getRole() {
		return role;
	}

	public void setRole(LevelCreation role) {
		this.role = role;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public LevelCreation findRoleUser(Person userConnecte) throws ToutaticeAnnuaireException {

		LevelCreation role = LevelCreation.NONHABILITE;
		
		if(config.getEnableCreation()) {
		
			if (userConnecte.hasRole(config.getRoleSuperAdministrateur())) {
				role = LevelCreation.HABILITE;
			} else {
				if (userConnecte.hasRole(config.getRoleAdministrateur())) {
					role = LevelCreation.HABILITE;
				}
			}
		}

		return role;
	}

}
