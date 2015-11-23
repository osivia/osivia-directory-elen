package fr.toutatice.identite.portail.fichePersonne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Component
public class HabilitationModifMailPwd {

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

		if (userConnecte != null) {
			if (config.getEnableModeENT()) {
				if (userConsulte.getTitle().equalsIgnoreCase("ele")
						|| userConsulte.getSourceSI()
								.equalsIgnoreCase("EXTACA")) {
					if (userConnecte.getUid().equals(userConsulte.getUid())) {
						role = Level.DROITMODIF;
					}
				}
			} else {
				if (userConnecte.getUid().equals(userConsulte.getUid())) {
					role = Level.DROITMODIF;
				}
			}
		}

		return role;
	}
}
