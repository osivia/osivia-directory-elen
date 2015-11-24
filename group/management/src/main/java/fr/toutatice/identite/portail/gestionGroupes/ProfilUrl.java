package fr.toutatice.identite.portail.gestionGroupes;

import fr.toutatice.outils.ldap.entity.Profil;

public class ProfilUrl implements Comparable<Object>{
	
	private Profil profil;
	private String url;
	public Profil getProfil() {
		return profil;
	}
	public void setProfil(Profil profil) {
		this.profil = profil;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public ProfilUrl(Profil profil, String url) {
		super();
		this.profil = profil;
		this.url = url;
	}
	
	public int compareTo(Object obj) {
		ProfilUrl p = (ProfilUrl) obj;
		return this.getProfil().getDisplayName().toLowerCase().compareTo(p.getProfil().getDisplayName().toLowerCase());
	}

}
