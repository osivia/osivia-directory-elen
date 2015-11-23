package fr.toutatice.identite.portail.fichePersonne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Component
public class HabilitationSurcharge {

	public enum level {
		DROIT_SURCHARGE, DROIT_SURCHARGE_ASSISTANCE, NONHABILITE, NON_SURCHARGEABLE;

		public String getString() {
			return this.name();
		}
	}

	@Autowired
	private FichePersonneConfig config;

	private level role;

	public level getRole() {
		return role;
	}

	public void setRole(level role) {
		this.role = role;
	}

	public level findRoleUser(Person userConnecte, Person userConsulte) throws ToutaticeAnnuaireException {

		level role = level.NONHABILITE;

		// super admin
		if(userConnecte == null) {
			role = level.NONHABILITE;
		}
		else if (userConnecte.hasRole(config.getRoleSuperAdministrateur())) {
			role = level.DROIT_SURCHARGE;

		} else if (userConnecte.hasRole(config.getRoleAdministrateur())) {
			// admin
			// On ne surcharge pas certaines personnes (rôle spécifique)
			// Les admins ne surchargent pas les superadmin
			if (userConsulte.hasRole(config.getRoleNonSurchargeable()) || userConsulte.hasRole(config.getRoleSuperAdministrateur())) {
				role = level.NON_SURCHARGEABLE;
			} else {
				role = level.DROIT_SURCHARGE;
			}

		} else if (userConnecte.hasRole(config.getRoleAssistance())) {

			// assistance
			// On ne surcharge pas certaines personnes (rôle spécifique)
			// L'assistance ne surcharge pas les superadmin ni les admin
			// L'assistance ne surcharge ni les parents ni les élèves
			if (userConsulte.hasRole(config.getRoleNonSurchargeable()) || userConsulte.hasRole(config.getRoleSuperAdministrateur())
					|| userConsulte.hasRole(config.getRoleAdministrateur())) {
				role = level.NON_SURCHARGEABLE;
			} else if (userConsulte.getTitle().equalsIgnoreCase("ELE")) {
				role = level.NON_SURCHARGEABLE;
			} else if (userConsulte.isParent()) {
				role = level.NON_SURCHARGEABLE;
			} else {
				role = level.DROIT_SURCHARGE_ASSISTANCE;
			}
		}
		return role;
	}

}
