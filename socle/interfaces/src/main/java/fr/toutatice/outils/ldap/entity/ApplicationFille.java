package fr.toutatice.outils.ldap.entity;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface ApplicationFille extends Application {

	/**
	 * Getter
	 */
	public String getProprietaire();

	/**
	 * Setter
	 */
	public void setProprietaire(String proprietaire);

	/**
	 * Getter
	 * @return
	 */
	public String getApplicationMere();

	/**
	 * Setter
	 * @param applicationMere
	 */
	public void setApplicationMere(String applicationMere);

	/**
	 * Recherche d'une application fille
	 * @param user propriétaire de l'application fille recherchée
	 * @param app application mère dont dépend l'application fille recherchée
	 * @return application fille recherchée
	 */
	public ApplicationFille findAppliFille(Person user, ApplicationMere app);

	/**
	 * Teste la présence d'une application fille pour un propriétaire donné
	 * @param user propriétaire présumé de l'application
	 * @param app Application mère dont dépend l'application fille recherchée
	 * @return true si l'application existe, false sinon
	 */
	public boolean presenceAppliFille(Person user, ApplicationMere app);

	/**
	 * Teste l'existence d'au moins une application fille liée à l'application mère
	 * @param app application mère dont on recherche des filles
	 * @return true si au moins une application fille existe, false sinon
	 */
	public boolean presenceAppliFille(ApplicationMere app);

	/**
	 * Création d'une application fille
	 * @param user propriétaire de l'application à créér
	 * @param appliMere application mère dont dépendra l'application fille
	 * @return application fille créé dans l'annuaire
	 * @throws ToutaticeAnnuaireException 
	 */
	@SuppressWarnings("static-access")
	public ApplicationFille creerAppliFille(Person user,
			ApplicationMere appliMere) throws ToutaticeAnnuaireException;

	/**
	 * suppression de l'application fille
	 * @throws ToutaticeAnnuaireException 
	 */
	public void supprimerAppliFille() throws ToutaticeAnnuaireException;

}