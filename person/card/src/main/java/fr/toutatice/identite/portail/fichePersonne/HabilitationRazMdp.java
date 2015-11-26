package fr.toutatice.identite.portail.fichePersonne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Component
public class HabilitationRazMdp {

	public enum level {
		DROITRAZ, NONHABILITE;

		public String getString() {
			return this.name();
		}
	}

	@Autowired
	private Organisation organisation;

	@Autowired
	private FichePersonneConfig config;

	private level role;

	public level getRole() {
		return this.role;
	}

	public void setRole(level role) {
		this.role = role;
	}

	public Organisation getOrganisation() {
		return this.organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public level findRoleUser(Person userConnecte, Person userConsulte) throws ToutaticeAnnuaireException {

		level role = level.NONHABILITE;

        if (userConnecte != null) {

			if (userConnecte.hasRole(this.config.getRoleSuperAdministrateur())) {
				role = level.DROITRAZ;
			} else {
				if (userConnecte.hasRole(this.config.getRoleAdministrateur())) {
					role = level.DROITRAZ;
				} else {
					if (userConnecte.hasRole(this.config.getRoleRazMdp())) {
						role = level.DROITRAZ;
					}
				}

			}
		}

		return role;
	}

}
