package fr.toutatice.outils.ldap.entity;

import java.util.List;

import org.osivia.portal.api.directory.DirectoryBean;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface OrganizationalUnit extends DirectoryBean {

	public String getId();

	public void setId(String id);

	public String getDn();

	public void setDn(String dn);

	/**
	 * Recherche d'un élément par son adresse dans l'annuaire (DN sans le base DN)
	 * @param path adresse dans l'annuaire
	 * @return organizationalUnit recherché
	 * @throws ToutaticeAnnuaireException
	 */
	public OrganizationalUnit findGroupeByDn(String dn)
			throws ToutaticeAnnuaireException;

	/**
	 * Recherche des éléments placés directement sous l'élément courant
	 * @return liste des "enfants"
	 */
	public List<OrganizationalUnit> findSubFolders();

}