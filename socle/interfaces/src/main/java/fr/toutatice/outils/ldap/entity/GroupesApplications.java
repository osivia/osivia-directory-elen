package fr.toutatice.outils.ldap.entity;

import java.util.List;

import org.osivia.portal.api.directory.DirectoryBean;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface GroupesApplications extends DirectoryBean {

	public String getId();

	public void setId(String id);

	public String getDisplayName();

	public void setDisplayName(String displayName);

	public String getMemberUrl();

	public void setMemberUrl(String memberUrl);

	/**
	 * Recherche d'un groupe d'applications par son identifiant
	 * @param nom nom du groupe recherché
	 * @return groupe d'applications recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public GroupesApplications findGroupeById(String id)
			throws ToutaticeAnnuaireException;

	/**
	 * Recherche d'un groupe d'application par son adresse dans l'annuaire
	 * @param path adresse du groupe dans l'annuaire
	 * @return groupe d'applications recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public GroupesApplications findGroupeByDn(String dn)
			throws ToutaticeAnnuaireException;

	/**
	 * Recherche de tous les groupes d'applications existants sous le chemin passé en paramètre
	 * @param chemin base de l'annuaire à partir de laquelle lancer la recherche
	 * @return Liste de tous les groupes sélectionnés
	 */
	public List<GroupesApplications> findGroupesNoeud(String dnNoeud);

	/**
	 * Recherche des groupes d'applications autorisés pour un utilisateur
	 * @return Liste des groupes recherchés
	 */
	public List<GroupesApplications> findGroupesNoeud(String dnNoeud,
			Person user);


}