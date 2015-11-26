package fr.toutatice.identite.portail.fichePersonne;


import org.nuxeo.ecm.automation.client.model.Documents;


public class ProfilNuxeo {

	String bio;
	String pathUserWorkspace;
	Documents listeBlogs = null;

	private String telFixe;
	private String telMobile;

    private String profession;


	public String getBio() {
		return this.bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}


	public String getPathUserWorkspace() {
		return this.pathUserWorkspace;
	}
	public void setPathUserWorkspace(String pathUserWorkspace) {
		this.pathUserWorkspace = pathUserWorkspace;
	}
	public Documents getListeBlogs() {
		return this.listeBlogs;
	}
	public void setListeBlogs(Documents listeBlogs) {
		this.listeBlogs = listeBlogs;
	}
	/**
	 * @return the telFixe
	 */
	public String getTelFixe() {
		return this.telFixe;
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
		return this.telMobile;
	}
	/**
	 * @param telMobile the telMobile to set
	 */
	public void setTelMobile(String telMobile) {
		this.telMobile = telMobile;
	}

    /**
     * Getter for profession.
     * 
     * @return the profession
     */
    public String getProfession() {
        return this.profession;
    }

    /**
     * Setter for profession.
     * 
     * @param profession the profession to set
     */
    public void setProfession(String profession) {
        this.profession = profession;
    }

}
