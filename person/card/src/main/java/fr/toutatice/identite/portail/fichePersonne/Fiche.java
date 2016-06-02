package fr.toutatice.identite.portail.fichePersonne;

import java.util.ArrayList;
import java.util.List;

import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.urls.Link;


public class Fiche {


	private String idConsulte;

	private ProfilNuxeo profilNuxeo;

	private Person userConsulte;

	private List<PersonUrl> listeParents = new ArrayList<PersonUrl>();

	
	private HabilitationModifFiche.Level levelUserConnecteModifFiche;
	
	private LevelConsultation levelConsultation;
	
	
	private boolean self = false;
		
	private Link avatar;
	private String tmpFile = "";
	private String typeMIME = "";

	
	public String getIdConsulte() {
		return idConsulte;
	}

	public void setIdConsulte(String idConsulte) {
		this.idConsulte = idConsulte;
	}

	/**
	 * @return the profilNuxeo
	 */
	public ProfilNuxeo getProfilNuxeo() {
		return profilNuxeo;
	}

	/**
	 * @param profilNuxeo
	 *            the profilNuxeo to set
	 */
	public void setProfilNuxeo(ProfilNuxeo profilNuxeo) {
		this.profilNuxeo = profilNuxeo;
	}
	public Person getUserConsulte() {
		return userConsulte;
	}
	public void setUserConsulte(Person userConsulte) {
		this.userConsulte = userConsulte;
	}

	
	public HabilitationModifFiche.Level getLevelUserConnecteModifFiche() {
		return levelUserConnecteModifFiche;
	}

	public void setLevelUserConnecteModifFiche(
			HabilitationModifFiche.Level levelUserConnecteModifFiche) {
		this.levelUserConnecteModifFiche = levelUserConnecteModifFiche;
	}


	public boolean isSelf() {
		return self;
	}
	public void setSelf(boolean self) {
		this.self = self;
	}

	
	public List<PersonUrl> getListeParents() {
		return listeParents;
	}

	public void setListeParents(List<PersonUrl> listeParents) {
		this.listeParents = listeParents;
	}

	/**
	 * @return the avatar
	 */
	public Link getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(Link avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the tmpFile
	 */
	public String getTmpFile() {
		return tmpFile;
	}

	/**
	 * @param tmpFile
	 *            the tmpFile to set
	 */
	public void setTmpFile(String tmpFile) {
		this.tmpFile = tmpFile;
	}

	/**
	 * @return the typeMIME
	 */
	public String getTypeMIME() {
		return typeMIME;
	}

	/**
	 * @param typeMIME
	 *            the typeMIME to set
	 */
	public void setTypeMIME(String typeMIME) {
		this.typeMIME = typeMIME;
	}
	


	public LevelConsultation getLevelConsultation() {
		return levelConsultation;
	}

	public void setLevelConsultation(LevelConsultation levelConsultation) {
		this.levelConsultation = levelConsultation;
	}

}
