package fr.toutatice.identite.portail.gestionPersonnes;

import org.osivia.portal.api.urls.Link;

import fr.toutatice.outils.ldap.entity.Person;

public class PersonUrl implements Comparable{
	
	private Person personne;
	private String url;
	
	private Link avatar;
	
	public Person getPersonne() {
		return personne;
	}

	public void setPersonne(Person personne) {
		this.personne = personne;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	/**
	 * @return the avatar
	 */
	public Link getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Link avatar) {
		this.avatar = avatar;
	}

	public PersonUrl(Person personne, String url) {
		super();
		this.personne = personne;
		this.url = url;
	}

	public int compareTo(Object obj) {
		PersonUrl p = (PersonUrl) obj;
		return this.getPersonne().getCn().toLowerCase().compareTo(p.getPersonne().getCn().toLowerCase());
	}

}
