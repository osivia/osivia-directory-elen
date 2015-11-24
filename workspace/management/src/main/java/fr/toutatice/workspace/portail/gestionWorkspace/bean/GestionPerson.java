package fr.toutatice.workspace.portail.gestionWorkspace.bean;

import fr.toutatice.outils.ldap.entity.Person;

public class GestionPerson {
	
	private Person personne;
	private GestionProfil.statut statut;
	public Person getPersonne() {
		return personne;
	}
	public void setPersonne(Person personne) {
		this.personne = personne;
	}
	public GestionProfil.statut getStatut() {
		return statut;
	}
	public void setStatut(GestionProfil.statut statut) {
		this.statut = statut;
	}
	public GestionPerson(
			Person personne,
			fr.toutatice.workspace.portail.gestionWorkspace.bean.GestionProfil.statut statut) {
		super();
		this.personne = personne;
		this.statut = statut;
	}
	
	
	
}
