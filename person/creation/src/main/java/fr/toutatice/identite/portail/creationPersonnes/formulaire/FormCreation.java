package fr.toutatice.identite.portail.creationPersonnes.formulaire;

public class FormCreation {

    private String title;

	private String sn;

	private String givenName;

	private String uid;
	
	private String nouveauMdp;
	
	private String confirmMdp;

	private String email;
	
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

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
	

	/**
	 * @return the nouveauMdp
	 */
	public String getNouveauMdp() {
		return nouveauMdp;
	}

	/**
	 * @param nouveauMdp the nouveauMdp to set
	 */
	public void setNouveauMdp(String nouveauMdp) {
		this.nouveauMdp = nouveauMdp;
	}

	/**
	 * @return the confirmMdp
	 */
	public String getConfirmMdp() {
		return confirmMdp;
	}

	/**
	 * @param confirmMdp the confirmMdp to set
	 */
	public void setConfirmMdp(String confirmMdp) {
		this.confirmMdp = confirmMdp;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


}
