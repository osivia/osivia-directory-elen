package fr.toutatice.outils.ldap.entity;

import java.util.List;

import org.osivia.portal.api.directory.DirectoryBean;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface Organisation extends DirectoryBean {

	/**
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 
	 * @param id
	 */
	public void setId(String id);

	/**
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description);

	/**
	 * 
	 * @return
	 */
	public String getLocalisation();

	public String getCodePostal();

	public void setCodePostal(String codePostal);

	public String getAdresse();

	public void setAdresse(String adresse);

	public String getTelephone();

	public void setTelephone(String telephone);

	public String getNumeroFax();

	public void setNumeroFax(String numeroFax);

	public String getSeeAlso();

	public void setSeeAlso(String seeAlso);

	public String getDisplayName();

	public void setDisplayName(String displayName);

	/**
	 * 
	 * @param localisation
	 */
	public void setLocalisation(String localisation);


	public List<String> getListeProfils();

	public void setListeProfils(List<String> listeProfils);

	public void addProfil(String dnProfil);

	public void removeProfil(String dnProfil);

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

	public boolean isManagedBy(Person p);

	/**
	 * Recherche d'une organsiation par son numéro
	 * @param rne numéro de la structure recherchée (RNE)
	 * @return structure recherchée
	 * @throws ToutaticeAnnuaireException
	 */
	public Organisation findOrganisation(String rne);

	public Organisation findOrganisationByDn(String dn)
			throws ToutaticeAnnuaireException;

	public List<Organisation> findOrganisationPersonneByProfil(Person p);

	public List<Organisation> findAllOrganisations();

	public List<Organisation> findOrganisationByPrefixe(String prefixe);

	public List<Organisation> findOrganisationsMultiCriteres(String rne,
			String nom);
	
	public boolean isEtablissement();

	public List<Organisation> findFromFiltre(String filtre, String critereTri);

	public List<Profil> findProfilsMission();

	public boolean contientProfil(String dnProfil);

	public boolean contientProfil(List<String> listeDnProfils);

	public void update() throws ToutaticeAnnuaireException;


}