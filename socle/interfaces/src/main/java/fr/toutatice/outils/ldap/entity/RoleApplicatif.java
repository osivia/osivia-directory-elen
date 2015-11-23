package fr.toutatice.outils.ldap.entity;

import java.util.List;

import org.osivia.portal.api.directory.DirectoryBean;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface RoleApplicatif extends DirectoryBean {


	public String getCn();

	public void setCn(String cn);

	public String getDisplayName();

	public void setDisplayName(String displayName);

	public String getDn();

	public String getDescription();

	public void setDescription(String description);

	public String getType();

	public void setType(String type);

	public String getFiltreRecherche();

	public void setFiltreRecherche(String filtreRecherche);

	public List<String> getListeMembers();

	public void setListeMembers(List<String> listeMembers);

	/**Ajout d'un membre au role
	 * Un test vérifie si cette personne n'est pas déjà membre (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnMember dn de la personne que l'on souhaite ajouter comme membre
	 */
	public void addMember(String dnMember);

	/**
	 * Suppression d'un membre du role
	 * Si la personne n'est pas dans la liste un log d'erreur est généré
	 * @param dnMember dn de la personne que l'on souhaite retirer de la liste des membres
	 */
	public void removeMember(String dnMember);

	/**
	 * Indique si la personne dont le dn est passé en paramètre est membre du role (indépendemmant de ses profils)
	 * @param dnMember DN de la personne testée
	 * @return true si la personne est membre du profil, false sinon
	 */
	public boolean isMemberExplicit(String dnMember);

	public List<String> getListeProfils();

	public void setListeProfils(List<String> listeProfils);

	/**Ajout d'un profil à un role
	 * Un test vérifie si ce profil n'est pas déjà affecté au role (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnProfil dn du profil que l'on souhaite ajouter 
	 */
	public void addProfil(String dnProfil);

	/**
	 * Suppression d'un des profils du role
	 * Si le profil n'était pas affecté au role un message est généré dans la log
	 * @param dnProfil
	 */
	public void removeProfil(String dnProfil);

	public List<String> getListeOwners();

	public void setListeOwners(List<String> listeOwner);

	/**Ajout d'un owner au role
	 * Un test vérifie si cette personne n'est pas déjà owner (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnOwner dn de la personne que l'on souhaite nommer owner
	 */
	public void addOwner(String dnOwner);

	/**
	 * Suppression d'un owner du role
	 * Si la personne n'est pas dans la liste un log d'erreur est généré
	 * @param dnOwner dn de la personne que l'on souhaite retirer de la liste des owners
	 */
	public void removeOwner(String dnOwner);

	public List<String> getListeManagers();

	public void setListeManagers(List<String> listeManagers);

	/**Ajout d'un manager 
	 * Un test vérifie si cette personne n'est pas déjà manager (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnManager dn de la personne que l'on souhaite ajouter comme manager
	 */
	public void addManager(String dnManager);

	/**
	 * Suppression d'un manager 
	 * Si la personne n'est pas dans la liste un log d'erreur est généré
	 * @param dnManager dn de la personne que l'on souhaite retirer de la liste des managers
	 */
	public void removeManager(String dnManager);

	public List<String> getListeExplicitManagers();

	public void setListeExplicitManagers(List<String> listeExplicitManagers);

	public void addExplicitManager(String dnManager);

	public void removeExplicitManager(String dnManager);

	public void create() throws ToutaticeAnnuaireException;

	public void delete() throws ToutaticeAnnuaireException;

	public boolean isManagedBy(Person p);

	public List<String> getListeMemberURL();

	public void setListeMemberURL(List<String> listeMemberURL);

	public void addMemberURL(String url);

	public void removeMemberURL(String url);

	/**
	 * Recherche de l'adresse (DN) du role applicatif dans l'annuaire
	 * @param cn nom du role applicatif
	 * @return DN du role applicatif
	 */
	public String findFullDn(String cn);

	/**
	 * Recherche de la liste des roles applicatifs d'une personne
	 * @param personne personne sur qui porte la recherche
	 * @return liste des roles applicatifs qui lui sont associés (soit directement, soit via un profil)
	 */
	public List<RoleApplicatif> findListRole(Person personne);

	/**
	 * Recherche des DN des roles applicatifs d'une personne
	 * @param personne personne sur qui porte la recherche
	 * @return liste des DN des roles
	 */
	public List<String> findListDnRole(Person personne);

	/**
	 * Recherche d'un role applicatif par son adresse dans l'annuaire (DN)
	 * @param dn adresse du role dans l'annuaire
	 * @return role applicatif recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public RoleApplicatif findRoleByDn(String dn);

	/**
	 * Recherche d'un role applicatif par son nom
	 * @param cn nom du role recherché
	 * @return role applicatif recherché
	 * @throws ToutaticeAnnuaireException
	 */
	public RoleApplicatif findRoleByCn(String cn)
			throws ToutaticeAnnuaireException;

	/**
	 * Mise à jour d'un role applicatif
	 * Mise à jour des attributs NOM, DESCRIPTION, MEMBERS, PROFILS et OWNERS
	 * @throws ToutaticeAnnuaireException
	 */
	public void updateRole() throws ToutaticeAnnuaireException;
	
	public RoleApplicatif getNewRole() ;


}