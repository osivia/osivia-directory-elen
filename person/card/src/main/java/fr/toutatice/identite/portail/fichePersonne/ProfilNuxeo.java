package fr.toutatice.identite.portail.fichePersonne;


import org.nuxeo.ecm.automation.client.model.Documents;


public class ProfilNuxeo {

	String bio;
	String pathUserWorkspace;
	Documents listeBlogs = null;

	private String telFixe;
	private String telMobile;
	private String entiteAdm;
	private String mailGenerique;
	private String referent;


	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}


	public String getPathUserWorkspace() {
		return pathUserWorkspace;
	}
	public void setPathUserWorkspace(String pathUserWorkspace) {
		this.pathUserWorkspace = pathUserWorkspace;
	}
	public Documents getListeBlogs() {
		return listeBlogs;
	}
	public void setListeBlogs(Documents listeBlogs) {
		this.listeBlogs = listeBlogs;
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
