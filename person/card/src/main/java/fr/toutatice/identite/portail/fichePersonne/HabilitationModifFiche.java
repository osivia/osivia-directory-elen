package fr.toutatice.identite.portail.fichePersonne;

import org.osivia.directory.v2.service.RoleService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

	@Autowired
	private RoleService roleService;
	
	public Level findRoleUser(Person userConnecte, Person userConsulte)
			 {

		Level role = Level.NONHABILITE;

		// super admin
		if (userConnecte == null) {
			role = Level.NONHABILITE;
		} else if (roleService.hasRole(userConnecte.getDn(), config.getRoleSuperAdministrateur())
				|| roleService.hasRole(userConnecte.getDn(), config.getRoleAdministrateur())) {
			role = Level.DROITMODIF;
		} else if (userConnecte.getUid().equals(userConsulte.getUid())) {
			role = Level.DROITMODIF;
		}

		return role;

	}
}
