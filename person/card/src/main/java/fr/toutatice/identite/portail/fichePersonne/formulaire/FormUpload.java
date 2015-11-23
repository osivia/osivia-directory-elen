package fr.toutatice.identite.portail.fichePersonne.formulaire;

import org.springframework.web.multipart.MultipartFile;


public class FormUpload {

	private MultipartFile file;
	private String alias;
	private String nouveauEmail;

	private boolean chargementAvatar=false;

    private String sn;
    private String givenName;
    
    private String title;
	private String bio;
	private String telFixe;
	private String telMobile;
	private String departementCns;
	private String entiteAdm;
	private String mailGenerique;
	private String referent;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	

	public String getNouveauEmail() {
		return nouveauEmail;
	}

	public void setNouveauEmail(String nouveauEmail) {
		this.nouveauEmail = nouveauEmail;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public boolean isChargementAvatar() {
		return chargementAvatar;
	}

	public void setChargementAvatar(boolean chargementAvatar) {
		this.chargementAvatar = chargementAvatar;
	}

	/**
     * @return the sn
     */
    public String getSn() {
        return sn;
    }


    /**
     * @param sn the sn to set
     */
    public void setSn(String sn) {
        this.sn = sn;
    }
    
	/**
	 * @return the givenName
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * @param givenName the givenName to set
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the telFixe
	 */
	public String getTelFixe() {
		return telFixe;
	}
	/**
	 * @param telFixe the telFixe to set
	 */
	public void setTelFixe(String telFixe) {
		this.telFixe = telFixe;
	}
	/**
	 * @return the telMobile
	 */
	public String getTelMobile() {
		return telMobile;
	}
	/**
	 * @param telMobile the telMobile to set
	 */
	public void setTelMobile(String telMobile) {
		this.telMobile = telMobile;
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
	 * @return the entiteAdm
	 */
	public String getEntiteAdm() {
		return entiteAdm;
	}
	/**
	 * @param entiteAdm the entiteAdm to set
	 */
	public void setEntiteAdm(String entiteAdm) {
		this.entiteAdm = entiteAdm;
	}
	/**
	 * @return the mailGenerique
	 */
	public String getMailGenerique() {
		return mailGenerique;
	}
	/**
	 * @param mailGenerique the mailGenerique to set
	 */
	public void setMailGenerique(String mailGenerique) {
		this.mailGenerique = mailGenerique;
	}
	/**
	 * @return the referent
	 */
	public String getReferent() {
		return referent;
	}
	/**
	 * @param referent the referent to set
	 */
	public void setReferent(String referent) {
		this.referent = referent;
	}
	
	

	
}
