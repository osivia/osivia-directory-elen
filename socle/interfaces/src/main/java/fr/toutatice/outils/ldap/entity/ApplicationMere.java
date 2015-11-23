package fr.toutatice.outils.ldap.entity;

import java.util.List;


public interface ApplicationMere extends Application {


	/**
	 * 
	 * @return
	 */
	public List<String> getDroitsInscription();

	/**
	 * 
	 * @param droitsInscription
	 */
	public void setDroitsInscription(List<String> droitsInscription);

	/**
	 * 
	 * @param droit
	 */
	public void addDroitInscription(String droit);

	/**
	 * 
	 * @return
	 */
	public boolean isExportableCsv();

	/**
	 * 
	 * @param exportableCsv
	 */
	public void setExportableCsv(boolean exportableCsv);

	/**
	 * Indique si 
	 * @return
	 */
	public boolean isPresenceAppliFillePrUserConnecte();

	/**
	 * 
	 * @param presenceAppliFillePrUserConnecte
	 */
	public void setPresenceAppliFillePrUserConnecte(
			boolean presenceAppliFillePrUserConnecte);

	/**
	 * Indique si l'application mère possède une application fille au moins
	 * @return true si au moins une application fille existe, false sinon
	 */
	public boolean presenceAppliFille();

	/**
	 * Indique si la personne passée en paramètre possède une application fille liée à cette application mère
	 * @param user personne sur qui porte la recherche
	 * @return true si l'application fille existe, false sinon
	 */
	public boolean presenceAppliFillePourUser(Person user);

	/**
	 * Recherche de la liste des applications mères pour lesquelles l'utilisateur a des droits d'inscription
	 * @param user
	 * @return
	 */
	public List<ApplicationMere> findListeApplisMereInscription(Person user);

}