package fr.toutatice.identite.portail.fichePersonne;

import fr.toutatice.outils.ldap.entity.Etablissement;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Structure;

public class Enfant {
	
	private Person enfant;
	private Structure etb;
	
	public Person getEnfant() {
		return enfant;
	}
	public void setEnfant(Person enfant) {
		this.enfant = enfant;
	}
	public Structure getEtb() {
		return etb;
	}
	public void setEtb(Structure etb) {
		this.etb = etb;
	}
	public Enfant(Person enfant, Structure etb) {
		super();
		this.enfant = enfant;
		this.etb = etb;
	}
	

	
}
