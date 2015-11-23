package fr.toutatice.identite.portail.fichePersonne;

import java.util.List;

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
		return role;
	}

	public void setRole(level role) {
		this.role = role;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public level findRoleUser(Person userConnecte, Person userConsulte) throws ToutaticeAnnuaireException {

		level role = level.NONHABILITE;

		if (userConsulte.getTitle().equalsIgnoreCase("ELE") && userConnecte != null) {

			if (userConnecte.hasRole(config.getRoleSuperAdministrateur())) {
				role = level.DROITRAZ;
			} else {
				if (userConnecte.hasRole(config.getRoleAdministrateur())) {
					role = level.DROITRAZ;
				} else {

					// pour le role raz mot de passe,on vérifie que le userconnecte
					// a le droit sur un des rne de la personne consultée
					// (il peut être habilité sur d'autres Rne mais pas sur celui du
					// user consulte
					// pour le role raz mot de passe,on vérifie que le userconnecte
					// a le droit sur un des rne de la personne consultée
					// (il peut être habilité sur d'autres Rne mais pas sur celui du
					// user consulte
					if (userConnecte.hasRole(config.getRoleRazMdp())) {
						boolean memeRne = false;
						List<Organisation> listeRneUserConnecte = organisation.findOrganisationPersonneByProfil(userConnecte);
						List<Organisation> listeRneUserConsulte = organisation.findOrganisationPersonneByProfil(userConsulte);
						for (Organisation org : listeRneUserConnecte) {
							for (Organisation org1 : listeRneUserConsulte) {
								if (org.getId().equalsIgnoreCase(org1.getId())) {
									memeRne = true;
									break;
								}
							}
						}

						role = level.DROITRAZ;
					}
				}

			}
		}

		return role;
	}

}
