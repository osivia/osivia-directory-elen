package fr.toutatice.outils.ldap.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.ApplicationDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Application issue de l'annuaire LDAP
 * @author aguihomat
 *
 */

@Service(value = "application")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ApplicationImpl implements ApplicationContextAware, Application {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	
	private static ApplicationContext context;  
  
	/**
	 * Factory, à instancier via l'IOC de SPRING
	 */
	@Autowired(required = false)
	@Qualifier("applicationDao")
	private ApplicationDao applicationDao;  

	
	// ATTRIBUTS OBLIGATOIRES
	
	/**
	 * Identifiant de l'appication (unique) (attribut obligatoire)
	 */
	private String id;  
	
	/**
	 * Nom de l'application (attribut obligatoire)
	 */
	private String nom;			
	/**
	 * Catégories auxquelles appartient l'application (attribut obligatoire)
	 */
	private List<String> categories = new ArrayList<String>(); 	
	/**
	 * Mots de passe de l'application (attribut obligatoire)
	 */	
	private List<byte[]> listePasswords = new ArrayList<byte[]>();
	// ATTRIBUTS OPTIONNELS
	
	/**
	 * Description de l'application (attribut optionnel)
	 */
	private String description;	
	
	private String typeApplication;
	/**
	 * URL de l'application (attribut optionnel)
	 */
	private String url;							
	/**
	 * URI de la base de données liée à l'application (attribut optionnel)
	 */
	private String dbUri;
	/**
	 * Catégorie de l'application pour les stats CDC XT
	 */
	private String cdcCatego;
	/**
	 * RNE de l'application pour les stats CDC XT
	 */
	private String cdcRne;
	/**
	 * Nom du propriétaire de l'application (attribut optionnel)
	 */
	//private String proprietaire;
	private List<String> listeOwners = new ArrayList<String>();
	
	private List<String> listeManagers = new ArrayList<String>();
	
	private List<String> listeExplicitManagers = new ArrayList<String>();
	/**
	 * Liste des profils autorisés à accéder à l'application (attribut optionnel)
	 */
	private List<String> listeProfils = new ArrayList<String>(); 			
	/**
	 * Liste des rôles applicatifs de l'application (attribut optionnel)
	 */
	private List<String> listeRolesApplicatifs = new ArrayList<String>();  
	/**
	 * Liste des organisations utilisant l'application
	 */
	private List<String> listeAppliOrganisations = new ArrayList<String>();  
	/**
	 * Dates des dernières mises à jour de l'application (attribut optionnel) - Non utilisé
	 */
	private List<String> derMaj = new ArrayList<String>();
	/**
	 * Mode d'authentification utilisé par l'application (attribut optionnel) - Non utilisé
	 */
	private List<String> modAuth = new ArrayList<String>();
	/**
	 * URL pour redirection lors de la déconnexion de l'application (attribut optionnel) - Non utilisé
	 */
	private List<String> urlLogout = new ArrayList<String>();
	/**
	 * Propriétés de l'application (attribut optionnel)
	 */
	private List<String> proprietes = new ArrayList<String>();
	
	/**
	 * Nature de l'application : doit elle s'ouvrir dans un nouvel onglet du navigateur, dans une portlet dynamique, dans une page dynamique du portail, dans une iframe ?
	 * Valeur possible : portlet, pageDynamique, ongletNavigateur, iframe
	 */
	private String nature;
	
	/**
	 * Contexte de l'application : indique dans quel contexte l'application doit être présentée : bureau, espace numérique,...
	 * Si rien n'est renseigné l'appli est présentée dans tous les contextes
	 */
	private String contexte;
	
	//------------------------------------------------------------------------------------------------------------------------
	// GETTERS ET SETTERS
	//------------------------------------------------------------------------------------------------------------------------
		
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setApplicationDao(fr.toutatice.outils.ldap.dao.ApplicationDao)
	 */
	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getId()
	 */
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getNom()
	 */
	public String getNom() {
		return nom;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setNom(java.lang.String)
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getCategories()
	 */
	public List<String> getCategories() {
		return categories;}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setCategories(java.util.List)
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addCategories(java.lang.String)
	 */
	public void addCategories(String s) {
		this.categories.add(s);	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getDescription()
	 */
	public String getDescription() {
		return description;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;	}
	
	
	public String getTypeApplication() {
		return typeApplication;
	}
	public void setTypeApplication(String typeApplication) {
		this.typeApplication = typeApplication;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getCdcCatego()
	 */
	public String getCdcCatego() {
		return cdcCatego;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setCdcCatego(java.lang.String)
	 */
	public void setCdcCatego(String cdcCatego) {
		this.cdcCatego = cdcCatego;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getCdcRne()
	 */
	public String getCdcRne() {
		return cdcRne;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setCdcRne(java.lang.String)
	 */
	public void setCdcRne(String cdcRne) {
		this.cdcRne = cdcRne;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getListeProfils()
	 */
	public List<String> getListeProfils() {
		return listeProfils;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setListeProfils(java.util.List)
	 */
	public void setListeProfils(List<String> listeProfils) {
		this.listeProfils = listeProfils;
	}
		
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getListeAppliOrganisations()
	 */
	public List<String> getListeAppliOrganisations() {
		return listeAppliOrganisations;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setListeAppliOrganisations(java.util.List)
	 */
	public void setListeAppliOrganisations(List<String> listeAppliOrganisations) {
		this.listeAppliOrganisations = listeAppliOrganisations;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addAppliOrganisation(java.lang.String)
	 */
	public void addAppliOrganisation(String s) {
		if(!this.listeAppliOrganisations.contains(s)){
			this.listeAppliOrganisations.add(s);
			}
		}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addProfil(java.lang.String)
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
			logger.error("Erreur : ajout du profil "+dnProfil+" à l'application "+this.getId()+" - Ce profil est déjà affecté à l'application");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#removeProfil(java.lang.String)
	 */
	public void removeProfil(String dnProfil) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeProfils()) {
			if (!s.equalsIgnoreCase(dnProfil)) {
				liste.add(s);
			}
		}
		if(this.getListeProfils().size()==liste.size()){
			logger.error("Erreur : suppression du profil "+dnProfil+" de l'application "+this.getId()+" - Ce profil n'est pas affecté à cette application");
		}
		this.setListeProfils(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getUrl()
	 */
	public String getUrl() {
		return url;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setUrl(java.lang.String)
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getListePasswords()
	 */
	public List<byte[]> getListePasswords() {
		return this.listePasswords;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setListePasswords(java.util.List)
	 */
	public void setListePasswords(List<byte[]> password) {
		this.listePasswords = password;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addPassword(byte[])
	 */
	public void addPassword(byte[] s) {
		this.listePasswords.add(s);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getListeRolesApplicatifs()
	 */
	public List<String> getListeRolesApplicatifs() {
		return listeRolesApplicatifs;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setListeRolesApplicatifs(java.util.List)
	 */
	public void setListeRolesApplicatifs(List<String> listeRolesApplicatifs) {
		this.listeRolesApplicatifs = listeRolesApplicatifs;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addRoleApplicatif(java.lang.String)
	 */
	public void addRoleApplicatif(String dnRole) {
		boolean existe=false;
		for(String s:this.getListeRolesApplicatifs()){
			if (s.equalsIgnoreCase(dnRole)){
				existe=true;
			}
		}
		if(!existe){
			this.listeRolesApplicatifs.add(dnRole);
		} else {
			logger.error("Erreur : ajout du role "+dnRole+" à l'application "+this.getId()+" - Ce role est déjà affecté à l'application");
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#removeRoleApplicatif(java.lang.String)
	 */
	public void removeRoleApplicatif(String dnRole) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeRolesApplicatifs()) {
			if (!s.equalsIgnoreCase(dnRole)) {
				liste.add(s);
			}
		}
		if(this.getListeRolesApplicatifs().size()==liste.size()){
			logger.error("Erreur : suppression du role "+dnRole+" de l'application "+this.getId()+" - Ce Role n'est pas affecté à cette application");
		}
		this.setListeRolesApplicatifs(liste);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getDbUri()
	 */
	public String getDbUri() {
		return dbUri;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setDbUri(java.lang.String)
	 */
	public void setDbUri(String dbUri) {
		this.dbUri = dbUri;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getListeOwners()
	 */
	public List<String> getListeOwners() {
		return listeOwners;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setListeOwners(java.util.List)
	 */
	public void setListeOwners(List<String> listeOwners) {
		this.listeOwners = listeOwners;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addOwner(java.lang.String)
	 */
	public void addOwner(String dnOwner){ 
		boolean existe=false;
		for(String s:this.getListeOwners()){
			if (s.equalsIgnoreCase(dnOwner)){
				existe=true;
			}
		}
		if(!existe){
			this.listeOwners.add(dnOwner);
		} else {
			logger.error("Erreur : ajout du owner "+dnOwner+" à l'application "+this.getId()+" - Ce dn existe déjà dans la liste des owners");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#removeOwner(java.lang.String)
	 */
	public void removeOwner(String dnOwner){ 
		ArrayList<String> liste= new ArrayList<String>();
		for(String s:this.getListeOwners()){
			if(!s.equalsIgnoreCase(dnOwner)){
				liste.add(s);
			}
		}
		if(this.getListeOwners().size()==liste.size()){
			logger.error("Erreur : suppression du owner "+dnOwner+" du profil "+this.getId()+" - Ce dn n'existe dans la liste des owners");
		}
		this.setListeOwners(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#isOwner(java.lang.String)
	 */
	public boolean isOwner(String dnOwner){
		boolean existe = false;
		for(String s : this.getListeOwners()){
			if(s.equalsIgnoreCase(dnOwner)){
				existe=true;
			}
		}
		return existe;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getListeManagers()
	 */
	public List<String> getListeManagers() {
		return listeManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setListeManagers(java.util.List)
	 */
	public void setListeManagers(List<String> listeManagers) {
		this.listeManagers = listeManagers;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addManager(java.lang.String)
	 */
	public void addManager(String dnManager) {
		boolean existe=false;
		for(String s:this.getListeManagers()){
			if (s.equalsIgnoreCase(dnManager)){
				existe=true;
			}
		}
		if(!existe){
			this.listeManagers.add(dnManager);
		} else {
			logger.error("Erreur : ajout du manager "+dnManager+" à l'application "+this.getId()+" - Ce dn existe déjà dans la liste des managers");
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#removeManager(java.lang.String)
	 */
	public void removeManager(String dnManager) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeManagers()) {
			if (!s.equalsIgnoreCase(dnManager)) {
				liste.add(s);
			}
		}
		if(this.getListeManagers().size()==liste.size()){
			logger.error("Erreur : suppression du manager "+dnManager+" de l'application "+this.getId()+" - Ce dn n'existe dans la liste des managers");
		}
		this.setListeManagers(liste);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getListeExplicitManagers()
	 */
	public List<String> getListeExplicitManagers() {
		return listeExplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setListeExplicitManagers(java.util.List)
	 */
	public void setListeExplicitManagers(List<String> listeExplicitManagers) {
		this.listeExplicitManagers = listeExplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addExplicitManager(java.lang.String)
	 */
	public void addExplicitManager(String dnManager){
		boolean existe=false;
		
		for(String s:this.getListeExplicitManagers()){
			if (s.equalsIgnoreCase(dnManager)){
				existe=true;
			}
		}
		if(!existe){
			this.listeExplicitManagers.add(dnManager);
		} else {
			logger.error("Erreur : ajout du manager explicite"+dnManager+" à l'application "+this.getId()+" - Ce dn existe déjà dans la liste des managers");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#removeExplicitManager(java.lang.String)
	 */
	public void removeExplicitManager(String dnManager) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeExplicitManagers()) {
			if (!s.equalsIgnoreCase(dnManager)) {
				liste.add(s);
			}
		}
		if(this.getListeExplicitManagers().size()==liste.size()){
			logger.error("Erreur : suppression du manager explicite"+dnManager+" de l'application "+this.getId()+" - Ce dn n'existe dans la liste des managers explicites");
		}
		this.setListeExplicitManagers(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#isManagedBy(fr.toutatice.outils.ldap.entity.Person)
	 */
	public boolean isManagedBy(Person p){
		boolean existe = false;
		for(String s : this.getListeManagers()){
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
				for(String s : this.getListeManagers()){
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
	 * @see fr.toutatice.outils.ldap.entity.Application#getDerMaj()
	 */
	public List<String> getDerMaj() {
		return derMaj;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setDerMaj(java.util.List)
	 */
	public void setDerMaj(List<String> derMaj) {
		this.derMaj = derMaj;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addDerMaj(java.lang.String)
	 */
	public void addDerMaj(String derMaj) {
		this.derMaj.add(derMaj);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getModAuth()
	 */
	public List<String> getModAuth() {
		return modAuth;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setModAuth(java.util.List)
	 */
	public void setModAuth(List<String> modAuth) {
		this.modAuth = modAuth;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addModAuth(java.lang.String)
	 */
	public void addModAuth(String modAuth) {
		this.modAuth.add(modAuth);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getUrlLogout()
	 */
	public List<String> getUrlLogout() {
		return urlLogout;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setUrlLogout(java.util.List)
	 */
	public void setUrlLogout(List<String> urlLogout) {
		this.urlLogout = urlLogout;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addUrlLogout(java.lang.String)
	 */
	public void addUrlLogout(String url) {
		this.urlLogout.add(url);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getProprietes()
	 */
	public List<String> getProprietes() {
		return proprietes;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setProprietes(java.util.List)
	 */
	public void setProprietes(List<String> proprietes) {
		this.proprietes = proprietes;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#addProprietes(java.lang.String)
	 */
	public void addProprietes(String prop) {
		this.proprietes.add(prop);
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getNature()
	 */
	public String getNature() {
		return nature;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setNature(java.lang.String)
	 */
	public void setNature(String nature) {
		this.nature = nature;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getContexte()
	 */
	public String getContexte() {
		return contexte;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setContexte(java.lang.String)
	 */
	public void setContexte(String contexte) {
		this.contexte = contexte;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getContext()
	 */
	public ApplicationContext getContext() {
		return context;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#getDn()
	 */
	public String getDn(){
		return applicationDao.findFullDn(this.getId());
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#findFullDn(java.lang.String)
	 */
	public String findFullDn(String cn) {
		return applicationDao.findFullDn(cn);
	}
	
	/**
	 * Recherche une application grâce à son identifiant exact
	 * @param identifiant de l'application
	 * @return application recherchée
	 * @throws ToutaticeAnnuaireException 
	 */
	public Application findApplication(String id) throws ToutaticeAnnuaireException {
		logger.debug("entree dans la methode Application.findApplication(id)");
		Application app = null;
		try {
			app = applicationDao.findByPrimaryKey(id);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
		if (app==null){
			throw new ToutaticeAnnuaireException("application "+id+" non trouvée dans l'annuaire");
		}
		return app;
	}
	
	/**
	 * Recherche toutes les applications de la base
	 * @return liste des applications existantes
	 */
	public List<Application> findAllApplis() {
		logger.debug("entree dans la methode Application.findAllApplis");
		return applicationDao.findAllApplis();
	}

	/**
	 * Recherche toutes les applications disponibles pour un utilisateur donné grâce à son profil
	 * @param utilisateur concerné
	 * @return liste des applications disponibles
	 */
	public List<Application> findApplisUser(Person user) {
		// Récupération de la liste des applications auxquelles a accès le user
		List<Application> liste = applicationDao.findApplisAutorisesParProfilUid(user);	
		return liste;
	}
	
	public List<Application> findApplisUser(Person user, String contexte) {
		// Récupération de la liste des applications auxquelles a accès le user
		List<Application> liste = applicationDao.findApplisAutorisesParProfilUid(user,contexte);	
		return liste;
	}
	
	/**
	 * Recherche toutes les applications disponibles pour un utilisateur donné grâce à ses profils et à ses roles
	 * @param utilisateur concerné
	 * @return liste des applications disponibles
	 */
	public List<Application> findApplisDispo(Person user) {
		logger.debug("entree dans la methode Application.findApplisDispo(user)");
		// Récupération de la liste des applications auxquelles a accès le user
		List<Application> liste = applicationDao.findAllApplisAutorises(user);	
		return liste;
	}
	
	public List<Application> findApplisCatego(Person user, String categorie) {
		logger.debug("entree dans la methode Application.findApplisDispo(user)");
		// Récupération de la liste des applications auxquelles a accès le user
		List<Application> liste = applicationDao.findApplisCategoAutorises(user,categorie);	
		return liste;
	}
	
	
	
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
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#findRole(fr.toutatice.outils.ldap.entity.Person)
	 */
	public ArrayList<RoleApplicatif> findRole(Person user) throws ToutaticeAnnuaireException {
		
		RoleApplicatif roleApplicatif = (RoleApplicatif) context.getBean("roleApplicatif");
		ArrayList<RoleApplicatif> liste = new ArrayList<RoleApplicatif>();
		
		for (String dnRole : this.getListeRolesApplicatifs()) {
			RoleApplicatif role = roleApplicatif.findRoleByDn(dnRole);
			if(user.hasRole(role.getCn())){
				liste.add(role);
			}
		}		
		return liste;
	}
		
	/**
	 * Recherche les applications correspondant aux critères
	 * @param id ou début de l'id des applications recherchées
	 * @param nom ou début du nom des applications recherchées
	 * @param liste des catégories auxquelles doivent appartenir les applications recherchées
	 * @return liste des applications correspondant aux critères
	 */
	public List<Application> findApplisCritere(String id, String nom, List<String> listeCates) {
		logger.debug("entree dans la methode Application.findApplisCritere(id,nom,cate)");
		return applicationDao.findApplisCritere(id, nom, listeCates);
	}
	
	/**
	 * Recherche les applications correspondant aux critères et autorisées pour un utiliseur donné
	 * @param utilisateur pour lequel les applications doivent etre autorisées
	 * @param id ou début de l'id des applications recherchées
	 * @param nom ou début du nom des applications recherchées
	 * @param liste des catégories auxquelles doivent appartenir les applications recherchées
	 * @return liste des application correspondant aux critères
	 */
	public List<Application> findApplisCritere(Person user, String id, String nom,
			List<String> listeCates) {
		logger.debug("entree dans la methode Application.findApplisCritere(user,id,nom,cate)");
		return applicationDao.findApplisCritere(user, id, nom, listeCates);
	}
	
	/**
	 * Recherche d'applications par nom
	 * @param id ou début de l'id des applications recherchées
	 * @param nom ou début du nom des applications recherchées
	 * @return liste des application correspondant aux critères
	 */
	public List<Application> findApplisNom(String id, String nom) {
		logger.debug("entree dans la methode Application.findApplisNom(id,nom)");
		return applicationDao.findApplisNom(id, nom);
	}
	
	/**
	 * Recherche des applications grâce à leur DN
	 * Fonction utilisée notamment pour faire de l'auto-complétion
	 * @param dn début du DN des applications recherchées (préfixées par "ENTApplicationId=")
	 * @return liste des applications correspondant aux critères
	 */
	public List<Application> findApplisDebutDn(String dn) {
		logger.debug("entree dans la methode Application.findApplisDebutDn");
		return applicationDao.findApplisDebutDn(dn);
	}
	
	/**
	 * Recherche des applications appartenant à un groupe d'applications donné
	 * @param groupe groupe d'application pour lequel on veut la liste des applications
	 * @return liste des applications du groupe
	 */
	public List<Application> findListeApplisGrp(GroupesApplications groupe)
	{
		logger.debug("entree dans la methode Application.findListeApplisGrp(grp)");
		return applicationDao.findListeApplisGrp(groupe);
	}
	
	/**
	 * Recherche des applications appartenant à un groupe d'applications donné et qui sont autorisées
	 * pour l'utilisateur passé en paramètre
	 * @param user l'utilisateur habilité à utiliser les applications
	 * @param groupe groupe d'applications pour lequel on veut la liste des applications
	 * @return liste des applications recherchées
	 */
	public List<Application> findListeApplisGrp(Person user, GroupesApplications groupe)
	{
		return applicationDao.findListeApplisGrp(user, groupe);
	}
	
	public List<Application> findListeApplisStructure(Structure str)
	{
		return applicationDao.findListeApplisStructure(str.getDn());
	}
	
	public List<Application> findListeApplisStructure(Person user, Structure str)
	{
		return applicationDao.findListeApplisStructure(user, str.getDn());
	}
	
	public List<Application> findListeApplisStructure(Person user, Structure str, String contexte)
	{
		return applicationDao.findListeApplisStructure(user, str.getDn(),contexte);
	}
	
	public List<Application> findListeApplisStructureGereesByUser(Person user, Structure str)
	{
		return applicationDao.findListeApplisStructureGereesByUser(user, str.getDn()); 
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#autorise(fr.toutatice.outils.ldap.entity.Person)
	 */
	public boolean autorise(Person user) {
		logger.debug("entree dans la methode Application.autorise(user)");
		boolean autorisation = false;
	
		for (String roleAppli : this.getListeRolesApplicatifs()) {
			if(user.hasRole(roleAppli)){
					autorisation = true;
					break;
			}
		}

		if (!autorisation) {
			for (String profilAppli : this.getListeProfils()) {
				for (String profilPerson : user.getListeProfils()) {
					if (profilPerson.toLowerCase().equals(profilAppli.toLowerCase())) {
						autorisation = true;
						break;
					}

				}
			}
		}
		return autorisation;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#autoriseProfil(fr.toutatice.outils.ldap.entity.Person)
	 */
	public boolean autoriseProfil(Person user) {
		logger.debug("entree dans la methode Application.autoriseProfil");
		boolean autorisation = false;

			for (String profilAppli : this.getListeProfils()) {
				for (String profilPerson : user.getListeProfils()) {
					if (profilPerson.toLowerCase().equals(profilAppli.toLowerCase())) {
						autorisation = true;
						break;
					}

				}
			}		
		return autorisation;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#updateAppli()
	 */
	public void updateAppli() throws ToutaticeAnnuaireException
	{
		logger.debug("entree dans la methode Application.updateAppli");
		try {
			applicationDao.updateAppli(this);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#updateProfil()
	 */
	public void updateProfil() throws ToutaticeAnnuaireException {
		logger.debug("entree dans la methode Application.updateProfil");
		try {
			applicationDao.updateProfil(this);
		} catch (ToutaticeAnnuaireException e) {
			logger.error("Erreur lors de la mise à jour des profils de l'application "+this.getId());
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#create()
	 */
	public void create() throws ToutaticeAnnuaireException
	{
		logger.debug("entree dans la methode Application.create");
		try {
			applicationDao.create(this);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
	}
	/**
	 * Codage d'un mot de passe en base 64 SHA
	 * @param motDePasse mot de passe à coder
	 * @return mot de passe codé en abse 64 SHA
	 */
	public static String fonctionBase64Sha1(String motDePasse)
			throws NoSuchAlgorithmException {
		logger.debug("entree dans la methode Application.fonctionBase64Sha1");
		MessageDigest md = MessageDigest.getInstance("SHA");
		byte[] b = md.digest(motDePasse.getBytes());
		Base64 base64 = new Base64();
		byte[] b1 = base64.encode(b);
		String s = new String(b1);
		s = "{SHA}" + s;
		return s;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Application#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.context = ctx;

	}

}
