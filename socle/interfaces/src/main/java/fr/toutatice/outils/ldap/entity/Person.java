package fr.toutatice.outils.ldap.entity;

import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.osivia.portal.api.directory.DirectoryBean;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface Person extends DirectoryBean {

	/**
	 * Getter de l'identifiant de la personne
	 * @return
	 */
	public String getUid();

	/**
	 * Setter de l'identifiant de la personne
	 * @param uid
	 */
	public void setUid(String uid);

	/**
	 * Getter du DN (distinguished Name) de la personne
	 * Ramène le DN complet de l'objet (avec le base DN du type dc=ent-bretagne,dc=fr)
	 * @return
	 */
	public String getDn();

	/**
	 * Getter du display name (sous la forme "Prénom Nom")
	 * @return
	 */
	public String getDisplayName();

	/**
	 * Setter du displayName (sous la forme "Prénom Nom")
	 * @param displayName
	 */
	public void setDisplayName(String displayName);

	/**
	 * Getter du GivenName (Prénom)
	 * @return
	 */
	public String getGivenName();

	/**
	 * Setter du GivenName (Prénom)
	 * @param givenName
	 */
	public void setGivenName(String givenName);

	/**
	 * Getter du Sn (Nom de famille)
	 * @return
	 */
	public String getSn();

	/**
	 * Setter du Sn (Nom de famille)
	 * @param sn
	 */
	public void setSn(String sn);

	/**
	 * Getter du Cn (sous la forme "Nom Prenom")
	 * @return
	 */
	public String getCn();

	/**
	 * Setter du Cn (sous la forme "Nom Prenom")
	 * @param cn
	 */
	public void setCn(String cn);

	/**
	 * Getter de l'alias de la personne (pseudo)
	 * @return
	 */
	public String getAlias();

	/**
	 * Setter de l'alias de la personne (pseudo)
	 * @return
	 */
	public void setAlias(String alias);

	public String getPersonJointure();

	public void setPersonJointure(String personJointure);

	public String getNationalProfil();

	public void setNationalProfil(String nationalProfil);

	/**
	 * Getter de la date de naissance (sous la forme JJ/MM/AAAA)
	 * @return
	 */
	public String getDateNaissance();

	/**
	 * Setter de la date de naissance (sous la forme JJ/MM/AAAA)
	 * @param dateNaissance
	 */
	public void setDateNaissance(String dateNaissance);

	/**
	 * Getter de la liste des établissements affectés à la personne
	 * Renvoit la liste des identifiants des Rne (7 chiffres, une lettre)
	 * @return
	 */
	public List<String> getListeRnes();

	/**
	 * Getter du sexe de la personne (M/F)
	 * @return
	 */
	public String getSexe();

	/**
	 * Setter du sexe de la personne (M/F)
	 * @return
	 */
	public void setSexe(String sexe);

	public String getPersonalTitle();
	public void setPersonalTitle(String s);
	public String getDisciplinePoste();
	public void setDisciplinePoste(String s);
	/**
	 * Setter de la liste des identifiants des établissements affectés à la personne (7 chiffres, une lettre)
	 * @param rne
	 */
	public void setListeRnes(List<String> rne);

	/**
	 * Ajout d'un RNE à une personne
	 * Un test vérifie si rne n'est pas déjà affecté à la personne (case non sensitive), si c'est le cas message d'information est généré dans la log et le rne n'est pas ajouté
	 * @param rne (7 chiffres, une lettre)
	 */
	public void addRne(String rne);

	/**
	 * Suppression d'un des RNE d'une personne
	 * Si le rne n'était pas affecté à la personne un message d'information est généré dans la log
	 * @param rne (7 chiffres, une lettre)
	 */
	public void removeRne(String rne);

	/**
	 * Récupération de la liste des mots de passe (sous la forme d'une chaine de caractères encodée)
	 * @return
	 */
	public List<String> getListePasswords();

	/**
	 * Setter de la liste des mots de passe de la personne
	 * @param password liste des mots de passe encodés de la personne
	 * @throws ToutaticeAnnuaireException 
	 */
	public void setListePasswords(List<String> password)
			throws ToutaticeAnnuaireException;

	/**
	 * Ajout d'un mot de passe
	 * @param s mot de passe à ajouter (non crypté)
	 * @throws ToutaticeAnnuaireException 
	 */
	public void addPassword(String s) throws ToutaticeAnnuaireException;

	/**
	 * Ajout d'un mot de passe
	 * @param s mot de passe à ajouter (crypté)
	 * @throws ToutaticeAnnuaireException 
	 */
	public void addPasswordSSHA(String s) throws ToutaticeAnnuaireException;

	/**
	 * Récupération de la liste des DN des profils associés à la personne
	 * (de la forme cn=xxxxx,ou=profils,ou=groupes,dc=ent-bretagne,dc=fr)
	 * @return
	 */
	public List<String> getListeProfils();

	/**
	 * Setter de la liste des DN des profils associés à la personne
	 * (de la forme cn=xxxxx,ou=profils,ou=groupes,dc=ent-bretagne,dc=fr)
	 * @param profils
	 */
	public void setListeProfils(List<String> profils);

	/**Ajout d'un profil à une personne
	 * Un test vérifie si ce profil n'est pas déjà affecté à la personne (case non sensitive), si c'est le cas un message d'information est généré dans la log et le profil n'est pas ajouté
	 * @param dnProfil dn du profil que l'on souhaite ajouter (de la forme cn=xxxxx,ou=profils,ou=groupes,dc=ent-bretagne,dc=fr)
	 */
	public void addProfil(String dnProfil);

	/**
	 * Suppression d'un des profils d'une personne
	 * Si le profil n'était pas affecté à la personne un message d'information est généré dans la log
	 * @param dnProfil dn du profil que l'on souhaite supprimer (de la forme cn=xxxxx,ou=profils,ou=groupes,dc=ent-bretagne,dc=fr)
	 */
	public void removeProfil(String dnProfil);

	/**
	 * Indique si personne possède le profil passé en paramètre dans sa liste de profils (case non sensitive)
	 * @param dnProfil DN du profil testé (de la forme cn=xxxxx,ou=profils,ou=groupes,dc=ent-bretagne,dc=fr)
	 * @return true si la personne est associée au profil, false sinon
	 */
	public boolean hasProfil(String dnProfil);

	/**
	 * 
	 */

	public boolean isAnimateurWks(String nomEN);

	public boolean isAuthorizeApplication(String idApplication)
			throws ToutaticeAnnuaireException;

	/**
	 * Récupération de l'identifiant de l'utilisateur qui a effectué une surcharge du mot de passe sur la personne
	 * Si le mot de passe n'est pas surchargé cet attribut est null
	 * @return
	 */
	public String getIdSurcharge();

	/**
	 * Stockage de l'identifiant de l'utilisateur ayant effectué une surcharge du mot de passe de l'utilisateur
	 * @param idSurcharge
	 */
	public void setIdSurcharge(String idSurcharge);

	public String getMotifSurcharge();

	public void setMotifSurcharge(String motifSurcharge);

	public String getTypeSurcharge();

	public void setTypeSurcharge(String typeSurcharge);

	/**
	 * Récupération de la source ayant alimenté les données de la personne
	 * @return AA pour l'annuaire académique, BEA pour la base élève académique, EXTACA pour les personnes extérieures à l'académie, HSI pour hors systme d'information,...
	 */
	public String getSourceSI();

	/**
	 * Alimentation de la source ayant alimenté les données de la personne
	 * @param sourceSI AA pour l'annuaire académique, BEA pour la base élève académique, EXTACA pour les personnes extérieures à l'académie, HSI pour hors systme d'information,...
	 */
	public void setSourceSI(String sourceSI);

	/**
	 * Récupération de la liste des identifiants des élèves liés à la personne
	 * @return liste d'identifiants LDAP
	 */
	public List<String> getListeUidElevesConcernes();

	/**
	 * Alimentation de la liste des identifiants des élèves liés à la personne
	 * @param liste d'identifiants LDAP
	 */
	public void setListeUidElevesConcernes(List<String> listeUidElevesConcernes);

	/**
	 * Récupération de la liste des id siècle des élèves liés à la personne
	 * @return liste d'identifiants SIECLE
	 */
	public List<String> getListeIdSiecleElevesConcernes();

	/**
	 * Alimentation de la liste des id siècle des élèves liés à la personne
	 * @param liste d'identifiants SIECLE
	 */
	public void setListeIdSiecleElevesConcernes(
			List<String> listeIdSiecleElevesConcernes);

	/**
	 * Récupération de l'identifiant SIECLE de la personne
	 * @return
	 */
	public String getIdSiecle();

	/**
	 * Alimentation de l'identifiant SIECLE de la personne
	 * @param idSiecle
	 */
	public void setIdSiecle(String idSiecle);

	/**
	 * Ajout d'un identifiant siecle lié à cette personne
	 * ie. ajout d'un enfant à cette personne
	 * L'ajout se fait dans la liste des id siecles et dans la liste des id ldap
	 * @param id id siecle à associer à la personne
	 */
	public void addIdSiecleElevesConcernes(String id);

	/**
	 * Indique si la personne a un mot de passe surchargé (ie 2 mots de passe)
	 * @return true si il y a plus d'un mot de passe, false sinon
	 */
	public boolean isUserSurcharged();

	/**
	 * Récupération de la liste des DN des roles applicatifs associés à la personne
	 * La recherche se fait sur l'identifiant de la personne (ie son uid est renseigné en member du role)
	 * et sur la liste des profils de la personne (ie un de ses profils est renseigné en EntRoleAppliProfils du rôle)
	 * ATTENTION : cette recherche ne prend pas en compte les éventuels FILTER des rôles applicatifs
	 * @return liste des Dn des rôles associés (sous la forme DN: cn=xxxxx,ou=RolesApplicatifs,ou=groupes,dc=ent-bretagne,dc=fr)
	 */
	public List<String> getListeRoles();

	/**
	 * Methode vérifiant si une personne détient le rôle passé en paramètre
	 * La recherche vérifie :
	 * 1) si le role possède l'id de la personne dans sa liste de member 
	 * 2) si le role possède un des profils de la personne dans sa liste de profils autorisés
	 * 3) si un des filtres (requête LDAP) du rôle correspond à cette personne
	 * @param user personne dont on veut tester l'appartenance à un role
	 * @param cnRole nom du role
	 * @return true si la personne détient le role, false sinon
	 */
	public boolean hasRole(String cnRole);

	/**
	 * Méthode vérifiant si une personne est affectée à la structure passée en paramètre
	 * Une personne est affectée à une structure si elle possède un des profils de la structure
	 * @param rne rne de la structure
	 * @return true si la personne est affectée à la structure, false sinon
	 */
	public boolean hasStructure(String rne);

	/**
	 * Récupération du TITLE de la personne
	 * @return ELE pour élève, ENS pour enseignant, EDU, DOC, DIR,...
	 */
	public String getTitle();

	/**
	 * Setter du TITLE de la personne
	 * @param title ELE pour élève, ENS pour enseignant, EDU, DOC, DIR,...
	 */
	public void setTitle(String title);

	/**
	 * Récupération de l'adresse email de la personne
	 * @return
	 */
	public String getEmail();

	/**
	 * Setter de l'adresse email de la personne
	 * @param email
	 */
	public void setEmail(String email);

	/**
	 * Récupération du divcod de la personne
	 * @return
	 */
	public String getDivcod();

	/**
	 * Setter du divcod de la personne
	 * @param divcod
	 */
	public void setDivcod(String divcod);

	/**
	 * Liste des DN des Managers Implicites (les managers implicites sont ceux désignés par des règles de gestion lors de l'alimentation de l'annuaire)
	 * Managers = personnes habilitées à modifier les données de la personne), sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr"
	 * @return
	 */
	public List<String> getListeImplicitManagers();

	/**
	 * Setter de la liste des managers implicites (les managers implicites sont ceux désignés par des règles de gestion lors de l'alimentation de l'annuaire)
	 * Managers = personnes habilitées à modifier les données de la personne), sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr"
	 * @param listeManagers
	 */
	public void setListeImplicitManagers(List<String> listeManagers);

	/**Ajout d'un manager implicite (les managers implicites sont ceux désignés par des règles de gestion lors de l'alimentation de l'annuaire)
	 * ATTENTION : cette méthode ne doit être appelé que dans des cas très particuliers. Utiliser de préférence les managers explicites
	 * Un test vérifie si cette personne n'est pas déjà manager (case non sensitive), si c'est le cas un log d'information est généré et le manager n'est pas ajouté
	 * @param dnManager dn de la personne que l'on souhaite ajouter comme manager (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 */
	public void addImplicitManager(String dnManager);

	/**
	 * Suppression d'un manager implicite (les managers implicites sont ceux désignés par des règles de gestion lors de l'alimentation de l'annuaire)
	 * ATTENTION : cette méthode ne doit être appelé que dans des cas très particuliers. Utiliser de préférence les managers explicites
	 * Si la personne n'est pas dans la liste un log d'information est généré
	 * @param dnManager dn de la personne que l'on souhaite retirer de la liste des managers (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 */
	public void removeImplicitManager(String dnManager);

	/**
	 * Récupération de la liste des managers explicites (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 * Les managers explicites sont les managers rajoutés manuellement à la personne
	 * @return
	 */
	public List<String> getListeExplicitManagers();

	/**
	 * Setter de la liste des managers explicites (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 * Les managers explicites sont les managers rajoutés manuellement à la personne
	 * @param listeExplicitManagers
	 */
	public void setListeExplicitManagers(List<String> listeExplicitManagers);

	/**
	 * Ajout d'un manager explicite 
	 * Si le manager est déjà renseigné en manager implicite ou explicite un message d'info est inscrit dans la log et le manager n'est pas rajouté
	 * @param dnManager (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 */
	public void addExplicitManager(String dnManager);

	/**
	 * suppression d'un manager explicite 
	 * si la personne passée en paramètre n'était pas manager explicite un message d'info est inscrit dans la log
	 * @param dnManager (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 */
	public void removeExplicitManager(String dnManager);

	/**
	 * Indique si la personne dont le dn est passé en paramètre est manager de la personne sur laquelle la méthode s'applique
	 * La recherche porte sur les managers explicites et implicites
	 * Pour chaque type de manager on vérifie :
	 * 		- si l'identifiant de la personne est renseigné comme manager
	 * 		- si un des profils de la personne est renseigné comme manager
	 * @param dnMember DN de la personne testée (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 * @return true si la personne est manager du profil, false sinon
	 */
	public boolean isManagedBy(Person p);

	/**
	 * Récupère le DN complet de la personne (avec la base de l'annuaire)
	 * @param uid identifiant de la personne
	 * @return dn de l'objet dans l'annuaire (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 */
	public String findFullDn(String uid);

	/**
	 * Recherche d'une personne par son id exact
	 * @param uid identifiant de la personne
	 * @return personne recherchée (null si la personne n'a pas été trouvée)
	 */
	public Person findUtilisateur(String uid);

	/**
	 * Recherche de personnes dans l'annuaire à partir du début de leur identifiant
	 * @param uid début de l'identifiant de la ou des personne(s) recherchée(s)
	 * @return liste des personnes correspondant au critère
	 */

	public List<Person> findUtilisateursUID(String uid);

	/**
	 * Recherche d'une personne par son DN exact
	 * @param dn (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 * @return personne recherchée (null si la personne n'a pas été trouvée)
	 */
	public Person findPersonByDn(String dn);

	/**
	 * Recherche de personnes dans l'annuaire à partir du début de leur nom 
	 * @param nom début du nom de la ou des personne(s) recherchée(s)
	 * @return liste des personnes correspondant au critère
	 */
	public List<Person> findUtilisateursNom(String nom);

	/**
	 * Recherche de personnes dans l'annuaire en fonction du début du numéro de leur établissement
	 * Attention : cette requête peut ramener un grand nombre de résultats si mal utilisée
	 * @param rne début du numéro de l'établissement
	 * @return liste des personnes correspondant au critère
	 */
	public List<Person> findUtilisateursRNE(String rne);

	/**
	 * Recherche de personnes dans l'annuaire en fonction du début de leur adresse email
	 * @param email début de l'adresse email
	 * @return liste des personnes correspondant au critère
	 */
	public List<Person> findUtilisateursMail(String email);

	/**
	 * Recherche de personnes dans l'annuaire en fonction du début de leur titre
	 * @param title début du titre 
	 * @return liste des personnes correspondant au critère
	 */
	public List<Person> findUtilisateursTitle(String title);

	/**
	 * Recherche de personnes en fonction d'un profil qui leur est affecté
	 * @param dnProfil DN du profil en question
	 * @return liste des personnes possédant le profil passé en paramètre
	 */
	public List<Person> findListePersonnesByDnProfil(String dnProfil);

	/**
	 * Recherche de personnes en fonction d'un profil qui leur est affecté
	 * La liste ramené sera trié en fonction d'un critère passé en paramètre
	 * @param dnProfil dnProfil DN du profil 
	 * @param critereTri critère de tri pour la liste de résultat (cn pour un tri sur le nom/prénom par exemple)
	 * @return liste des personnes possédant le profil passé en paramètre trié suivant le critère choisi
	 */
	public List<Person> findListePersonnesAvecProfil(String dnProfil,
			String critereTri);

	/**
	 * Recherche multicritères de personnes dans l'annuaire
	 * @deprecated remplacée par la méthode findPersonneMultiCriteres
	 * @param nom chaine représentant le début du nom ou de l'identifiant de la (ou des) personne(s) recherchée(s)
	 * @param rne rne de la (ou des) personne(s) recherchée(s) 
	 * @return liste des personnes correspondant aux critères
	 */
	@Deprecated
	public List<Person> getPersonByNomIdRne(String nom, String rne);

	/**
	 * Recherche de personnes par critères
	 * @param nomId chaine représentant le début du nom ou de l'identifiant de la (ou des) personne(s) recherchée(s)
	 * @param rne rne de la (ou des) personne(s) recherchée(s) 
	 * @param dnProfilList liste de profils affectés aux personnes recherchées
	 * @param filtre filtre LDAP supplémentaire pour la recherche (syntaxe LDAP !)
	 * @param critereTri critère pour trier les résultats (cn, displayName,...)
	 * @return
	 */
	public List<Person> findPersonneMultiCriteres(String nomId, String rne,
			List<String> dnProfilList, String filtre, String critereTri);

	/**
	 * Recherche multicritères de personnes dans l'annuaire
	 * @param nom début du nom ou de l'identifiant de la (ou des) personne(s) recherchée(s)
	 * @param rne début du numéro de l'établissement de la (ou des) personne(s) recherchée(s)
	 * @param email début de l'adresse email de la (ou des) personne(s) recherchée(s)
	 * @param title début du titre de la (ou des) personne(s) recherchée(s)
	 * * @param critereTri critère suivant lequel trier la liste (nom de l'attribut dans LDAP)
	 * @return liste des personnes correspondant aux critères
	 */
	public List<Person> getPersonByCriteres(String nom, String rne,
			String email, String title, String critereTri);

	/**
	 * Recherche multicritères de personnes dans l'annuaire
	 * @param nom début du nom ou de l'identifiant de la (ou des) personne(s) recherchée(s)
	 * @param rne début du numéro de l'établissement de la (ou des) personne(s) recherchée(s)
	 * @param email début de l'adresse email de la (ou des) personne(s) recherchée(s)
	 * @param title début du titre de la (ou des) personne(s) recherchée(s)
	 * @param divcod identifiant du service
	 * @param critereTri critère suivant lequel trier la liste (nom de l'attribut dans LDAP)
	 * @return liste des personnes correspondant aux critères
	 */
	public List<Person> getPersonByCriteres(String nom, String rne, String email, String title, String divcod, String critereTri);
	
	/**
	 * Recherche des utilisateurs ayant un mot de passe surchargé 
	 * Attention : La recherche se base sur la présence de l'attribut EntPersonSmdp 
	 * Si cet attribut est absent l'utilisateur ne sera pas retourné même s'il a plusieurs mots de passe
	 *
	 * @return liste des personnes ayant un mot de passe surchargé
	 */

	public List<Person> rechercherSurcharge();

	/**
	 * Recherche triée des utilisateurs ayant un mot de passe surchargé 
	 * Attention : La recherche se base sur la présence de l'attribut EntPersonSmdp 
	 * Si cet attribut est absent l'utilisateur ne sera pas retourné même s'il a plusieurs mots de passe
	 * @param critereTriResultat : permet de trier la liste en fonction de l'identifiant des utilisateurs (uid), leur nom de famille (cn),...
	 * @return
	 */
	public List<Person> rechercherSurcharge(String critereTriResultat);

	/**
	 * Recherche des utilisateurs ayant un mot de passe surchargé par une personne donnée
	 * @param uid identifiant de la personne ayant surchargé le mot de passe
	 * @return liste des personnes ayant un mot de passe surchargé par cette personne
	 */

	public List<Person> rechercherSurchargeParUtilisateur(String uid);

	/**
	 * Recherche des utilisateurs ayant un mot de passe surchargé par une personne donnée
	 * @param uid identifiant de la personne ayant efefctué des surcharges
	 * @param critereTriResultat : permet de trier la liste en fonction de l'identifiant des utilisateurs (uid), leur nom de famille (cn),...
	 * @return
	 */
	public List<Person> rechercherSurchargeParUtilisateur(String uid,
			String critereTriResultat);

	/**
	 * Indique si la personne est membre explicite du profil dont le nom est passé en paramètre
	 * @param cnProfil nom du profil
	 * @return true si la personne est membre explicite, false sinon
	 */
	public boolean isMemberExplicit(String cnProfil);

	/**
	 * Création d'une personne dans l'annuaire
	 * @throws ToutaticeAnnuaireException 
	 */
	public void create() throws ToutaticeAnnuaireException;

	/**
	 * Suppression de la personne de l'annuaire
	 * @throws ToutaticeAnnuaireException
	 * @throws NamingException
	 */
	public void delete() throws ToutaticeAnnuaireException, NamingException;

	/**
	 * Modification d'une personne
	 * Modification des attributs nom, prénom, date de naissance, titre, email, liste des RNE, liste des Profils, liste des managers
	 * La mise à jour des profils entraine la modification des objets profils (maj de la liste de leurs membres)
	 * @throws ToutaticeAnnuaireException 
	 * @throws NamingException 
	 */

	public void update() throws ToutaticeAnnuaireException, NamingException;

	/**
	 * Mise à jour des managers implicites de la personne
	 * Cet attribut est mis à jour par LDAPoMatic, cette fonction ne devrait pas être utilisée sauf cas très spécifique
	 * @throws ToutaticeAnnuaireException
	 */
	public void updateManagersImplicites() throws ToutaticeAnnuaireException;

	/**
	 * Modification de l'adresse email d'une personne 
	 * Si il n'y a pas encore d'attribut email celui ci est créé
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updateEmail() throws ToutaticeAnnuaireException;

	/**
	 * Mise à jour du mot de passe d'une personne
	 * @param mdp nouveau mot de passe
	 * @throws ToutaticeAnnuaireException 
	 */

	public void updatePassword(String mdp) throws ToutaticeAnnuaireException;

	/**
	 * Vérification de la validité du mot de passe saisi
	 * @param mdp mot de passe renseigné
	 * @return true si le mot de passe est correct, faux dans le cas contraire
	 */
	public boolean verifMdp(String mdp);

	/**
	 * Surcharge du mot de passe à un utilisateur 
	 * Un utilisateur ne peut avoir que 2 mots de passe : 
	 * - Le premier est toujours celui qu'il utilise
	 * - Le deuxième est une surcharge et est utilisé pour l'assistance technique 
	 * Le mot de passe est à rentrer en clair, il sera ensuite codé en base64 SHA
	 * 
	 * @param mdpSurcharge mot de passe à ajouter (en clair)
	 * @param login identifiant de la personne effectuant la surcharge
	 * @throws ToutaticeAnnuaireException 
	 */
	/*public void surchargeMdp(String mdpSurcharge, String login) throws ToutaticeAnnuaireException {
		if (this.isUserSurcharged()) {
			loggerSurcharge.info("L'utilisateur "+login+" écrase la surcharge de "+this.getIdSurcharge()+" sur la personne "+this.getUid());
			this.deleteSurcharge(login);
		}
		String mdpEncode = fonctionBase64SSHA(mdpSurcharge);
		this.surchargeMdpEncode(mdpEncode, login);
	
	}*/

	public void surchargeMdp(String mdpSurcharge, String idSurcharge,
			String typeSurcharge, String motifSurcharge, String adresseIP)
			throws ToutaticeAnnuaireException;

	/**
	 * Surcharge du mot de passe d'un utilisateur 
	 * Un utilisateur ne peut avoir que 2 mots de passe : 
	 * - Le premier est toujours celui qu'il utilise
	 * - Le deuxième est une surcharge et est utilisé pour l'assistance technique 
	 * Le mot de passe est à rentrer déjà encodé
	 * 
	 * @param mdpSurcharge mot de passe à ajouter (mode de passe crypté)
	 * @param login identifiant de la personne effectuant la surcharge
	 * @throws ToutaticeAnnuaireException 
	 */
	/*public void surchargeMdpEncode(String mdpEncode, String login) throws ToutaticeAnnuaireException {
		List<String> password = new ArrayList<String>();
		password = this.getListePasswords();
		String pwduser = password.get(0);
		password.clear();
		password.add(pwduser);
		password.add(mdpEncode);
		this.setListePasswords(password);
		this.setIdSurcharge(login);
		try {
			personDao.addPersonSmdp(this);
			personDao.updatePassword(this);
			if(!login.trim().isEmpty()){
				loggerSurcharge.info("l'utilisateur "+login+" a surchargé le mot de passe de la personne "+this.getUid());
			}
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
	}*/

	public void surchargeMdpEncode(String mdpEncode, String idSurcharge,
			String typeSurcharge, String motifSurcharge, String adresseIP)
			throws ToutaticeAnnuaireException;

	/**
	 * Suppression de la surcharge de mot de passe 
	 * Le 1er mot de passe (celui de l'utilisateur) est conservé 
	 * Le 2ème mot de passe est effacé
	 * L'attribut servant à identifier la personne ayant surchargé le mot de passe est supprimé
	 * @throws ToutaticeAnnuaireException 
	 */
	/*public void deleteSurcharge(String login) throws ToutaticeAnnuaireException {
		this.setIdSurcharge("");
		List<String> password = this.getListePasswords();
		String pwduser = password.get(0);
		password.clear();
		password.add(pwduser);
		this.setListePasswords(password);
		personDao.updatePassword(this);
		personDao.deletePersonSmdp(this);
		loggerSurcharge.info("La surcharge sur l'utilisateur "+this.getUid()+" a été supprimée (Surcharge de type "+this.getTypeSurcharge()+" , motif : "+this.getMotifSurcharge()+", faite par :"+this.getIdSurcharge());
	}*/

	public void deleteSurcharge() throws ToutaticeAnnuaireException;

	/**
	 * Recherche du mot de passe initial d'une personne
	 * Dans le cas d'un élève il s'agit de sa date de naissance (sans les "/")
	 * @return mot de passe initial de le personne
	 */
	public String findMdpInitialEleve();

	/**
	 * Remise à zéro du mot de passe d'une personne
	 * (date de naissance dans le cas d'un élève)
	 * @throws ToutaticeAnnuaireException
	 */
	public void razMdp() throws ToutaticeAnnuaireException;

	/**
	 * Vérifie si le mot de passe de l'élève a déjà été changé
	 * teste si le pwd = date de naissance sans les / pour les élèves de l'éducation nationale)
	 * teste si le pwd = date de naissance avec les / pour les élèves du maritime)
	 * @return true si le mot de passe a été changé, false sinon
	 */
	public boolean eleveHasChangedPassword();

	/**
	 * Recherche des applications auxquelles la personne a le droit d'accéder
	 * @return liste des applications de la personne
	 */
	public List<Application> findApplicationsAutorisees();

	/**
	 * Recherche de la liste des applications que la personne peut gérer (ie dont elle est manager) 
	 * @return
	 */
	public List<Application> findApplicationsGerees();

	/**
	 * Recherche des profils associés à la personne.
	 * Cette recherche se fait dans les objets profils de l'annuaire, la liste ramenée peut différer de la liste des profils affectés à la personne dans sa fiche LDAP
	 * @return liste des profils d'une personne
	 */
	public List<Profil> findProfilsAssocies();

	/**
	 * Recherche de la liste des profils affectés à la personne et liés à une organisation donnée 
	 * @param idOrga RNE de l'organisation
	 * @return Liste des profils de cette personne
	 */
	public List<Profil> findProfilsAssociesByOrga(String idOrga);

	/**
	 * Recherche des profils gérés par une personne (ie dont elle est manager)
	 * @return liste des profils gérés
	 */
	public List<Profil> findProfilsGeres();

	/**
	 * Recherche de la liste des profils gérés par une personne et liés à un organisation donnée
	 * @param idOrga RNE de l'organisation
	 * @return liste des profils gérés dans cette organisation
	 */
	public List<Profil> findProfilsGeresParOrga(String idOrga);

	/**
	 * Recherche de la liste des profils gérés de façon implicite
	 * @return
	 */
	public List<Profil> findProfilsGeresImplicitement();

	/**
	 * Recherche de la liste des profils gérés de façon explicite
	 * @return
	 */
	public List<Profil> findProfilsGeresExplicitement();

	/**
	 * Recherche de la liste des profils gérés de façon implicite dans une organisation donnée
	 * @return
	 */
	public List<Profil> findProfilsGeresImplicitementParOrga(String idOrga);

	/**
	 * Recherche de la liste des profils gérés de façon explicite dans une organisation donnée
	 * @return
	 */
	public List<Profil> findProfilsGeresExplicitementParOrga(String idOrga);

	/**
	 * Indique si la personne a été créé via son compte ATEN, et est donc un parent d'élève
	 * @return true si il s'agit d'un parent, false sinon
	 */
	public boolean isParent();
	
	public List<Person> findParents();

	/**
	 * Recherche d'un élève via son identifiant SIECLE
	 * @param idSiecle
	 * @return élève correspondant
	 */
	public Person findEleveByIdSiecle(String idSiecle);

	/**
	 * Methode pour l'application de gestion des inscriptions à labomep
	 * Méthode non générique ! NE peut être utiliséep our la gestion des inscriptions à un autre service
	 * @param dn
	 * @param format
	 * @return
	 * @throws ToutaticeAnnuaireException
	 */
	public String findInfosCsvPerson(String dn, String format)
			throws ToutaticeAnnuaireException;

	/**
	 * Méthode utilisée pour construire une MAP avec les données d'une personne
	 * Cette méthode est utilisée par le portail la connexion d'un utilisateur, pour stocker ses données en session et les
	 * mettre à disposition des portlets.
	 * C'est la portlet Menu-Application qui instancie cette map
	 * @param datas
	 */
	public void populateMap(Map<String, Object> datas);

	/**
	 * L'égalité se juge sur l'identifiant LDAP de la personne
	 */
	public boolean equals(Object o);

}