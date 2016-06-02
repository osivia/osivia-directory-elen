package fr.toutatice.identite.portail.fichePersonne;

import org.osivia.portal.api.directory.v2.model.Person;

public class PersonUrl {
	
	private Person personne;
	private String url;
	
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
	public PersonUrl(
			Person personne,
			String url) {
		super();
		this.personne = personne;
		this.url = url;
	}
	
	

}
