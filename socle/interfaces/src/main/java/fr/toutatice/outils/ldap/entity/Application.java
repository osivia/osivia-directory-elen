package fr.toutatice.outils.ldap.entity;

import java.util.ArrayList;
import java.util.List;

import org.osivia.portal.api.directory.DirectoryBean;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface Application extends DirectoryBean {


	/**
	 * 
	 * @return
	 */
	public String getId();

	public void setId(String id);

	public String getNom();

	public void setNom(String nom);

	public List<String> getCategories();

	public void setCategories(List<String> categories);

	public void addCategories(String s);

	public String getDescription();

	public void setDescription(String description);
	
	public String getTypeApplication();
	
	public void setTypeApplication(String s);

	public String getCdcCatego();

	public void setCdcCatego(String cdcCatego);

	public String getCdcRne();

	public void setCdcRne(String cdcRne);

	public List<String> getListeProfils();

	public void setListeProfils(List<String> listeProfils);

	public List<String> getListeAppliOrganisations();

	public void setListeAppliOrganisations(List<String> listeAppliOrganisations);

	public void addAppliOrganisation(String s);

	/**Ajout d'un profil à une application
	 * Un test vérifie si ce profil n'est pas déjà affecté à l'application (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnProfil dn du profil que l'on souhaite ajouter 
	 */
	public void addProfil(String dnProfil);

	/**
	 * Suppression d'un des profils d'une application
	 * Si le profil n'était pas affecté à la personne un message est généré dans la log
	 * @param dnProfil
	 */
	public void removeProfil(String dnProfil);

	public String getUrl();

	public void setUrl(String url);

	public List<byte[]> getListePasswords();

	public void setListePasswords(List<byte[]> password);

	public void addPassword(byte[] s);

	public List<String> getListeRolesApplicatifs();

	public void setListeRolesApplicatifs(List<String> listeRolesApplicatifs);

	public void addRoleApplicatif(String dnRole);

	public void removeRoleApplicatif(String dnRole);

	public String getDbUri();

	public void setDbUri(String dbUri);

	/**
	 * Liste des DN des owners de l'application
	 * @return
	 */
	public List<String> getListeOwners();

	public void setListeOwners(List<String> listeOwners);

	/**Ajout d'un owner 
	 * Un test vérifie si cette personne n'est pas déjà owner (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnOwner dn de la personne que l'on souhaite nommer owner
	 */
	public void addOwner(String dnOwner);

	/**
	 * Suppression d'un owner
	 * Si la personne n'est pas dans la liste un log d'erreur est généré
	 * @param dnOwner dn de la personne que l'on souhaite retirer de la liste des owners
	 */
	public void removeOwner(String dnOwner);

	/**
	 * Indique si la personne dont le dn est passé en paramètre est owner de l'application
	 * @param dnOwner DN de la personne testée
	 * @return true si la personne est owner du profil, false sinon
	 */
	public boolean isOwner(String dnOwner);

	/**
	 * Liste des DN des managers du profil (ie les personnes habilitées à modifier le profil
	 * @return
	 */
	public List<String> getListeManagers();

	public void setListeManagers(List<String> listeManagers);

	/**Ajout d'un manager à l'application
	 * Un test vérifie si cette personne n'est pas déjà manager (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnManager dn de la personne que l'on souhaite ajouter comme manager
	 */
	public void addManager(String dnManager);

	/**
	 * Suppression d'un manager de l'application
	 * Si la personne n'est pas dans la liste un log d'erreur est généré
	 * @param dnManager dn de la personne que l'on souhaite retirer de la liste des managers
	 */
	public void removeManager(String dnManager);

	public List<String> getListeExplicitManagers();

	public void setListeExplicitManagers(List<String> listeExplicitManagers);

	public void addExplicitManager(String dnManager);

	public void removeExplicitManager(String dnManager);

	/**
	 * Indique si la personne dont le dn est passé en paramètre est manager de l'application
	 * @param dnMember DN de la personne testée
	 * @return true si la personne est manager du profil, false sinon
	 */
	public boolean isManagedBy(Person p);

	public List<String> getDerMaj();

	public void setDerMaj(List<String> derMaj);

	public void addDerMaj(String derMaj);

	public List<String> getModAuth();

	public void setModAuth(List<String> modAuth);

	public void addModAuth(String modAuth);

	public List<String> getUrlLogout();

	public void setUrlLogout(List<String> urlLogout);

	public void addUrlLogout(String url);

	public List<String> getProprietes();

	public void setProprietes(List<String> proprietes);

	public void addProprietes(String prop);

	public String getNature();

	public void setNature(String nature);

	public String getContexte();

	public void setContexte(String contexte);


	public String getDn();

	/**
	 * 
	 * @param cn
	 * @return
	 */
	public String findFullDn(String cn);

	/**
	 * Recherche une application grâce à son identifiant exact
	 * @param identifiant de l'application
	 * @return application recherchée
	 * @throws ToutaticeAnnuaireException 
	 */
	public Application findApplication(String id)
			throws ToutaticeAnnuaireException;

	/**
	 * Recherche toutes les applications de la base
	 * @return liste des applications existantes
	 */
	public List<Application> findAllApplis();

	/**
	 * Recherche toutes les applications disponibles pour un utilisateur donné grâce à son profil
	 * @param utilisateur concerné
	 * @return liste des applications disponibles
	 */
	public List<Application> findApplisUser(Person user);

	public List<Application> findApplisUser(Person user, String contexte);

	/**
	 * Recherche toutes les applications disponibles pour un utilisateur donné grâce à ses profils et à ses roles
	 * @param utilisateur concerné
	 * @return liste des applications disponibles
	 */
	public List<Application> findApplisDispo(Person user);

	public List<Application> findApplisCatego(Person user, String categorie);

	/**
	 * Recherche les roles d'un utilisateur pour chacune des applications de la liste
	 * @param liste des applications
	 * @param user utilisateur pour lequel on veut les roles auxquels il peut accéder
	 * @return liste des applications enrichies avecl a liste des roles de l'utilisateur (dans l'attribut rolesUser)
	 * @throws ToutaticeAnnuaireException 
	 */
	/*public List<Application> findRolesUsersParApplis(List<Application> liste, Person user) throws ToutaticeAnnuaireException
	{
		logger.debug("entree dans la methode Application.findRolesUsersParApplis)");
		RoleApplicatif role = (RoleApplicatif) context.getBean("roleApplicatif");
		
		for(Application app : liste)
		{
			for (String nomRole : app.getListeRolesApplicatifs()) {
				
				try {
					role = role.findRoleByDn(nomRole);
				} catch (ToutaticeAnnuaireException e) {
					throw e;
				}
	
				if (role != null) {
					if (role.getListeMembers().contains(user.getDn())) {
						String libRole = role.getCn();
						if (role.getDescription() != null) {
							libRole = libRole.concat(" - "
									+ role.getDescription());
						}
						app.addRoleUser(libRole);
					} else {
						for (String profilUser : user.getListeProfils()) {
							if (role.getListeProfils().contains(profilUser)) {
								String libRole = role.getCn();
								if (role.getDescription() != null) {
									libRole = libRole.concat(" - "
											+ role.getDescription());
								}
								app.addRoleUser(libRole);
								break;
							}
						}
					}
				}
			}
		}
		return liste;
	}*/
	/**
	 * Recherche des roles applicatifs que possède la personne passée en paramètre pour l'application sur laquelle
	 * s'applique la méthode
	 * @throws ToutaticeAnnuaireException 
	 */
	public ArrayList<RoleApplicatif> findRole(Person user)
			throws ToutaticeAnnuaireException;

	/**
	 * Recherche les applications correspondant aux critères
	 * @param id ou début de l'id des applications recherchées
	 * @param nom ou début du nom des applications recherchées
	 * @param liste des catégories auxquelles doivent appartenir les applications recherchées
	 * @return liste des applications correspondant aux critères
	 */
	public List<Application> findApplisCritere(String id, String nom,
			List<String> listeCates);

	/**
	 * Recherche les applications correspondant aux critères et autorisées pour un utiliseur donné
	 * @param utilisateur pour lequel les applications doivent etre autorisées
	 * @param id ou début de l'id des applications recherchées
	 * @param nom ou début du nom des applications recherchées
	 * @param liste des catégories auxquelles doivent appartenir les applications recherchées
	 * @return liste des application correspondant aux critères
	 */
	public List<Application> findApplisCritere(Person user, String id,
			String nom, List<String> listeCates);

	/**
	 * Recherche d'applications par nom
	 * @param id ou début de l'id des applications recherchées
	 * @param nom ou début du nom des applications recherchées
	 * @return liste des application correspondant aux critères
	 */
	public List<Application> findApplisNom(String id, String nom);

	/**
	 * Recherche des applications grâce à leur DN
	 * Fonction utilisée notamment pour faire de l'auto-complétion
	 * @param dn début du DN des applications recherchées (préfixées par "ENTApplicationId=")
	 * @return liste des applications correspondant aux critères
	 */
	public List<Application> findApplisDebutDn(String dn);

	/**
	 * Recherche des applications appartenant à un groupe d'applications donné
	 * @param groupe groupe d'application pour lequel on veut la liste des applications
	 * @return liste des applications du groupe
	 */
	public List<Application> findListeApplisGrp(GroupesApplications groupe);

	/**
	 * Recherche des applications appartenant à un groupe d'applications donné et qui sont autorisées
	 * pour l'utilisateur passé en paramètre
	 * @param user l'utilisateur habilité à utiliser les applications
	 * @param groupe groupe d'applications pour lequel on veut la liste des applications
	 * @return liste des applications recherchées
	 */
	public List<Application> findListeApplisGrp(Person user,
			GroupesApplications groupe);

	public List<Application> findListeApplisStructure(Structure str);

	public List<Application> findListeApplisStructure(Person user, Structure str);

	public List<Application> findListeApplisStructure(Person user,
			Structure str, String contexte);

	public List<Application> findListeApplisStructureGereesByUser(Person user,
			Structure str);

	/**
	 * Indique si l'utilisateur est autorisé à utiliser l'application
	 * (en fonction de ses roles applicatif et de ses profils)
	 * @param user Utilisateur pour lequel on teste l'accès
	 * @return true si la personne est autorisée à accéder à l'application, false sinon
	 */
	public boolean autorise(Person user);

	/**
	 * Indique si l'utilisateur a un profil qui est autorisé par l'application
	 * @param user Utilisateur pour lequel on teste l'accès
	 * @return true si l'utilisateur a un profil habilité, false sinon
	 */
	public boolean autoriseProfil(Person user);

	/**
	 * Mise à jour d'une application dans l'annuaire 
	 * Attention : Mise à jour des attributs nom, description, url, profils et managers uniquement
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updateAppli() throws ToutaticeAnnuaireException;

	/**
	 * Mise à jour de la liste des profils d'une application
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updateProfil() throws ToutaticeAnnuaireException;

	/**
	 * Création d'une application dans l'annuaire
	 * @throws ToutaticeAnnuaireException 
	 */
	public void create() throws ToutaticeAnnuaireException;


}