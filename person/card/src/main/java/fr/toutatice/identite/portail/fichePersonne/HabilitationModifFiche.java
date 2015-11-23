package fr.toutatice.identite.portail.fichePersonne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Component
public class HabilitationModifFiche {

	public enum Level {
		DROITMODIF, NONHABILITE;

		public String getString() {
			return this.name();
		}
	}

	@Autowired
	private FichePersonneConfig config;

	public Level findRoleUser(Person userConnecte, Person userConsulte)
			throws ToutaticeAnnuaireException {

		Level role = Level.NONHABILITE;

		// super admin
		if (userConnecte == null) {
			role = Level.NONHABILITE;
		} else if (userConnecte.hasRole(config.getRoleSuperAdministrateur())
				|| userConnecte.hasRole(config.getRoleAdministrateur())) {
			role = Level.DROITMODIF;
		} else if (userConnecte.getUid().equals(userConsulte.getUid())) {
			role = Level.DROITMODIF;
		}

		return role;

	}
}
