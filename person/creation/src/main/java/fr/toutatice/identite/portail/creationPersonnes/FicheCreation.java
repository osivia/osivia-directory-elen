package fr.toutatice.identite.portail.creationPersonnes;

import java.util.ArrayList;
import java.util.List;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.entity.Structure;

public class FicheCreation {

	private Person userConsulte;
	private List<Structure> listeEtb = new ArrayList<Structure>();
	private List<Profil> listeProfils = new ArrayList<Profil>();
	
	// private ProfilNuxeo profilNuxeo;
	private String tmpFile="";
	private String typeMIME="";
	
	private boolean droitUpdateMdp = false;
	private boolean droitUpdateEmail = false;
	private boolean droitRazMdp = false;
	
	private boolean chargementAvatar = false;
	//
	// private Habilitation.level levelUserConnecte;
	// private Habilitation.type typeUserConnecte;
	
	private boolean self = false;
	
	// private String idParent;
	// private List<Enfant> listeEnfants = new ArrayList<Enfant>();
	
	public Person getUserConsulte() {
		return userConsulte;
	}
	public void setUserConsulte(Person userConsulte) {
		this.userConsulte = userConsulte;
	}
	public List<Structure> getListeEtb() {
		return listeEtb;
	}
	public void setListeEtb(List<Structure> listeEtb) {
		this.listeEtb = listeEtb;
	}
	public List<Profil> getListeProfils() {
		return listeProfils;
	}
	public void setListeProfils(List<Profil> listeProfils) {
		this.listeProfils = listeProfils;
	}
	public boolean isDroitUpdateMdp() {
		return droitUpdateMdp;
	}
	public void setDroitUpdateMdp(boolean droitUpdateMdp) {
		this.droitUpdateMdp = droitUpdateMdp;
	}
	public boolean isDroitUpdateEmail() {
		return droitUpdateEmail;
	}
	public void setDroitUpdateEmail(boolean droitUpdateEmail) {
		this.droitUpdateEmail = droitUpdateEmail;
	}
	public boolean isDroitRazMdp() {
		return droitRazMdp;
	}
	public void setDroitRazMdp(boolean droitRazMdp) {
		this.droitRazMdp = droitRazMdp;
	}

	// public Habilitation.level getLevelUserConnecte() {
	// return levelUserConnecte;
	// }
	// public void setLevelUserConnecte(Habilitation.level levelUserConnecte) {
	// this.levelUserConnecte = levelUserConnecte;
	// }
	// public Habilitation.type getTypeUserConnecte() {
	// return typeUserConnecte;
	// }
	// public void setTypeUserConnecte(Habilitation.type typeUserConnecte) {
	// this.typeUserConnecte = typeUserConnecte;
	// }
	public boolean isSelf() {
		return self;
	}
	public void setSelf(boolean self) {
		this.self = self;
	}

	// public String getIdParent() {
	// return idParent;
	// }
	// public void setIdParent(String idParent) {
	// this.idParent = idParent;
	// }
	// public List<Enfant> getListeEnfants() {
	// return listeEnfants;
	// }
	// public void setListeEnfants(List<Enfant> listeEnfants) {
	// this.listeEnfants = listeEnfants;
	// }
	// public ProfilNuxeo getProfilNuxeo() {
	// return profilNuxeo;
	// }
	// public void setProfilNuxeo(ProfilNuxeo profilNuxeo) {
	// this.profilNuxeo = profilNuxeo;
	// }
	public String getTmpFile() {
		return tmpFile;
	}
	public void setTmpFile(String tmpFile) {
		this.tmpFile = tmpFile;
	}
	public String getTypeMIME() {
		return typeMIME;
	}
	public void setTypeMIME(String typeMIME) {
		this.typeMIME = typeMIME;
	}
	public boolean isChargementAvatar() {
		return chargementAvatar;
	}
	public void setChargementAvatar(boolean chargementAvatar) {
		this.chargementAvatar = chargementAvatar;
	}
	
	
}
