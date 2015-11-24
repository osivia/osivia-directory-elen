package fr.toutatice.identite.portail.ficheProfil;



public class ManagerStatut {
	
	
	public enum statut {AJOUTER,SUPPRIMER,RAS;
	public String getString() {
    return this.name();
	}
	}
	
	
	public ManagerStatut(String dn, fr.toutatice.identite.portail.ficheProfil.ManagerStatut.statut statut) {
		super();
		this.dn = dn;
		this.statut = statut;
	}
	

	private String dn;
	private ManagerStatut.statut statut;
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public ManagerStatut.statut getStatut() {
		return statut;
	}
	public void setStatut(ManagerStatut.statut statut) {
		this.statut = statut;
	}
	
	

}
