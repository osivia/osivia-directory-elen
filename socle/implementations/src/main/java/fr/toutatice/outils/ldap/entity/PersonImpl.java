package fr.toutatice.outils.ldap.entity;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.ApplicationDao;
import fr.toutatice.outils.ldap.dao.PersonDao;
import fr.toutatice.outils.ldap.dao.ProfilDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;
import fr.toutatice.outils.ldap.util.ChargementCdcIdEtab;

/**
 * Personne physique issue de l'annuaire LDAP
 * @author aguihomat
 *
 */

@Service(value="person")
@Scope(value=BeanDefinition.SCOPE_PROTOTYPE)
public class PersonImpl implements ApplicationContextAware, Person{
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	protected static final Log loggerSurcharge = LogFactory.getLog("fr.toutatice.outils.ldap.surcharge");
	private static ApplicationContext context;  
	
	/**
	 * Factory, à instancier via l'IOC de SPRING
	 */
	@Autowired(required=false)
	private PersonDao personDao;
	
	
	@Autowired(required=false)
	ProfilDao profilDao;
	
	@Autowired(required=false)
	@Qualifier("applicationDao")
	ApplicationDao applicationDao;
	
	//------------------------------------------------------------------------------------------------------------------------
	// ATTRIBUTS DE L'OBJET PERSONNE
	//------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Identifiant de la personne (en général 1ère lettre du prénom - nom)
	 */
	private String uid;
	
	/**
	 * Nom de la personne tel qu'il doit être affiché
	 */
	private String displayName;
	
	/**
	 * Prénom de la personne
	 */
	private String givenName;
	
	/**
	 * Nom de famille de la personne
	 */
	private String sn;
	
	/**
	 * Nom et prénom de la personne
	 */
	private String cn;
	
	/**
	 * Surnom de la personne
	 */
	private String alias;
	/**
	 * Date de naissance de la personne
	 */
	private String dateNaissance;
	
	/**
	 * Sexe de la personne
	 */
	private String sexe;
	
	private String personalTitle;
	private String disciplinePoste;
	/**
	 * Person Jointure
	 */
	private String personJointure;
	/**
	 * 
	 */
	private String nationalProfil;
	/**
	 * Liste des numéros des établissements affectés à la personne
	 */
	private List<String> listeRnes = new ArrayList<String>();
	
	/**
	 * Liste des mots de passe encodés de l'utilisateur (maximum 2)
	 */
	//encodé au format byte[])
	private List<String> listePasswords = new ArrayList<String>();
	
	/**
	 * Liste des DN des profils affectés à la personne
	 */
	private List<String> listeProfils = new ArrayList<String>();
	
	/**
	 * Liste des DN des managers affectés à la personne
	 */
	private List<String> listeImplicitManagers = new ArrayList<String>();
	private List<String> listeExplicitManagers = new ArrayList<String>();
	
	/**
	 * Identifiant de la personne ayant effectué une surcharge de mot de passe sur la personne (le cas échéant)
	 */
	private String idSurcharge;
	private String motifSurcharge;
	private String typeSurcharge;
	/**
	 * Provenance des données : AA = annuaire académique
	 */
	private String sourceSI;

	/**
	 * Liste des DN des rôles applicatifs affectés à la personne
	 */
	//private ArrayList<String> listeRoles = new ArrayList<String>();

	/**
	 * Titre de la personne (ELE, INS, ADM,...)
	 */
	private String title;
	
	/**
	 * Adresse email de la personne
	 */
	private String email;
	
	/**
	 * Divcod de la personne
	 */
	private String divcod;
	
	
	/**
	 * Liste des id siècle des élèves reliés à la personne (enfants de la personne notamment)
	 */
	private List<String> listeIdSiecleElevesConcernes = new ArrayList<String>(); 
	
	/**
	 * Liste des identifiants LDAP des élèves reliés à la personne (enfants de la personne notamment)
	 */
	private List<String> listeUidElevesConcernes = new ArrayList<String>(); 
	
	
	/**
	 * id Siecle de la personne
	 */
	private String idSiecle;

	//------------------------------------------------------------------------------------------------------------------------
	// GETTERS ET SETTERS
	//------------------------------------------------------------------------------------------------------------------------
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getUid()
	 */
	public String getUid() {
		return uid;		}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setUid(java.lang.String)
	 */
	public void setUid(String uid) {
		this.uid = uid;	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getDn()
	 */
	public String getDn() {
		return personDao.buildFullDn(this.getUid());	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getDisplayName()
	 */
	public String getDisplayName() {
		return displayName;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getGivenName()
	 */
	public String getGivenName() {
		return givenName;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setGivenName(java.lang.String)
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getSn()
	 */
	public String getSn() {
		return sn;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setSn(java.lang.String)
	 */
	public void setSn(String sn) {
		this.sn = sn;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getCn()
	 */
	public String getCn() {
		return cn;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setCn(java.lang.String)
	 */
	public void setCn(String cn) {
		this.cn = cn;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getAlias()
	 */
	public String getAlias() {
		return alias;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setAlias(java.lang.String)
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getPersonJointure()
	 */
	public String getPersonJointure() {
		return personJointure;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setPersonJointure(java.lang.String)
	 */
	public void setPersonJointure(String personJointure) {
		this.personJointure = personJointure;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getNationalProfil()
	 */
	public String getNationalProfil() {
		return nationalProfil;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setNationalProfil(java.lang.String)
	 */
	public void setNationalProfil(String nationalProfil) {
		this.nationalProfil = nationalProfil;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getDateNaissance()
	 */
	public String getDateNaissance() {
		return dateNaissance;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setDateNaissance(java.lang.String)
	 */
	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getListeRnes()
	 */
	public List<String> getListeRnes() {
		return listeRnes;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getSexe()
	 */
	public String getSexe() {
		return sexe;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setSexe(java.lang.String)
	 */
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	
	public String getPersonalTitle() {
		return personalTitle;
	}
	public void setPersonalTitle(String personalTitle) {
		this.personalTitle = personalTitle;
	}
	
	public String getDisciplinePoste() {
		return disciplinePoste;
	}
	public void setDisciplinePoste(String disciplinePoste) {
		this.disciplinePoste = disciplinePoste;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setListeRnes(java.util.List)
	 */
	public void setListeRnes(List<String> rne) {
		this.listeRnes = rne;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#addRne(java.lang.String)
	 */
	public void addRne(String rne) {
		boolean existe=false;
		for(String s:this.getListeRnes()){
			if (s.equalsIgnoreCase(rne)){
				existe=true;
			}
		}
		if(!existe){
			this.listeRnes.add(rne);
		} else {
			logger.info("Erreur : ajout du rne "+rne+" à la personne "+this.getCn()+" - Ce rne est déjà affecté à cette personne");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#removeRne(java.lang.String)
	 */
	public void removeRne(String rne){	
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeRnes()) {
			if (!s.equalsIgnoreCase(rne)) {
				liste.add(s);
			}
		}
		if(this.getListeRnes().size()==liste.size()){
			logger.info("Erreur : suppression du rne "+rne+" de la personne "+this.getCn()+" - Ce rne n'est pas affecté ) cette personne");
		}
		this.setListeRnes(liste);	
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getListePasswords()
	 */
	public List<String> getListePasswords() {
		return this.listePasswords;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setListePasswords(java.util.List)
	 */
	public void setListePasswords(List<String> password) throws ToutaticeAnnuaireException {
		if(password.size()<=2){
			this.listePasswords = password;	}
		else{
			logger.info("Tentative d'associer plus de 2 mots de passe à la personne "+this.getUid());
			throw(new ToutaticeAnnuaireException("Tentative d'associer plus de 2 mots de passe à la personne "+this.getUid()));
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#addPassword(java.lang.String)
	 */
	public void addPassword(String s) throws ToutaticeAnnuaireException {
		if(this.listePasswords.size()<2){
			this.listePasswords.add(fonctionBase64SSHA(s));	
		}else{
			logger.info("Tentative d'associer plus de 2 mots de passe à la personne "+this.getUid());
			throw(new ToutaticeAnnuaireException("Tentative d'associer plus de 2 mots de passe à la personne "+this.getUid()));
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#addPasswordSSHA(java.lang.String)
	 */
	public void addPasswordSSHA(String s) throws ToutaticeAnnuaireException {
		if(this.listePasswords.size()<2){
			this.listePasswords.add(s);	
		}else{
			logger.info("Tentative d'associer plus de 2 mots de passe à la personne "+this.getUid());
			throw(new ToutaticeAnnuaireException("Tentative d'associer plus de 2 mots de passe à la personne "+this.getUid()));
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getListeProfils()
	 */
	public List<String> getListeProfils() {
		return listeProfils;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setListeProfils(java.util.List)
	 */
	public void setListeProfils(List<String> profils) {
		this.listeProfils = profils;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#addProfil(java.lang.String)
	 */
	public void addProfil(String dnProfil) {
		boolean existe=false;
		for(String s:this.getListeProfils()){
			if (s.equalsIgnoreCase(dnProfil)){
				existe=true;
			}
		}
		if(!existe){
			this.listeProfils.add(dnProfil);
		} else {
			logger.info("Erreur : ajout du profil "+dnProfil+" à la personne "+this.getCn()+" - Ce profil est déjà affecté à la personne");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#removeProfil(java.lang.String)
	 */
	public void removeProfil(String dnProfil) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeProfils()) {
			if (!s.equalsIgnoreCase(dnProfil)) {
				liste.add(s);
			}
		}
		if(this.getListeProfils().size()==liste.size()){
			logger.info("Erreur : suppression du profil "+dnProfil+" de la personne "+this.getCn()+" - Ce profil n'est pas affecté à cette personne");
		}
		this.setListeProfils(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#hasProfil(java.lang.String)
	 */
	public boolean hasProfil(String dnProfil) {
		boolean existe = false;
		for(String s : this.getListeProfils()){
			if(s.equalsIgnoreCase(dnProfil)){
				existe=true;
			}
		}
		return existe;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#isAnimateurWks(java.lang.String)
	 */
	
	public boolean isAnimateurWks(String nomEN){
		boolean isAnimateur=false;
		for(String dn:this.getListeProfils()){
			Profil p = profilDao.findByDn(dn);
			if(p!=null&&p.getType()!=null){
				if(p.getType().equalsIgnoreCase("space-group")){
					if(p.getCn().startsWith(nomEN+"_")){
						if(p.getCn().endsWith("_animateur")){
							isAnimateur=true;
							break;
						}
					}
				}
			}
		}
		
		return isAnimateur;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#isAuthorizeApplication(java.lang.String)
	 */
	public boolean isAuthorizeApplication(String idApplication) throws ToutaticeAnnuaireException{
		Application app = applicationDao.findByPrimaryKey(idApplication);
		return app.autorise(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getIdSurcharge()
	 */
	public String getIdSurcharge() {
		return idSurcharge;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setIdSurcharge(java.lang.String)
	 */
	public void setIdSurcharge(String idSurcharge) {
		this.idSurcharge = idSurcharge;	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getMotifSurcharge()
	 */
	public String getMotifSurcharge() {
		return motifSurcharge;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setMotifSurcharge(java.lang.String)
	 */
	public void setMotifSurcharge(String motifSurcharge) {
		this.motifSurcharge = motifSurcharge;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getTypeSurcharge()
	 */
	public String getTypeSurcharge() {
		return typeSurcharge;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setTypeSurcharge(java.lang.String)
	 */
	public void setTypeSurcharge(String typeSurcharge) {
		this.typeSurcharge = typeSurcharge;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getSourceSI()
	 */
	public String getSourceSI() {
		return sourceSI;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setSourceSI(java.lang.String)
	 */
	public void setSourceSI(String sourceSI) {
		this.sourceSI = sourceSI;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getListeUidElevesConcernes()
	 */
	public List<String> getListeUidElevesConcernes() {
		return listeUidElevesConcernes;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setListeUidElevesConcernes(java.util.List)
	 */
	public void setListeUidElevesConcernes(List<String> listeUidElevesConcernes) {
		this.listeUidElevesConcernes = listeUidElevesConcernes;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getListeIdSiecleElevesConcernes()
	 */
	public List<String> getListeIdSiecleElevesConcernes() {
		return listeIdSiecleElevesConcernes;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setListeIdSiecleElevesConcernes(java.util.List)
	 */
	public void setListeIdSiecleElevesConcernes(
			List<String> listeIdSiecleElevesConcernes) {
		this.listeIdSiecleElevesConcernes = listeIdSiecleElevesConcernes;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getIdSiecle()
	 */
	public String getIdSiecle() {
		return idSiecle;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setIdSiecle(java.lang.String)
	 */
	public void setIdSiecle(String idSiecle) {
		this.idSiecle = idSiecle;
	}
	

	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#addIdSiecleElevesConcernes(java.lang.String)
	 */
	public void addIdSiecleElevesConcernes(String id) {
		boolean existe=false;
		for(String s:this.getListeIdSiecleElevesConcernes()){
			if (s.equalsIgnoreCase(id)){
				existe=true;
			}
		}
		if(!existe){
			Person eleve = this.findEleveByIdSiecle(id);
			if(eleve!=null){
				this.listeIdSiecleElevesConcernes.add(id);
				this.getListeUidElevesConcernes().add(eleve.getUid());
			}else{
				logger.error("Erreur : ajout de l'id siecle "+id+" à la personne "+this.getCn()+" - Cet id n'est pas valide");
			}
		} else {
			logger.info("Erreur : ajout de l'id siecle "+id+" à la personne "+this.getCn()+" - Cet id est déjà lié à cette personne");
		}
	}
	/**
	 * Suppression d'un identifiant siecle lié à cette personne
	 * ie suppression d'un enfant lié à cette personne
	 * La suppression se fait dans la liste des id siecles et dans la liste des id ldap
	 * @param id
	 */
	protected void removeIdSiecleElevesConcernes(String id) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeIdSiecleElevesConcernes()) {
			if (!s.equalsIgnoreCase(id)) {
				liste.add(s);
			}
		}
		if(this.getListeIdSiecleElevesConcernes().size()==liste.size()){
			logger.info("Erreur : suppression de l'id siecle "+id+" lié à la personne "+this.getCn()+" - Cet id n'était pas associé à cette personne");
		}else{
			Person eleve = this.findEleveByIdSiecle(id);
			if(eleve!=null){
				this.setListeIdSiecleElevesConcernes(liste);
				this.getListeUidElevesConcernes().remove(eleve.getUid());
			}else{
				logger.info("Erreur : suppression de l'id siecle "+id+" à la personne "+this.getCn()+" - Cet id est déjà lié à cette personne");
			}
		}
		
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#isUserSurcharged()
	 */
	public boolean isUserSurcharged() {
		if (this.getListePasswords().size()>1) {
			return true;
		} else {
			return false;
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setPersonDao(fr.toutatice.outils.ldap.dao.PersonDao)
	 */
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getListeRoles()
	 */
	public List<String> getListeRoles() {
		//TODO : rajouter une recherche sur les filters ?
		RoleApplicatif roleApplicatif = (RoleApplicatif) context.getBean("roleApplicatif");
		return roleApplicatif.findListDnRole(this);	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#hasRole(java.lang.String)
	 */
	public boolean hasRole(String cnRole) {
		return personDao.personHasRole(this, cnRole);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#hasStructure(java.lang.String)
	 */
	public boolean hasStructure(String rne){
		return personDao.personHasStructure(this,rne);
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getTitle()
	 */
	public String getTitle() {
		return title;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getEmail()
	 */
	public String getEmail() {
		return email;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setEmail(java.lang.String)
	 */
	public void setEmail(String email) {
		this.email = email;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getDivcod()
	 */
	public String getDivcod() {
		return divcod;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setDivcod(java.lang.String)
	 */
	public void setDivcod(String divcod) {
		this.divcod = divcod;	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getListeImplicitManagers()
	 */
	public List<String> getListeImplicitManagers() {
		return listeImplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setListeImplicitManagers(java.util.List)
	 */
	public void setListeImplicitManagers(List<String> listeManagers) {
		this.listeImplicitManagers = listeManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#addImplicitManager(java.lang.String)
	 */
	public void addImplicitManager(String dnManager) {
		boolean existe=false;
		for(String s:this.getListeImplicitManagers()){
			if (s.equalsIgnoreCase(dnManager)){
				existe=true;
			}
		}
		if(!existe){
			this.listeImplicitManagers.add(dnManager);
		} else {
			logger.info("Erreur : ajout du manager "+dnManager+" à la personne "+this.getCn()+" - Ce dn existe déjà dans la liste des managers");
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#removeImplicitManager(java.lang.String)
	 */
	public void removeImplicitManager(String dnManager) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeImplicitManagers()) {
			if (!s.equalsIgnoreCase(dnManager)) {
				liste.add(s);
			}
		}
		if(this.getListeImplicitManagers().size()==liste.size()){
			logger.info("Erreur : suppression du manager "+dnManager+" de la personne "+this.getCn()+" - Ce dn n'existe dans la liste des managers");
		}
		this.setListeImplicitManagers(liste);
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#getListeExplicitManagers()
	 */
	public List<String> getListeExplicitManagers() {
		return listeExplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setListeExplicitManagers(java.util.List)
	 */
	public void setListeExplicitManagers(List<String> listeExplicitManagers) {
		this.listeExplicitManagers = listeExplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#addExplicitManager(java.lang.String)
	 */
	public void addExplicitManager(String dnManager){
		boolean existe=false;
		for(String s:this.getListeImplicitManagers()){
			if (s.equalsIgnoreCase(dnManager)){
				existe=true;
			}
		}
		for(String s:this.getListeExplicitManagers()){
			if (s.equalsIgnoreCase(dnManager)){
				existe=true;
			}
		}
		if(!existe){
			this.listeExplicitManagers.add(dnManager);
		} else {
			logger.info("Erreur : ajout du manager explicite"+dnManager+" à la personne "+this.getUid()+" - Ce dn existe déjà dans la liste des managers");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#removeExplicitManager(java.lang.String)
	 */
	public void removeExplicitManager(String dnManager) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeExplicitManagers()) {
			if (!s.equalsIgnoreCase(dnManager)) {
				liste.add(s);
			}
		}
		if(this.getListeExplicitManagers().size()==liste.size()){
			logger.info("Erreur : suppression du manager explicite"+dnManager+" de la personne "+this.getUid()+" - Ce dn n'existe dans la liste des managers explicites");
		}
		this.setListeExplicitManagers(liste);
	}
	
	/**
	 * Indique si la personne dont le dn est passé en paramètre est manager de la personne sur laquelle la méthode s'applique
	 * La recherche porte sur les managers explicites et implicites
	 * Pour chaque type de manager on vérifie :
	 * 		- si l'identifiant de la personne est renseigné comme manager
	 * 		- si un des profils de la personne est renseigné comme manager
	 * @param dnMember DN de la personne testée (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 * @return true si la personne est manager du profil, false sinon
	 */
	public boolean isManagedBy(Person p){
		boolean existe = false;
		for(String s : this.getListeImplicitManagers()){
			if(s.equalsIgnoreCase(p.getDn())){
				existe=true;
			}
		}
		for(String s : this.getListeExplicitManagers()){
			if(s.equalsIgnoreCase(p.getDn())){
				existe=true;
			}
		}
		if (!existe) {
			for(String profil : p.getListeProfils()) {
				for(String s : this.getListeImplicitManagers()){
					if(s.equalsIgnoreCase(profil)){
						existe=true;
					}
				}
				for(String s : this.getListeExplicitManagers()){
					if(s.equalsIgnoreCase(profil)){
						existe=true;
					}
				}
			}
		}
		return existe;
	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findFullDn(java.lang.String)
	 */
	public String findFullDn(String uid) {
		return personDao.buildFullDn(uid);
	}
	
	/**
	 * Recherche d'une personne par son id exact
	 * @param uid identifiant de la personne
	 * @return personne recherchée (null si la personne n'a pas été trouvée)
	 */
	public Person findUtilisateur(String uid)
	{
		Person p;
		p = personDao.findByPrimaryKey(uid);
		return p;
	}

	/**
	 * Recherche de personnes dans l'annuaire à partir du début de leur identifiant
	 * @param uid début de l'identifiant de la ou des personne(s) recherchée(s)
	 * @return liste des personnes correspondant au critère
	 */

	public List<Person> findUtilisateursUID(String uid) {
		return personDao.getPersonByUid(uid);
	}
	
	/**
	 * Recherche d'une personne par son DN exact
	 * @param dn (sous la forme "uid=xxxxxx,ou=personnes,dc=ent-bretagne,dc=fr")
	 * @return personne recherchée (null si la personne n'a pas été trouvée)
	 */
	public Person findPersonByDn(String dn) {
		Person p = null;
		try {
			p = personDao.findByDn(dn);
		} catch (ToutaticeAnnuaireException e) {
			logger.info("personne non trouvé dans l'annuaire - recherche par dn : "+dn);
			p = null;
		}
		return p;
	}
	
	/**
	 * Recherche de personnes dans l'annuaire à partir du début de leur nom 
	 * @param nom début du nom de la ou des personne(s) recherchée(s)
	 * @return liste des personnes correspondant au critère
	 */
	public List<Person> findUtilisateursNom(String nom) {
		return personDao.getPersonByName(nom);
	}

	/**
	 * Recherche de personnes dans l'annuaire en fonction du début du numéro de leur établissement
	 * Attention : cette requête peut ramener un grand nombre de résultats si mal utilisée
	 * @param rne début du numéro de l'établissement
	 * @return liste des personnes correspondant au critère
	 */
	public List<Person> findUtilisateursRNE(String rne) {
		return personDao.getPersonByRne(rne);
	}
	
	/**
	 * Recherche de personnes dans l'annuaire en fonction du début de leur adresse email
	 * @param email début de l'adresse email
	 * @return liste des personnes correspondant au critère
	 */
	public List<Person> findUtilisateursMail(String email) {
		return personDao.getPersonByMail(email);
	}
	
	/**
	 * Recherche de personnes dans l'annuaire en fonction du début de leur titre
	 * @param title début du titre 
	 * @return liste des personnes correspondant au critère
	 */
	public List<Person> findUtilisateursTitle(String title) {
		return personDao.getPersonByTitle(title);
	}
	
	/**
	 * Recherche de personnes en fonction d'un profil qui leur est affecté
	 * @param dnProfil DN du profil en question
	 * @return liste des personnes possédant le profil passé en paramètre
	 */
	public List<Person> findListePersonnesByDnProfil(String dnProfil) {
		return personDao.findListePersonnesByDnProfil(dnProfil);
	}
	
	/**
	 * Recherche de personnes en fonction d'un profil qui leur est affecté
	 * La liste ramené sera trié en fonction d'un critère passé en paramètre
	 * @param dnProfil dnProfil DN du profil 
	 * @param critereTri critère de tri pour la liste de résultat (cn pour un tri sur le nom/prénom par exemple)
	 * @return liste des personnes possédant le profil passé en paramètre trié suivant le critère choisi
	 */
	public List<Person> findListePersonnesAvecProfil(String dnProfil, String critereTri) {
		return personDao.getListePersonnesAyantProfilTrie(dnProfil, critereTri); 
	}
	
	/**
	 * Recherche multicritères de personnes dans l'annuaire
	 * @deprecated remplacée par la méthode findPersonneMultiCriteres
	 * @param nom chaine représentant le début du nom ou de l'identifiant de la (ou des) personne(s) recherchée(s)
	 * @param rne rne de la (ou des) personne(s) recherchée(s) 
	 * @return liste des personnes correspondant aux critères
	 */
	@Deprecated
	public List<Person> getPersonByNomIdRne(String nom, String rne)	{
		return personDao.getPersonByNomIdRne(nom, rne);
	}
	
	/**
	 * Recherche de personnes par critères
	 * @param nomId chaine représentant le début du nom ou de l'identifiant de la (ou des) personne(s) recherchée(s)
	 * @param rne rne de la (ou des) personne(s) recherchée(s) 
	 * @param dnProfilList liste de profils affectés aux personnes recherchées
	 * @param filtre filtre LDAP supplémentaire pour la recherche (syntaxe LDAP !)
	 * @param critereTri critère pour trier les résultats (cn, displayName,...)
	 * @return
	 */
	public List<Person> findPersonneMultiCriteres(String nomId, String rne, List<String> dnProfilList, String filtre, String critereTri){
		return personDao.findPersonneMultiCriteres(nomId,rne,dnProfilList,filtre,critereTri);
	}
	
	/**
	 * Recherche multicritères de personnes dans l'annuaire
	 * @param nom début du nom ou de l'identifiant de la (ou des) personne(s) recherchée(s)
	 * @param rne début du numéro de l'établissement de la (ou des) personne(s) recherchée(s)
	 * @param email début de l'adresse email de la (ou des) personne(s) recherchée(s)
	 * @param title début du titre de la (ou des) personne(s) recherchée(s)
	 * * @param critereTri critère suivant lequel trier la liste (nom de l'attribut dans LDAP)
	 * @return liste des personnes correspondant aux critères
	 */
	public List<Person> getPersonByCriteres(String nom, String rne, String email, String title, String critereTri)	{
		return personDao.getPersonByCriteres(nom, rne, email, title,critereTri);
	}
	
	/**
	 * Recherche multicritères de personnes dans l'annuaire
	 * @param nom début du nom ou de l'identifiant de la (ou des) personne(s) recherchée(s)
	 * @param rne début du numéro de l'établissement de la (ou des) personne(s) recherchée(s)
	 * @param email début de l'adresse email de la (ou des) personne(s) recherchée(s)
	 * @param title début du titre de la (ou des) personne(s) recherchée(s)
	 * @param divcod filtre sur le service de rattachement
	 * * @param critereTri critère suivant lequel trier la liste (nom de l'attribut dans LDAP)
	 * 
	 * @return liste des personnes correspondant aux critères
	 */
	public List<Person> getPersonByCriteres(String nom, String rne, String email, String title, String divcod, String critereTri)	{
		return personDao.getPersonByCriteres(nom, rne, email, title,divcod,critereTri);
	}

	/**
	 * Recherche des utilisateurs ayant un mot de passe surchargé 
	 * Attention : La recherche se base sur la présence de l'attribut EntPersonSmdp 
	 * Si cet attribut est absent l'utilisateur ne sera pas retourné même s'il a plusieurs mots de passe
	 *
	 * @return liste des personnes ayant un mot de passe surchargé
	 */

	public List<Person> rechercherSurcharge() {
		List<Person> l = new ArrayList<Person>();
		l = personDao.getSurcharge("");
		return l;
	}
	
	/**
	 * Recherche triée des utilisateurs ayant un mot de passe surchargé 
	 * Attention : La recherche se base sur la présence de l'attribut EntPersonSmdp 
	 * Si cet attribut est absent l'utilisateur ne sera pas retourné même s'il a plusieurs mots de passe
	 * @param critereTriResultat : permet de trier la liste en fonction de l'identifiant des utilisateurs (uid), leur nom de famille (cn),...
	 * @return
	 */
	public List<Person> rechercherSurcharge(String critereTriResultat) {
		List<Person> l = new ArrayList<Person>();
		l = personDao.getSurcharge(critereTriResultat);
		return l;
	}
	
	/**
	 * Recherche des utilisateurs ayant un mot de passe surchargé par une personne donnée
	 * @param uid identifiant de la personne ayant surchargé le mot de passe
	 * @return liste des personnes ayant un mot de passe surchargé par cette personne
	 */

	public List<Person> rechercherSurchargeParUtilisateur(String uid) {
		List<Person> l = new ArrayList<Person>();
		l = personDao.getSurchargeParUtilisateur(uid,"");
		return l;
	}
	
	/**
	 * Recherche des utilisateurs ayant un mot de passe surchargé par une personne donnée
	 * @param uid identifiant de la personne ayant efefctué des surcharges
	 * @param critereTriResultat : permet de trier la liste en fonction de l'identifiant des utilisateurs (uid), leur nom de famille (cn),...
	 * @return
	 */
	public List<Person> rechercherSurchargeParUtilisateur(String uid, String critereTriResultat) {
		List<Person> l = new ArrayList<Person>();
		l = personDao.getSurchargeParUtilisateur(uid,critereTriResultat);
		return l;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#isMemberExplicit(java.lang.String)
	 */
	public boolean isMemberExplicit(String cnProfil) {
		boolean retour = false;
		Profil p = profilDao.findByPrimaryKey(cnProfil);
		for(String s:p.getListeExplicitMembers()) {
			if(s.toLowerCase().equals(this.getDn().toLowerCase())) {
				retour = true;
			}
		}
		return retour;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#create()
	 */
	public void create() throws ToutaticeAnnuaireException
	{
		personDao.create(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#delete()
	 */
	public void delete() throws ToutaticeAnnuaireException, NamingException
	{
		for(String dnProfil:this.getListeProfils()){
			Profil prof = profilDao.findByDn(dnProfil);
			if(prof!=null){
				prof.removeMember(this.getDn());
				prof.updateProfil();
			}
		}
		personDao.delete(this);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#update()
	 */
	
	public void update() throws ToutaticeAnnuaireException, NamingException {
		try {
			personDao.update(this);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#updateManagersImplicites()
	 */
	public void updateManagersImplicites() throws ToutaticeAnnuaireException{
		personDao.updateImplicitManagers(this);
	}
	
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#updateEmail()
	 */
	public void updateEmail() throws ToutaticeAnnuaireException {
			try {
				personDao.updateEmail(this);
			} catch (ToutaticeAnnuaireException e) {
				throw e;
			}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#updatePassword(java.lang.String)
	 */

	public void updatePassword(String mdp) throws ToutaticeAnnuaireException {
	
		String mdpEncode = fonctionBase64SSHA(mdp);
		String idSurcharge=null;
		String typeSurcharge=null;
		String motifSurcharge=null;
		String mdpSurcharge=null;
		boolean userSurcharge = false;
		
		if (this.getListePasswords().size()>1)
		{
			//sauvegarde de la surcharge
			userSurcharge = true;
			idSurcharge = this.getIdSurcharge();
			typeSurcharge=this.getTypeSurcharge();
			motifSurcharge=this.getMotifSurcharge();
			mdpSurcharge = this.getListePasswords().get(1);
			if (idSurcharge == null) {
				idSurcharge ="";
			}
			
			this.deleteSurcharge();
		}
		
		ArrayList<String> newListePassword = new ArrayList<String>();
		newListePassword.add(mdpEncode);
		this.setListePasswords(newListePassword);


		try{
		personDao.updatePassword(this);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
		
		if (userSurcharge) {
			//restauration de la surcharge
				this.surchargeMdpEncode(mdpSurcharge, idSurcharge, typeSurcharge, motifSurcharge,"");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#verifMdp(java.lang.String)
	 */
	public boolean verifMdp(String mdp) {
		return personDao.authenticate(this.getUid(),mdp);
	}

	


	
	/**
	 * @deprecated
	 * Codage d'un mot de passe en base 64 SHA
	 * @param motDePasse mot de passe à encoder
	 * @return mot de passe encodé en base 64 SHA
	 */
	@Deprecated
	public static String fonctionBase64Sha1(String motDePasse) {
		String s="";
		try {
		MessageDigest md = MessageDigest.getInstance("SHA"); 
		byte[] b = md.digest(motDePasse.getBytes());
		Base64 base64 = new Base64();
		byte[] b1 = base64.encode(b);
		s = new String(b1);
		s = "{SHA}" + s;
		} catch (NoSuchAlgorithmException e) 
		{ logger.error("problème d'encodage du mot de passe"); } 
		return s;
	}
	
	/**
	 * Codage d'un mot de passe en base 64 SSHA
	 * @param motDePasse mot de passe à encoder
	 * @return mot de passe encodé en base 64 SSHA
	 */
	public static String fonctionBase64SSHA(String motDePasse){
		byte[] salt;
		String mdpSSHA="";
		salt = SecureRandom.getSeed(20);
		LdapShaPasswordEncoder encoder = new LdapShaPasswordEncoder();
		mdpSSHA = encoder.encodePassword(motDePasse, salt);
		return mdpSSHA;
	}
	
	

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#surchargeMdp(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	/*public void surchargeMdp(String mdpSurcharge, String login) throws ToutaticeAnnuaireException {
		if (this.isUserSurcharged()) {
			loggerSurcharge.info("L'utilisateur "+login+" écrase la surcharge de "+this.getIdSurcharge()+" sur la personne "+this.getUid());
			this.deleteSurcharge(login);
		}
		String mdpEncode = fonctionBase64SSHA(mdpSurcharge);
		this.surchargeMdpEncode(mdpEncode, login);

	}*/
	
	public void surchargeMdp(String mdpSurcharge, String idSurcharge, String typeSurcharge, String motifSurcharge, String adresseIP) throws ToutaticeAnnuaireException {
		if (this.isUserSurcharged()) {
			this.deleteSurcharge();
		}
		String mdpEncode = fonctionBase64SSHA(mdpSurcharge);
		this.surchargeMdpEncode(mdpEncode, idSurcharge, typeSurcharge, motifSurcharge, adresseIP);

	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#surchargeMdpEncode(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
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
	
	public void surchargeMdpEncode(String mdpEncode, String idSurcharge, String typeSurcharge, String motifSurcharge, String adresseIP) throws ToutaticeAnnuaireException {
		List<String> password = new ArrayList<String>();
		password = this.getListePasswords();
		String pwduser = password.get(0);
		password.clear();
		password.add(pwduser);
		password.add(mdpEncode);
		this.setListePasswords(password);
		this.setIdSurcharge(idSurcharge);
		this.setTypeSurcharge(typeSurcharge);
		this.setMotifSurcharge(motifSurcharge);
		try {
			personDao.addPersonSmdp(this);
			personDao.updatePassword(this);
			loggerSurcharge.info("l'utilisateur "+this.getUid()+" a été surchargé par "+idSurcharge+" (IP : "+adresseIP+"). Type de Surcharge : "+typeSurcharge+" - Motif : "+motifSurcharge);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#deleteSurcharge()
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
	
	public void deleteSurcharge() throws ToutaticeAnnuaireException {
		List<String> password = this.getListePasswords();
		String pwduser = password.get(0);
		password.clear();
		password.add(pwduser);
		this.setListePasswords(password);
		personDao.updatePassword(this);
		personDao.deletePersonSmdp(this);
		loggerSurcharge.info("La surcharge sur l'utilisateur "+this.getUid()+" a été supprimée (Surcharge de type "+this.getTypeSurcharge()+" , motif : "+this.getMotifSurcharge()+", faite par : "+this.getIdSurcharge());
		this.setIdSurcharge("");
		this.setTypeSurcharge("");
		this.setMotifSurcharge("");
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findMdpInitialEleve()
	 */
	public String findMdpInitialEleve() {
		String mdp = "";
		if (this.getTitle().toLowerCase().equals("ele")) {
			mdp = this.getDateNaissance().replaceAll("/", "");
		}
		return mdp;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#razMdp()
	 */
	public void razMdp() throws ToutaticeAnnuaireException {
		if (this.getTitle().toLowerCase().equals("ele")) {
			this.updatePassword(this.findMdpInitialEleve());
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#eleveHasChangedPassword()
	 */
	public boolean eleveHasChangedPassword() {
		boolean mdpChange = true;
		
		if (this.getTitle().toLowerCase().equals("ele")){
			if(this.getSourceSI().equalsIgnoreCase("EM")){
				String dateNaissance = this.getDateNaissance().substring(0, 2)+"/"+this.getDateNaissance().substring(2, 4)+"/"+this.getDateNaissance().substring(4, 8);
				mdpChange = ! this.verifMdp(dateNaissance);
			}
			else{
				mdpChange = ! this.verifMdp(this.getDateNaissance().replaceAll("/", ""));
			}
		}
		return mdpChange;
	}	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findApplicationsAutorisees()
	 */
	public List<Application> findApplicationsAutorisees(){
	//	ApplicationDao applicationDao = (ApplicationDao) context.getBean("applicationDao",ApplicationDao.class);
		return applicationDao.findAllApplisAutorises(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findApplicationsGerees()
	 */
	public List<Application> findApplicationsGerees() {
		//ApplicationDao applicationDao = (ApplicationDao) context.getBean("applicationDao",ApplicationDao.class);
		List<Application> appliImplicite = applicationDao.findListeApplisGereesImplicitementUser(this);
		List<Application> appliExplicite = applicationDao.findListeApplisGereesExplicitementUser(this);
		appliImplicite.addAll(appliExplicite);
		return appliImplicite;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findProfilsAssocies()
	 */
	public List<Profil> findProfilsAssocies(){
		return profilDao.findProfilsPerson(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findProfilsAssociesByOrga(java.lang.String)
	 */
	public List<Profil> findProfilsAssociesByOrga(String idOrga){
		return profilDao.findProfilsPersonByOrga(this, idOrga);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findProfilsGeres()
	 */
	public List<Profil> findProfilsGeres(){
		return profilDao.findProfilsGeresPerson(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findProfilsGeresParOrga(java.lang.String)
	 */
	public List<Profil> findProfilsGeresParOrga(String idOrga){
		return profilDao.findProfilsGeresPersonParOrga(this,idOrga);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findProfilsGeresImplicitement()
	 */
	public List<Profil> findProfilsGeresImplicitement(){
		return profilDao.findProfilsGeresImplicitementPerson(this);
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findProfilsGeresExplicitement()
	 */
	public List<Profil> findProfilsGeresExplicitement(){
		return profilDao.findProfilsGeresExplicitementPerson(this);
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findProfilsGeresImplicitementParOrga(java.lang.String)
	 */
	public List<Profil> findProfilsGeresImplicitementParOrga(String idOrga){
		return profilDao.findProfilsGeresImplicitementPersonParOrga(this, idOrga);
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findProfilsGeresExplicitementParOrga(java.lang.String)
	 */
	public List<Profil> findProfilsGeresExplicitementParOrga(String idOrga){
		return profilDao.findProfilsGeresExplicitementPersonParOrga(this, idOrga);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#isParent()
	 */
	public boolean isParent(){
		if(this.getUid().toLowerCase().endsWith("@aten")){
			return true;
		}else{
			return false;
		}		
	}
	
	public List<Person> findParents(){
		return personDao.findParents(this.getIdSiecle());
	}
	
	/**
	 * Recherche d'un élève via son identifiant SIECLE
	 * @param idSiecle
	 * @return élève correspondant
	 */
	public Person findEleveByIdSiecle(String idSiecle){
		return personDao.findEleveByIdSiecle(idSiecle);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#findInfosCsvPerson(java.lang.String, java.lang.String)
	 */
	public String findInfosCsvPerson(String dn, String format) throws ToutaticeAnnuaireException
	{
		String enregCsv = "";
				Person p;
		try {
			p = personDao.findByDn(dn);
		} catch (ToutaticeAnnuaireException e1) {
			throw e1;
		}
		if (p.getUid() != null)
		{
		
		String[] tableElt = format.split(";");
		int i = tableElt.length;
		
		if (! (p==null))
		{
			for (int j = 0; j<i; j++)
			{
				String attr = tableElt[j];
				try {
					Field f = p.getClass().getDeclaredField(attr);
					String s;
					s = f.get(p).toString();
					enregCsv = enregCsv.concat(s+";");
				
				} catch (NoSuchFieldException e) {
						if (attr.contains("rne"))
						{
							enregCsv = enregCsv.concat(p.getListeRnes().get(0)+";");
						}
						else 
						{
							logger.error("Problème de génération du fichier csv : le format n'est pas compatible avec les données de l'annuaire");
						}
				} catch(IllegalAccessException e)
				{
					logger.error("Problème de génération du fichier csv : le format n'est pas compatible avec les données de l'annuaire");
				}

			}
		}
		}
		return enregCsv;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#populateMap(java.util.Map)
	 */
	public void populateMap(Map<String, Object> datas){
		if(this.getUid()!=null){
			datas.put("LDAPUSER.uid", this.getUid());
		}
		if(this.getCn()!=null){
			datas.put("LDAPUSER.cn", this.getCn());
		}
		if(this.getSn()!=null){
			datas.put("LDAPUSER.sn", this.getSn());
		}
		if(this.getDisplayName()!=null){
			datas.put("LDAPUSER.displayName", this.getDisplayName());
		}
		if(this.getGivenName()!=null){
			datas.put("LDAPUSER.givenName", this.getGivenName());
		}
		if(this.getAlias()!=null){
			datas.put("LDAPUSER.alias", this.getAlias());
		}
		if(this.getSexe()!=null){
			datas.put("LDAPUSER.sexe", this.getSexe());
		}
		if(this.getDateNaissance()!=null){
			datas.put("LDAPUSER.dateNaissance", this.getDateNaissance());
		}
		if(this.getDivcod()!=null){
			datas.put("LDAPUSER.divcod", this.getDivcod());
		}
		if(this.getListeRnes()!=null){
			datas.put("LDAPUSER.listeRnes", this.getListeRnes());
			if(!this.getListeRnes().isEmpty()){
				String idCdc = (String) ChargementCdcIdEtab.idEtabCdc.get(this.getListeRnes().get(0));
				if(idCdc!=null){
					String[] tab = idCdc.split("\\.");
					if(tab.length>1){
						datas.put("LDAPUSER.cdcIdCollectivite",tab[0]);
						datas.put("LDAPUSER.cdcIdEtab",tab[1]);
					}
				}else{
					datas.put("LDAPUSER.cdcIdCollectivite","");
					datas.put("LDAPUSER.cdcIdEtab","");
				}
			}else{
				datas.put("LDAPUSER.cdcIdEtab","");
			}
		}
		if(this.getListeProfils()!=null){
			datas.put("LDAPUSER.listeProfils", this.getListeProfils());
		}
		if(this.getListePasswords()!=null){
			datas.put("LDAPUSER.listePasswords", this.getListePasswords());
		}
		if(this.getListeImplicitManagers()!=null){
			datas.put("LDAPUSER.listeManagersImplicites", this.getListeImplicitManagers());
		}
		if(this.getListeExplicitManagers()!=null){
			datas.put("LDAPUSER.listeManagersExplicites", this.getListeExplicitManagers());
		}
		if(this.getIdSurcharge()!=null){
			datas.put("LDAPUSER.idSurcharge", this.getIdSurcharge());
		}
		if(this.getTypeSurcharge()!=null){
			datas.put("LDAPUSER.typeSurcharge", this.getTypeSurcharge());
		}
		if(this.getMotifSurcharge()!=null){
			datas.put("LDAPUSER.motifSurcharge", this.getMotifSurcharge());
		}
		if(this.getSourceSI()!=null){
			datas.put("LDAPUSER.sourceSI", this.getSourceSI());
		}
		if(this.getTitle()!=null){
			datas.put("LDAPUSER.title", this.getTitle());
		}
		if(this.getEmail()!=null){
			datas.put("LDAPUSER.email", this.getEmail());
		}
		if(this.getIdSiecle()!=null){
			datas.put("LDAPUSER.idSiecle", this.getIdSiecle());
		}
		if(this.getListeIdSiecleElevesConcernes()!=null){
			datas.put("LDAPUSER.idSiecleElevesConcernes", this.getListeIdSiecleElevesConcernes());
		}
		if(this.getListeUidElevesConcernes()!=null){
			datas.put("LDAPUSER.listeIdElevesConcernes", this.getListeUidElevesConcernes());
		}
		if(this.getPersonJointure()!=null){
			datas.put("LDAPUSER.personJointure", this.getPersonJointure());
		}
		if(this.getNationalProfil()!=null){
			datas.put("LDAPUSER.nationalProfil", this.getNationalProfil());
		}
	}
	
	/**
	 * Méthode utilisée dans le portail, par les portlets qui souhaitent récupérer les infos de l'utilisateur connecté
	 * A partir de la map stockée en session un objet "Person" est reconstruit, ce qui évite de faire un accès au LDAP pour
	 * chaque portlet
	 * @param datas
	 * @return
	 */
	public static Person instanciateFromMap(Map<String, Object> datas) throws ToutaticeAnnuaireException {
		Person p = (Person) context.getBean("person");
	
		if(datas!=null){
			p.setUid((String)datas.get("LDAPUSER.uid"));
			p.setCn((String)datas.get("LDAPUSER.cn"));
			p.setSn((String)datas.get("LDAPUSER.sn"));
			p.setDisplayName((String)datas.get("LDAPUSER.displayName"));
			p.setGivenName((String)datas.get("LDAPUSER.givenName"));
			p.setAlias((String)datas.get("LDAPUSER.alias"));
			p.setSexe((String)datas.get("LDAPUSER.sexe"));
			p.setDateNaissance((String)datas.get("LDAPUSER.dateNaissance"));
			p.setDivcod((String)datas.get("LDAPUSER.divcod"));
			p.setListeRnes((List<String>)datas.get("LDAPUSER.listeRnes"));
			p.setListeProfils((List<String>)datas.get("LDAPUSER.listeProfils"));
			p.setListePasswords((List<String>)datas.get("LDAPUSER.listePasswords"));
			p.setListeImplicitManagers((List<String>)datas.get("LDAPUSER.listeManagersImplicites"));
			p.setListeExplicitManagers((List<String>)datas.get("LDAPUSER.listeManagersExplicites"));
			p.setIdSurcharge((String)datas.get("LDAPUSER.idSurcharge"));
			p.setTypeSurcharge((String)datas.get("LDAPUSER.typeSurcharge"));
			p.setMotifSurcharge((String)datas.get("LDAPUSER.motifSurcharge"));
			p.setSourceSI((String)datas.get("LDAPUSER.sourceSI"));
			p.setTitle((String)datas.get("LDAPUSER.title"));
			p.setEmail((String)datas.get("LDAPUSER.email"));
			p.setIdSiecle((String)datas.get("LDAPUSER.idSiecle"));
			p.setListeIdSiecleElevesConcernes((List<String>)datas.get("LDAPUSER.idSiecleElevesConcernes"));
			p.setListeUidElevesConcernes((List<String>)datas.get("LDAPUSER.listeIdElevesConcernes"));
			p.setPersonJointure((String)datas.get("LDAPUSER.personJointure"));
			p.setNationalProfil((String)datas.get("LDAPUSER.nationalProfil"));
			
			if(p.getUid()==null){
				p=null;
			}
			
		}else{
			p=null;
		}
			
		return p;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#equals(java.lang.Object)
	 */
	@Override public boolean equals(Object o) {
	   
	    if ( this == o ) return true;

	    if ( !(o instanceof Person) ) return false;
	   
	    Person p = (Person) o;

	   return p.getUid().equals(this.getUid());
	      
	  }
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Person#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.context = ctx;

	}
	

}
