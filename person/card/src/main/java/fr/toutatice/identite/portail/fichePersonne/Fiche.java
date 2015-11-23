package fr.toutatice.identite.portail.fichePersonne;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osivia.portal.api.urls.Link;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.entity.Structure;

public class Fiche {
	
public enum type {EXTACA,INTACA,MER,AUTRE;
	
	public String getString() {
    return this.name();
	}
}

	private String idConsulte;

	private ProfilNuxeo profilNuxeo;

	private Person userConsulte;
	private type typeUserConsulte;
	private type typeUserConnecte;
	private List<Structure> listeEtb = new ArrayList<Structure>();
	private List<Profil> listeProfils = new ArrayList<Profil>();
	private Profil classe;
	private List<PersonUrl> listeParents = new ArrayList<PersonUrl>();
	
	private boolean droitUpdateMdp = false;
	private boolean droitUpdateEmail = false;
	private boolean droitRazMdp = false;
	
	private HabilitationSurcharge.level levelUserConnecteSurcharge;
	private HabilitationRazMdp.level levelUserConnecteRazMdp;
	private HabilitationModifMailPwd.Level levelUserConnecteModifPwdMail;
	private HabilitationModifFiche.Level levelUserConnecteModifFiche;
	private LevelConsultation levelConsultation;
	
	
	private boolean self = false;
	
	private String motifSurcharge="";
	
	private List<Enfant> listeEnfants = new ArrayList<Enfant>();
	
	private Link avatar;
	private String tmpFile = "";
	private String typeMIME = "";

	private String departementCns;
	
	private Map<String, String> listeDptCns;
	
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
	

	public HabilitationSurcharge.level getLevelUserConnecteSurcharge() {
		return levelUserConnecteSurcharge;
	}
	public void setLevelUserConnecteSurcharge(
			HabilitationSurcharge.level levelUserConnecteSurcharge) {
		this.levelUserConnecteSurcharge = levelUserConnecteSurcharge;
	}
	public HabilitationRazMdp.level getLevelUserConnecteRazMdp() {
		return levelUserConnecteRazMdp;
	}
	public void setLevelUserConnecteRazMdp(
			HabilitationRazMdp.level levelUserConnecteRazMdp) {
		this.levelUserConnecteRazMdp = levelUserConnecteRazMdp;
	}
	public HabilitationModifFiche.Level getLevelUserConnecteModifFiche() {
		return levelUserConnecteModifFiche;
	}

	public void setLevelUserConnecteModifFiche(
			HabilitationModifFiche.Level levelUserConnecteModifFiche) {
		this.levelUserConnecteModifFiche = levelUserConnecteModifFiche;
	}

	public type getTypeUserConnecte() {
		return typeUserConnecte;
	}
	public void setTypeUserConnecte(type typeUserConnecte) {
		this.typeUserConnecte = typeUserConnecte;
	}
	
	public type getTypeUserConsulte() {
		return typeUserConsulte;
	}
	public void setTypeUserConsulte(type typeUserConsulte) {
		this.typeUserConsulte = typeUserConsulte;
	}
	public boolean isSelf() {
		return self;
	}
	public void setSelf(boolean self) {
		this.self = self;
	}
	public List<Enfant> getListeEnfants() {
		return listeEnfants;
	}
	public void setListeEnfants(List<Enfant> listeEnfants) {
		this.listeEnfants = listeEnfants;
	}
	public String getMotifSurcharge() {
		return motifSurcharge;
	}
	public void setMotifSurcharge(String motifSurcharge) {
		this.motifSurcharge = motifSurcharge;
	}
	
	
	public Profil getClasse() {
		return classe;
	}

	public void setClasse(Profil classe) {
		this.classe = classe;
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


	/**
	 * @return the levelUserConnecteModifPwdMail
	 */
	public HabilitationModifMailPwd.Level getLevelUserConnecteModifPwdMail() {
		return levelUserConnecteModifPwdMail;
	}

	/**
	 * @param levelUserConnecteModifPwdMail
	 *            the levelUserConnecteModifPwdMail to set
	 */
	public void setLevelUserConnecteModifPwdMail(HabilitationModifMailPwd.Level levelUserConnecteModifPwdMail) {
		this.levelUserConnecteModifPwdMail = levelUserConnecteModifPwdMail;
	}
	


	public LevelConsultation getLevelConsultation() {
		return levelConsultation;
	}

	public void setLevelConsultation(LevelConsultation levelConsultation) {
		this.levelConsultation = levelConsultation;
	}
	
	

	/**
	 * @return the departementCns
	 */
	public String getDepartementCns() {
		return departementCns;
	}

	/**
	 * @param departementCns the departementCns to set
	 */
	public void setDepartementCns(String departementCns) {
		this.departementCns = departementCns;
	}

	
	
	/**
	 * @return the listeDptCns
	 */
	public Map<String, String> getListeDptCns() {
		return listeDptCns;
	}

	/**
	 * @param listeDptCns the listeDptCns to set
	 */
	public void setListeDptCns(Map<String, String> listeDptCns) {
		this.listeDptCns = listeDptCns;
	}

	public type findTypeUser(Person user){
		if(user.getSourceSI().toLowerCase().equals("aa")){
			return type.INTACA;
		}else{
			if(user.getSourceSI().toLowerCase().equals("aa")){
				return type.EXTACA;
			}else{
				if(user.getSourceSI().toLowerCase().equals("em")){
					return type.MER;
				}else{
					return type.AUTRE;
				}
			}
		}
	}
	
}
