package fr.toutatice.outils.ldap.dao;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.core.LdapTemplate;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface PersonDao {

	public void setPrenom(String s);

	public void setPrenomNom(String s);

	public void setNomPrenom(String s);

	public void setNom(String s);

	public void setId(String s);

	public void setAlias(String s);

	public void setSexe(String s);

	public void setRne(String s);

	public void setPassword(String s);

	public void setIdsurcharge(String s);

	public void setProfils(String s);

	public void setCategorie(String s);

	public void setClasseObjet(String s);

	public void setBASE_DN(String s);

	public void setTitle(String s);

	public void setEmail(String s);

	public void setDivcod(String s);

	public void setDateNaissance(String s);

	public void setManager(String s);

	public void setExplicitManager(String m);

	public void setSourceSI(String s);

	public void setElevesConcernes(String s);

	public void setIdSiecle(String s);

	public void setPersonJointure(String s);

	public void setNationalProfil(String s);
	
	public void setPersonalTitle(String s);
	public void setDisciplinePoste(String s);

	public LdapTemplate getLdapTemplateEcriture();

	public void setLdapTemplateEcriture(LdapTemplate ldapTemplateEcriture);

	public LdapTemplate getLdapTemplateLecture();

	public void setLdapTemplateLecture(LdapTemplate ldapTemplateLecture);

	public LdapTemplate getLdapTemplateLectureNonPoolee();

	public void setLdapTemplateLectureNonPoolee(
			LdapTemplate ldapTemplateLectureNonPoolee);

	/**
	 * Construit le DN complet d'une personne
	 * @param uid identifiant de la personne dont on veut le DN
	 * @return DN de la personne (avec la base de l'annuaire LDAP, défini dans le fichier properties)
	 */
	public String buildFullDn(String uid);

	/**
	 * Recherche d'une personne par son uid
	 * @param uid Id exact de la personne recherché
	 * @return Personne recherchée
	 * @throws ToutaticeAnnuaireException 
	 */
	public Person findByPrimaryKey(String uid);

	/**
	 * Recherche d'une personne par son DN 
	 * @param dn DN exact de la personne recherchée
	 * @return personne recherchée
	 * @throws ToutaticeAnnuaireException
	 */
	public Person findByDn(String dn) throws ToutaticeAnnuaireException;

	/**
	 * Recherche de personnes grâce à un id ou une partie de l'id
	 * @param uid Id ou partie de l'Id des personnes recherchées
	 * @return Liste des personnes correspondan à l'Id passé en paramètre
	 */
	public List<Person> getPersonByUid(String uid);

	/**
	 * Recherche de personnes par nom
	 * @param name nom ou début du nom des personnes recherchées
	 * @return liste des personnnes correspondant au nom
	 */
	public List<Person> getPersonByName(String name);

	/**
	 * Recherche de personnes par adresse email
	 * @param mail email ou début de l'email des personnes recherchées
	 * @return liste des personnes correspondant à l'adresse email
	 */
	public List<Person> getPersonByMail(String mail);

	/**
	 * Recherche de personnes par leur TITLE
	 * @param titre titre de la personne recherchée
	 * @return liste des personnes dont le titre correspond à celui passé en paramètre
	 */
	public List<Person> getPersonByTitle(String titre);

	/**
	 * Recherche de personnes par établissement
	 * @param rne rne ou début du rne des personnes recherchées
	 * @return Liste des personnes correspondant au rne passé en paramètre
	 */
	public List<Person> getPersonByRne(String noRne);

	/**
	 * Recherche de personnes par nom, identifiant et établissement
	 * @param nomId nom ou identifiant de la personne recherchée (en entier ou le début du nom/identifiant)
	 * @param noRne numéro exact de l'établissement 
	 * @return liste des personnes correspondant aux critères
	 */
	public List<Person> getPersonByNomIdRne(String nomId, String noRne);

	/**
	 * Recherche de personnes multi-critères : identifient ou nom, numéro d'établissement,
	 * adresse email, titre
	 * @param nomId début du nom ou de l'identifiant
	 * @param noRne numéro exact de l'établissement
	 * @param mail début de l'adresse email
	 * @param titre début du titre de la personne
	 * @return liste des personnse correspondant aux critères
	 */
	public List<Person> getPersonByCriteres(String nomId, String noRne,
			String mail, String titre, String critereTri);

	/**
	 * Recherche de personnes multi-critères : identifient ou nom, numéro d'établissement,
	 * adresse email, titre
	 * @param nomId début du nom ou de l'identifiant
	 * @param noRne numéro exact de l'établissement
	 * @param mail début de l'adresse email
	 * @param titre début du titre de la personne
	 * @param divcod filtre sur le service
	 * @return liste des personnse correspondant aux critères
	 */
	public List<Person> getPersonByCriteres(String nomId, String noRne,
			String mail, String titre, String divcod, String critereTri);
	
	public List<Person> findPersonneMultiCriteres(String nomId, String rne,
			List<String> listeDnProfils, String filtre, String critereTri);

	public List<Person> findListePersonnesByDnProfil(String dnProfil);

	public Person findEleveByIdSiecle(String id);
	
	public List<Person> findParents(String idSiecle);

	/**
	 * Recherche des personnes ayant un attribut EntPersonSmdp
	 * (personne ayant théoriquement un mot de passe surchargé par
	 * la personne dont l'id est renseigné dans ce paramètre)
	 * @return liste des personnes ayant cet attribut
	 */
	public List<Person> getSurcharge(String critereTriResultat);

	/**
	 * Recherche des personnes ayant un mot de passe surchargé par la personne 
	 * dont l'id est passé en paramètre
	 * @return liste des personnes surchargées par la personne dont l'id est passé en paramètre
	 */
	public List<Person> getSurchargeParUtilisateur(String uid,
			String critereTriResultat);

	/**
	 * Mise à jour des données d'une personne
	 * @param p Personne à mettre à jour
	 * @throws ToutaticeAnnuaireException
	 * @throws javax.naming.NamingException 
	 */
	public void update(Person p) throws ToutaticeAnnuaireException,
			javax.naming.NamingException;

	/**
	 * Méthode appelé par ProfilDAO lors de la mise à jour d'un profil : si un membre a été ajouté à un profil il faut rajouter le profil dans la 
	 * liste des profils de cette personne
	 * @param dnMembre
	 * @param dnProfil
	 * @throws ToutaticeAnnuaireException
	 */
	public void ajoutProfilViaMajProfil(Person personMaj, String dnProfil)
			throws ToutaticeAnnuaireException;

	/**
	 * Méthode appelé par ProfilDAO lors de la mise à jour d'un profil : si un membre a été supprimé d'un profil il faut supprimer le profil dans la 
	 * liste des profils de cette personne
	 * @param dnMembre
	 * @param dnProfil
	 * @throws ToutaticeAnnuaireException
	 */
	public void supprimerProfilViaMajProfil(Person personMaj, String dnProfil)
			throws ToutaticeAnnuaireException;

	/**
	 * Mise à jour des profils d'une personne
	 * @param p personne à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 * @throws javax.naming.NamingException 
	 */
	public void updateProfil(Person personMaj)
			throws ToutaticeAnnuaireException, javax.naming.NamingException;

	public void updateImplicitManagers(Person personMaj)
			throws ToutaticeAnnuaireException;

	public void updateExplicitManagers(Person personMaj)
			throws ToutaticeAnnuaireException;

	/**
	 * Mise à jour des Rne d'une personne
	 * @param p personne à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updateRne(Person p) throws ToutaticeAnnuaireException;

	/**
	 * Mise à jour de l'attribut UserPassword
	 * @param p Personne à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updatePassword(Person p) throws ToutaticeAnnuaireException;

	public boolean authenticate(String uid, String mdpNonCode);

	/**
	 * Mise à jour du paramètre EntPersonSmdp
	 * Si le paramètre est vide on supprime l'attribut (la recherche des users suchargés se base sur la présence de l'attribut)
	 * Si il est renseigné on créé l'attribut
	 * @param p Personne à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 */
	public void addPersonSmdp(Person p) throws ToutaticeAnnuaireException;

	/**
	 * Suppression de l'attribut EntPersonSmdp d'une personne
	 * @param p Personne à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 */
	public void deletePersonSmdp(Person p) throws ToutaticeAnnuaireException;

	/**
	 * Miseà  jour de l'adresse mail d'une personne
	 * @param p personne dont il faut mettre à jour l'adresse mail
	 * @throws ToutaticeAnnuaireException
	 */
	public void updateEmail(Person p) throws ToutaticeAnnuaireException;

	/**
	 * Méthode indiquant si un role est bien affecté à une personne
	 * @param user personne dont on veut connaitre le role
	 * @param cnRole nom du role à tester
	 * @return true si la personne correspond bien à ce role, false sinon
	 * @throws ToutaticeAnnuaireException
	 */
	@SuppressWarnings("unchecked")
	public boolean personHasRole(Person user, String cnRole);

	@SuppressWarnings("unchecked")
	public boolean personHasStructure(Person user, String rne);

	@SuppressWarnings("static-access")
	public List<Person> getListePersonnesAyantProfilTrie(String dnProfil,
			String critereTri);

	/**
	 * Création d'une personne dans l'annuaire
	 * @param p Personne à créér
	 * @throws ToutaticeAnnuaireException
	 */
	public void create(Person p) throws ToutaticeAnnuaireException;

	/**
	 * Suppression d'une personne dans l'annuaire
	 * @param p personne à supprimer
	 * @throws ToutaticeAnnuaireException 
	 */
	public void delete(Person p) throws ToutaticeAnnuaireException;

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException;

}