package fr.toutatice.identite.portail.fichePersonne;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Evalutation des droits de consultation de la fiche
 */
@Component
public class HabilitationConsultation {

	@Autowired
	private FichePersonneConfig config;

	@Autowired
	private Organisation organisation;

	public LevelConsultation getLevelConsultation(Person userConnecte,
			Person userConsulte) throws ToutaticeAnnuaireException {

		LevelConsultation level = new LevelConsultation();

		// Pour la présentation, il faut juste être connecté
		if (userConnecte != null) {
			level.setShowBio(true);
		}

		// Cas de l'assistance+ et de la consultation de sa propre fiche,
		// affichage des infos perso
		if(userConnecte == null) {
			
		}
		else if (userConnecte.hasRole(config.getRoleSuperAdministrateur())
				|| userConnecte.hasRole(config.getRoleAdministrateur())
				|| userConnecte.hasRole(config.getRoleAssistance())
				|| userConnecte.getUid().equals(userConsulte.getUid())) {

			level.setShowLogin(true);
			level.setShowMail(true);
			level.setShowEtab(true);
			

			if (userConsulte.getTitle().equals("ELE")) {
				level.setShowInfosEle(true);
			}

			if (userConsulte.isParent()) {
				level.setShowInfosParents(true);
			}

		} else if (interneOuMemeOrga(userConnecte, userConsulte)) {
			// Si le consultant est du personnel et consulte un autre membre du
			// pers ou une personne
			// du même établissement, il peut voir le login et l'étab

			level.setShowLogin(true);
			level.setShowEtab(true);

			if (userConsulte.getTitle().equals("ELE")) {
				// il peut voir les infos si c'est un élève
				level.setShowInfosEle(true);
			} else {
				// Il peut voir son mail, sauf si c'est un élève (besoin d'une
				// autorisation parentale ?)
				level.setShowMail(true);
			}

			if (userConsulte.isParent()) {
				
				level.setShowInfosParents(true);
			}
		} else {
			// TODO externaliser dans une conf
			level.setShowMail(true);
		}
		
		return level;

	}

	private boolean interneOuMemeOrga(Person userConnecte, Person userConsulte) {

		boolean ret = false;

		if (userConnecte.getSourceSI().equals("AA")) {
			if (userConsulte.getSourceSI().equals("AA")) {
				ret = true;
			} else {

				List<Organisation> listeRneUserConnecte = organisation
						.findOrganisationPersonneByProfil(userConnecte);
				List<Organisation> listeRneUserConsulte = organisation
						.findOrganisationPersonneByProfil(userConsulte);
				for (Organisation org : listeRneUserConnecte) {
					for (Organisation org1 : listeRneUserConsulte) {
						if (org.getId().equalsIgnoreCase(org1.getId())) {
							ret = true;
							break;
						}
					}
				}
			}
		}

		return ret;
	}
}
