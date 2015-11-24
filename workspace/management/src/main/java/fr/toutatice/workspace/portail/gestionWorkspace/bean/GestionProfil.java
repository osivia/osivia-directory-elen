package fr.toutatice.workspace.portail.gestionWorkspace.bean;

import fr.toutatice.outils.ldap.entity.Profil;

public class GestionProfil {

	public enum statut {AJOUTER,SUPPRIMER,RAS;
	public String getString() {
    return this.name();
	}
	}
	
	private Profil profil;
	private GestionProfil.statut statut;
	
	public Profil getProfil() {
		return profil;
	}
	public void setProfil(Profil profil) {
		this.profil = profil;
	}
	public GestionProfil.statut getStatut() {
		return statut;
	}
	public void setStatut(GestionProfil.statut statut) {
		this.statut = statut;
	}
	
	public GestionProfil(
			Profil profil,
			fr.toutatice.workspace.portail.gestionWorkspace.bean.GestionProfil.statut statut) {
		super();
		this.profil = profil;
		this.statut = statut;
	}
	
	
}
