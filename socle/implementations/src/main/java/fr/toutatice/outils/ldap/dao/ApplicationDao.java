package fr.toutatice.outils.ldap.dao;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.toutatice.outils.ldap.entity.Application;
import fr.toutatice.outils.ldap.entity.GroupesApplications;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;


/**
 * Classe permettant d'accéder à l'annuaire LDAP, de consulter 
 * et mettre à jour les informations concernant les applications.
 * Les attributs de cette classe sont les libellés des champs dans l'annuaire LDAP
 * Ils sont à renseigner dans un fichier properties et injectés dans cette classe via SPRING
 * @author aguihomat
 *
 */

//l'Interface ApplicationContextAware permet d'instancier des objets via Spring via un getBean sur le contexte

@Repository
@Qualifier("applicationDao")
@Scope("singleton")
public class ApplicationDao implements ApplicationContextAware {
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	protected static ApplicationContext context;
	
	@Autowired
	@Qualifier("ldapTemplateEcriture")
	protected LdapTemplate ldapTemplateEcriture;
	
	@Autowired
	@Qualifier("ldapTemplateLecture")
	protected LdapTemplate ldapTemplateLecture;

	// attributs permettant de paramétrer le nom des champs de la base de
	// données dans le fichier de config Spring

	//nom de la catégorie "Application" dans LDAP
	protected static String categorieLDAP;
	
	//attributs obligatoires
	private static String id;
	private static String nom;
	private static String objectClass;
	private static String categories;
	private static String passwords;
	
	//attributs facultatifs
	private static String description;
	private static String typeApplication;
	private static String url;
	private static String profils;
	private static String rolesapplicatifs;
	private static String urlLogout;
	private static String derMaj;
	private static String proprietaire;
	private static String dbUri;
	private static String modAuth;
	private static String proprietes;
	private static String BASE_DN="";
	private static String manager="";
	private static String explicitManager;
	private static String appliOrganisation;
	private static String nature;
	private static String contexte;
	
	//Attributs CDC XT
	private static String cdcCatego;
	private static String cdcRne;
	
	// Setters, obligatoires pour instanciation va le fichier de config Spring
	public void setId(String s) {
		id = s;
	}
	public void setNom(String s) {
		nom = s;
	}
	public void setDescription(String s) {
		description = s;
	}
	public void setTypeApplication(String s) {
		typeApplication = s;
	}
	public void setUrl(String s) {
		url = s;
	}
	public void setPasswords(String s) {
		passwords = s;
	}
	public void setProfils(String s) {
		profils = s;
	}
	public void setCategories(String s) {
		categories = s;
	}
	public void setObjectClass(String s) {
		objectClass = s;
	}
	public void setRolesapplicatifs(String s) {
		rolesapplicatifs = s;
	}
	public void setCdcCatego(String s) {
		cdcCatego = s;
	}
	public void setCdcRne(String s) {
		cdcRne = s;
	}
	public void setAppliOrganisation(String s) {
		appliOrganisation = s;
	}
	public void setCategorieLDAP(String s) {
		categorieLDAP = s;
	}
	public void setUrlLogout(String s) {
		urlLogout = s;
	}
	public void setDerMaj(String s) {
		derMaj = s;
	}
	public void setProprietaire(String s) {
		proprietaire = s;
	}
	public void setDbUri(String s) {
		dbUri = s;
	}
	public void setModAuth(String s) {
		modAuth = s;
	}
	public void setBASE_DN(String s) {
		BASE_DN = s;
	}
	public void setProprietes(String prop) {
		proprietes = prop;
	}
	public void setNature(String n){
		nature = n;
	}
	public void setContexte(String c){
		contexte = c;
	}
	public void setManager(String m) {
		manager = m;
	}
	public void setExplicitManager(String m) {
		explicitManager = m;
	}
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		context = ctx;
	}
	public static ApplicationContext getContext() {
		return context;
	}
	public static String getCategorieLDAP() {
		return categorieLDAP;
	}
	public static String getId() {
		return id;
	}
	public static String getNom() {
		return nom;
	}
	public static String getObjectClass() {
		return objectClass;
	}
	public static String getCategories() {
		return categories;
	}
	public static String getPasswords() {
		return passwords;
	}
	public static String getDescription() {
		return description;
	}
	public static String getUrl() {
		return url;
	}
	public static String getProfils() {
		return profils;
	}
	public static String getRolesapplicatifs() {
		return rolesapplicatifs;
	}
	public static String getCDCCatego() {
		return cdcCatego;
	}
	public static String getCDCRne() {
		return cdcRne;
	}
	public static String getAppliOrganisation() {
		return appliOrganisation;
	}
	public static String getUrlLogout() {
		return urlLogout;
	}
	public static String getDerMaj() {
		return derMaj;
	}
	public static String getProprietaire() {
		return proprietaire;
	}
	public static String getManager() {
		return manager;
	}
	public static String getDbUri() {
		return dbUri;
	}
	public static String getModAuth() {
		return modAuth;
	}
	public LdapTemplate getLdapTemplateEcriture() {
		return ldapTemplateEcriture;
	}
	public void setLdapTemplateEcriture(LdapTemplate ldapTemplateEcriture) {
		this.ldapTemplateEcriture = ldapTemplateEcriture;
	}
	public LdapTemplate getLdapTemplateLecture() {
		return ldapTemplateLecture;
	}
	public void setLdapTemplateLecture(LdapTemplate ldapTemplateLecture) {
		this.ldapTemplateLecture = ldapTemplateLecture;
	}





	// Classe interne permettant de récupérer une application dans l'annuaire
	protected static class ApplicationAttributMapper implements AttributesMapper {
		
		public Object mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {

			// instanciation d'un objet Application via Spring (il ne faut pas
			// utiliser le constructeur par défaut !)
			Application app = (Application) context.getBean("application");
			app = (Application) this.valoriser(app,attrs);

			return app;
			}

		@SuppressWarnings("rawtypes")
		public Object valoriser(Application app, Attributes attrs) throws javax.naming.NamingException
		{
			Attribute attr = attrs.get(id);
			if (attr != null) {
				app.setId(attr.get().toString());
			} else {logger.error("Erreur d'accès à l'annuaire : recherche d'une application");}
	
			attr = attrs.get(nom);
			if (attr != null) {
				app.setNom(attr.get().toString());
			} else { 
				app.setNom("");
			}
			
			if (attrs.get(description) != null)
				{ app.setDescription(attrs.get(description).get().toString());}
			else {
				app.setDescription("");
			}
			
			if (attrs.get(typeApplication) != null)
			{ app.setTypeApplication(attrs.get(typeApplication).get().toString());}
			else {
				app.setTypeApplication("");
			}
			
			if (attrs.get(cdcCatego) != null)
			{ app.setCdcCatego(attrs.get(cdcCatego).get().toString());}
			else {
				app.setCdcCatego("");
			}
			
			if (attrs.get(cdcRne) != null)
			{ app.setCdcRne(attrs.get(cdcRne).get().toString());}
			else {
				app.setCdcRne("");
			} 
			
			attr = attrs.get(nature);
			if (attr != null) {
				app.setNature(attr.get().toString());
			} else { 
				app.setNature("");
			}
			
			attr = attrs.get(contexte);
			if (attr != null) {
				app.setContexte(attr.get().toString());
			} else { 
				app.setContexte("");
			}
			
			if (attrs.get(url) != null)
				{ app.setUrl(attrs.get(url).get().toString());}
			else {
				app.setUrl("");
				//logger.info("l'application "+app.getId()+" n'a pas d'URL de renseignée dans l'annuaire");
			}
			
			if (attrs.get(dbUri) != null)
				{ app.setDbUri(attrs.get(dbUri).get().toString());}
			else {
				app.setDbUri("");
				//logger.info("l'application "+app.getId()+" n'a pas de DbUri de renseigné dans l'annuaire");
			}
			
			attr = attrs.get(proprietaire);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addOwner((String)m.next());
				}
			}
			attr = attrs.get(manager);
			if(attr != null) {
				NamingEnumeration m = (attr.getAll());
				while(m.hasMore()) {
					app.addManager((String)m.next());
				}
			}
			
			attr = attrs.get(explicitManager);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addExplicitManager((String) m.next());
				}
			}
			
			attr = attrs.get(passwords);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addPassword((byte[])m.next());
				}
			}

			attr = attrs.get(profils);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addProfil((String)m.next());
				}
			}
			
			attr = attrs.get(rolesapplicatifs);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addRoleApplicatif((String) m.next());
				}
			}
			
			attr = attrs.get(appliOrganisation);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addAppliOrganisation((String) m.next());
				}
			}
			
			attr = attrs.get(categories);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addCategories((String) m.next());
				}
			}
			
			attr = attrs.get(derMaj);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addDerMaj((String) m.next());
				}
			}
			
			attr = attrs.get(modAuth);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addModAuth((String) m.next());
				}
			}
			
			attr = attrs.get(proprietes);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addProprietes((String) m.next());
				}
			}
			
			attr = attrs.get(urlLogout);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addUrlLogout((String) m.next());
				}
			}

			return app;
		}
	}
	
	protected Attributes buildAttributes(Application app) {
	      Attributes attrs = new BasicAttributes();
	      BasicAttribute ocattr = new BasicAttribute("objectclass");
	      ocattr.add("top");
	      ocattr.add(objectClass);
	      attrs.put(ocattr);
	      attrs.put(id, app.getId());
	      attrs.put(nom, app.getNom());
	      attrs.put(url, app.getUrl());
	      attrs.put(description, app.getDescription());
	      attrs.put(cdcCatego, app.getCdcCatego());
	      attrs.put(cdcRne, app.getCdcRne());
	      
	      if(app.getTypeApplication()!=null){
	    	  attrs.put(typeApplication,app.getTypeApplication());
	      }
	      
	      if(app.getNature()!=null){
	    	  attrs.put(nature,app.getNature());
	      }
	      if(app.getContexte()!=null){
	    	  attrs.put(contexte,app.getContexte());
	      }
	      
	      //attrs.put(proprietaire, app.getProprietaire());
	     	      
	      BasicAttribute pwdattr = new BasicAttribute(passwords);
	      for (byte[] o : app.getListePasswords())
	      {
	    	  pwdattr.add(o);
	      }
	      attrs.put(pwdattr);
	      
	      BasicAttribute propattr = new BasicAttribute(proprietaire);
	      for (String s : app.getListeOwners())
	      {
	    	  propattr.add(s);
	      }
	      attrs.put(propattr);
	      
	      BasicAttribute catattr = new BasicAttribute(categories);
	      for (String s : app.getCategories())
	      {
	    	  catattr.add(s);
	      }
	      attrs.put(catattr);
	      
	      BasicAttribute prfattr = new BasicAttribute(profils);
	      for (String s : app.getListeProfils())
	      {
	    	  prfattr.add(s);
	      }
	      attrs.put(prfattr);
	      
	      BasicAttribute orgattr = new BasicAttribute(appliOrganisation);
	      for (String s : app.getListeAppliOrganisations())
	      {
	    	  orgattr.add(s);
	      }
	      attrs.put(orgattr);
	      
	      return attrs;
	   }



	/**
	 * Construit le DN d'une application à partir de son id
	 * @param idAppli identifiant de l'application dont on veut le DN
	 * @return DN de l'application (sans la base de l'annuaire LDAP, défini dans le fichier properties)
	 */
	protected Name buildDn(String idAppli) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou", categorieLDAP);
		dn.add(id, idAppli);
		return dn;
	}
	
	/**
	 * Construit le DN d'une application à partir de cette dernière
	 * @param appli application dont on veut le DN
	 * @return  DN de l'application (sans la base de l'annuaire LDAP, défini dans le fichier properties)
	 */
	protected Name buildDn(Application appli)
	{
		return this.buildDn(appli.getId());
	}
	
	/**
	 * Construit le DN d'une application à partir de son nom
	 * @param id de l'application
	 * @return DN complet de l'application (avec la base de l'annuaire LDAP, défini dans le fichier properties)
	 */
	public String findFullDn(String id)
	{
		if(id.trim().isEmpty()){
			return null;
		} else {
			String dn= this.buildDn(id).toString() + "," + BASE_DN;
			return dn;
		}
	}


	/**
	 * Recherche d'une application par son Id exact
	 * 
	 * @param id exact de l'application recherchée
	 * @return Application recherchée
	 * @throws ToutaticeAnnuaireException 
	 */
	public Application findByPrimaryKey(String id) throws ToutaticeAnnuaireException {
		Application application = (Application) context.getBean("application");
		if(!id.trim().isEmpty()){
			Name dn = buildDn(id);
			try {
				ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
				application = (Application) ldapTemplateLecture.lookup(dn,
						applicationAttributMapper);
			} 		
			catch (NameNotFoundException e) {
				logger.error("Recherche d'application : identifiant non reconnu " + id);
				throw new ToutaticeAnnuaireException("Erreur lors de la recherche dans l'annuaire : application "+id+" inconnue");
			}
		} else {
			application=null;
		}
		return application;
	}
	

	/**
	 * Récupère toutes les applications contenues dans l'annuaire
	 * @return liste de toutes les applications existantes
	 */
	@SuppressWarnings("unchecked")
	public List<Application> findAllApplis()
	{
		logger.debug("entree dans la methode application.findAllApplis");
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass.toLowerCase()));
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	
	/**
	 * Récupère toutes les applications autorisées pour un utilisateur, 
	 * en fonction de ses profils et de ses roles applicatifs
	 * @param user Utilisateur pour lequel on veut connaitre les applications autorisées
	 * @return liste des applications autorisées pour l'utilisateur passé en paramètre
	 */
	@SuppressWarnings("unchecked")
	public List<Application> findApplisAutorisesParProfilUid(Person user)
	{
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		OrFilter orFilter = new OrFilter();
		for(String p:listeProfils)	
		{
			orFilter.or(new EqualsFilter(profils, p));
		}	
		
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));

		List<Application> liste;
		if ( orFilter.equals(new OrFilter()))
			{liste= new ArrayList<Application>();}
		else
		{
			filter.and(orFilter);
			ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
			liste = ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(), applicationAttributMapper);
		}
		return liste;
	}
	
	@SuppressWarnings("unchecked")
	public List<Application> findApplisAutorisesParProfilUid(Person user, String ctx)
	{
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		OrFilter orFilter = new OrFilter();
		for(String p:listeProfils)	
		{
			orFilter.or(new EqualsFilter(profils, p));
		}	
		
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));

		List<Application> liste;
		if ( orFilter.equals(new OrFilter()))
			{liste= new ArrayList<Application>();}
		else
		{
			filter.and(orFilter);
			//Filtre sur le contexte
			OrFilter orFilter2 = new OrFilter();
			orFilter2.or(new EqualsFilter(contexte, ctx));
			orFilter2.or(new NotFilter(new LikeFilter(contexte, "*")));
			filter.and(orFilter2);
			ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
			liste = ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(), applicationAttributMapper);
		}
		return liste;
	}
	
	/**
	 * Récupère toutes les applications autorisées pour un utilisateur, 
	 * en fonction de ses profils et de ses roles applicatifs
	 * @param user Utilisateur pour lequel on veut connaitre les applications autorisées
	 * @return liste des applications autorisées pour l'utilisateur passé en paramètre
	 */
	@SuppressWarnings("unchecked")
	public List<Application> findAllApplisAutorises(Person user)
	{
		logger.debug("entree dans la methode application.findAllApplisautorises");
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		OrFilter orFilter = new OrFilter();
		for(String p:listeProfils)	
		{
			orFilter.or(new EqualsFilter(profils, p));
		}	
		for (String dnRole :listeRoles)
		{
			orFilter.or(new EqualsFilter(rolesapplicatifs,dnRole));
		}
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));

		List<Application> liste;
		if ( orFilter.equals(new OrFilter()))
			{liste= new ArrayList<Application>();}
		else
		{
			filter.and(orFilter);
			ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
			liste = ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(), applicationAttributMapper);
		}
		return liste;
	}
	
	@SuppressWarnings("unchecked")
	public List<Application> findApplisCategoAutorises(Person user, String categorie)
	{
		
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		OrFilter orFilter = new OrFilter();
		for(String p:listeProfils)	
		{
			orFilter.or(new EqualsFilter(profils, p));
		}	
		for (String dnRole :listeRoles)
		{
			orFilter.or(new EqualsFilter(rolesapplicatifs,dnRole));
		}
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		
		filter.and(new EqualsFilter(this.categories, categorie));

		List<Application> liste;
		if ( orFilter.equals(new OrFilter()))
			{liste= new ArrayList<Application>();}
		else
		{
			filter.and(orFilter);
			ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
			liste = ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(), applicationAttributMapper);
		}
		return liste;
	}
	
	/**
	 * Recherche la liste des applications dont la personne est manager de façon implicite
	 * @param user
	 * @return
	 */
	public List<Application> findListeApplisGereesImplicitementUser(Person user) {
		AndFilter filter = new AndFilter();
		OrFilter orFilter = new OrFilter();
		for(String p:user.getListeProfils())	
		{
			orFilter.or(new EqualsFilter(this.manager, p));
		}	
		orFilter.or(new EqualsFilter(this.manager, user.getDn()));
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(orFilter);
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	/**
	 * Recherche la liste des applications dont la personne est manager de façon explicite
	 * @param user
	 * @return
	 */
	public List<Application> findListeApplisGereesExplicitementUser(Person user) {
		AndFilter filter = new AndFilter();
		OrFilter orFilter = new OrFilter();
		for(String p:user.getListeProfils())	
		{
			orFilter.or(new EqualsFilter(this.explicitManager, p));
		}	
		orFilter.or(new EqualsFilter(this.explicitManager, user.getDn()));
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(orFilter);
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	/**
	 * Récupère les applications qui correspondent aux critères de recherches 
	 * passés en paramètre : identifiant, nom et catégories de l'application 
	 * et qui sont autorisées pour l'utilisateur passé en paramètre
	 * @param user utilisateur devant avoir le droit d'accès aux applications recherchées
	 * @param idApp identifiant ou début de l'identifiant des applications recherchées
	 * @param nomApp nom ou début du nom des applications recherchées
	 * @param listeCategories liste des catégories auxquelles doivent appartenir les applications recherchées
	 * @return liste des applications correspondant aux critères et autorisées pour l'utilisateur
	 */
	@SuppressWarnings("unchecked")
	public List<Application> findApplisCritere(Person user, 
			String idApp, String nomApp, List<String> listeCategories)
	{
		logger.debug("entree dans la methode application.findApplisCritere(user,id,nom,cate");
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		idApp = idApp.concat("*");
		filter.and(new LikeFilter(id,idApp));
		nomApp = nomApp.concat("*");
		filter.and(new LikeFilter(nom,nomApp));
		
		//Filtre sur la catégorie de l'application
		OrFilter orFilter2 = new OrFilter();
		for(String s : listeCategories)
		{
			orFilter2.or(new EqualsFilter(categories,s));
		}
		filter.and(orFilter2);  
		
		
		//Filtre sur les profils et roles autorisés
		OrFilter orFilter = new OrFilter();
		for(String p:listeProfils)	
		{
			orFilter.or(new EqualsFilter(profils, p));
		}	
		for (String dnRole:listeRoles)
		{
			orFilter.or(new EqualsFilter(rolesapplicatifs,dnRole));
		}
		
		List<Application> liste;
		
		if ( orFilter.equals(new OrFilter()))
			{liste= new ArrayList<Application>();}
		else
		{
			filter.and(orFilter);
			ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
			liste = ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
		}
		return liste;
	}
	
/**
 * Récupère les applications de l'annuaire qui correspondent aux critères de recherches 
 * passés en paramètre : id, nom et catégories de l'application
 * @param idApp identifiant ou début de l'identifiant des applications recherchées
 * @param nomApp nom ou début du nom des applications recherchées
 * @param listeCategories liste des catégories auxquelles doivent appartenir les applications recherchées
 * @return liste des applications correspondant aux critères
 */
	@SuppressWarnings("unchecked")
	public List<Application> findApplisCritere(	String idApp, String nomApp, List<String> listeCategories)
	{
		logger.debug("entree dans la methode application.findApplisCritere(id,nom,cate)");
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		idApp = idApp.concat("*");
		filter.and(new LikeFilter(id,idApp));
		nomApp = nomApp.concat("*");
		filter.and(new LikeFilter(nom,nomApp));
		
		//Filtre sur la catégorie de l'application
		OrFilter orFilter2 = new OrFilter();
		for(String s : listeCategories)
		{
			orFilter2.or(new EqualsFilter(categories,s));
		}
		filter.and(orFilter2);  

		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	/**
	 * Recherche d'applications par nom
	 * @param idApp identifiant ou début de l'identifiant des applications recherchées
	 * @param nomApp nom ou début du nom des applications recherchées
	 * @return liste des applications correspondant aux critères
	 */
		@SuppressWarnings("unchecked")
		public List<Application> findApplisNom(	String idApp, String nomApp)
		{
			logger.debug("entree dans la methode application.findApplisNom(id,nom)");
			AndFilter filter = new AndFilter();
			filter.and(new EqualsFilter("objectclass", objectClass));
			
			OrFilter orFilter = new OrFilter();
			idApp = idApp.concat("*");
			orFilter.or(new LikeFilter(id,idApp));
			nomApp = nomApp.concat("*");
			orFilter.or(new LikeFilter(nom,nomApp));
			
			filter.and(orFilter);
			
			ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
			return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
		}
	
	/**
	 * Recherche les applications dont le DN commence par le dn passé en paramètre
	 * @param dnApp début du DN des applications recherchées (préfixées par "ENTApplicationId=")
	 * @return liste des applications correspondant au critère
	 */
	@SuppressWarnings("unchecked")
	public List<Application> findApplisDebutDn(String dnApp)
	{
		logger.debug("entree dans la methode application.findApplisDebutDn");
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		dnApp = dnApp.substring(17);
		dnApp = dnApp.concat("*");
		filter.and(new LikeFilter(id,dnApp));
		
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	/**
	 * Recherche les applications autorisées pour l'utilisateur passé en paramètre et qui
	 * appartiennent au groupe d'application choisi
	 * @param user l'utilisateur autorisé
	 * @param groupe le groupe auquel doivent appartenir les applications recherchées
	 * @return liste des applications correspondant aux critères
	 */
	@SuppressWarnings("unchecked")
	public List<Application> findListeApplisGrp(Person user, GroupesApplications groupe)
	{
		logger.debug("entree dans la methode application.findListeApplisGrp(user,grp)");
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		AndFilter filter = new AndFilter();
		
		//Filtre sur l'url du groupe
		String filtreGroup = groupe.getMemberUrl();
		
		//Filtre sur les profils et roles autorisés
		OrFilter orFilter = new OrFilter();
		for(String p:listeProfils)	
		{
			orFilter.or(new EqualsFilter(profils, p));
		}	
		for (String dnRole:listeRoles)
		{
			orFilter.or(new EqualsFilter(rolesapplicatifs,dnRole));
		}
		
		filter.and(orFilter);
		
		String filtreGlobal = "(&" + filtreGroup.concat(filter.encode()) + ")";
		
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filtreGlobal, applicationAttributMapper);
	}
	

	/**
	 * Recherche les applications appartenant à un groupe d'applications donné
	 * @param groupe le groupe auquel appartiennent les applications recherchées
	 * @return liste des applications appartenant au groupe d'applications
	 */
	@SuppressWarnings("unchecked")
	public List<Application> findListeApplisGrp(GroupesApplications groupe)
	{
		logger.debug("entree dans la methode application.findListeApplisGrp(grp)");
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", groupe.getMemberUrl(), applicationAttributMapper);
	}
	
	@SuppressWarnings("unchecked")
	public List<Application> findListeApplisStructure(String dnStructure)
	{
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new EqualsFilter(appliOrganisation,dnStructure));	
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	@SuppressWarnings("unchecked")
	public List<Application> findListeApplisStructure(Person user, String dnStructure)
	{
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		AndFilter filter = new AndFilter();
		
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new EqualsFilter(appliOrganisation,dnStructure));	
		
		//Filtre sur les profils et roles autorisés
		OrFilter orFilter = new OrFilter();
		for(String p:listeProfils)	
		{
			orFilter.or(new EqualsFilter(profils, p));
		}	
		for (String dnRole:listeRoles)
		{
			orFilter.or(new EqualsFilter(rolesapplicatifs,dnRole));
		}
		
		filter.and(orFilter);
		
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	@SuppressWarnings("unchecked")
	public List<Application> findListeApplisStructure(Person user, String dnStructure, String ctx)
	{
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		AndFilter filter = new AndFilter();
		
		filter.and(new EqualsFilter("objectclass", objectClass));
		
		
		
		//Filtre sur les profils et roles autorisés
		OrFilter orFilter = new OrFilter();
		for(String p:listeProfils)	
		{
			orFilter.or(new EqualsFilter(profils, p));
		}	
		for (String dnRole:listeRoles)
		{
			orFilter.or(new EqualsFilter(rolesapplicatifs,dnRole));
		}
		
		filter.and(orFilter);
		
		//Filtre sur le contexte et l'organisation
		OrFilter orFilter2 = new OrFilter();
		orFilter2.or(new EqualsFilter(contexte, ctx));
		orFilter2.or(new EqualsFilter(appliOrganisation,dnStructure));
	
		filter.and(orFilter2);

		
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	@SuppressWarnings("unchecked")
	public List<Application> findListeApplisStructureGereesByUser(Person user, String dnStructure)
	{
		List<String> listeProfils = user.getListeProfils();
		List<String> listeRoles = user.getListeRoles();
		
		AndFilter filter = new AndFilter();
		
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new EqualsFilter(appliOrganisation,dnStructure));	
		
		//Filtre sur les managers
		OrFilter orFilter = new OrFilter();
		orFilter.or(new EqualsFilter(this.explicitManager, user.getDn()));
		orFilter.or(new EqualsFilter(this.manager, user.getDn()));
		for(String dnProfil:user.getListeProfils()){
			orFilter.or(new EqualsFilter(this.explicitManager,dnProfil));
		}
		for(String dnProfil:user.getListeProfils()){
			orFilter.or(new EqualsFilter(this.manager,dnProfil));
		}
		
		filter.and(orFilter);
		
		ApplicationAttributMapper applicationAttributMapper = new ApplicationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}
	
	/**
	 * Mise à jour d'une application 
	 * La mise à jour s'applique uniquement aux attributs suivants : nom, description, url, profils
	 * @param application à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updateAppli(Application appli) throws ToutaticeAnnuaireException {

		Name dn = this.buildDn(appli);

		try {
			Attribute attrNom = new BasicAttribute(nom, appli.getNom());
			ModificationItem itemNom = new ModificationItem(
					DirContext.REPLACE_ATTRIBUTE, attrNom);
			Attribute attrDesc = new BasicAttribute(description, appli
					.getDescription());
			ModificationItem itemDesc = new ModificationItem(
					DirContext.REPLACE_ATTRIBUTE, attrDesc);
			Attribute attrUrl = new BasicAttribute(url, appli.getUrl());
			ModificationItem itemUrl = new ModificationItem(
					DirContext.REPLACE_ATTRIBUTE, attrUrl);
			ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { itemNom,
					itemDesc, itemUrl });
		} catch (NamingException e) {
			logger.error("Impossible de mettre à jour l'application "+appli.getNom());
			throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour de l'application "+appli.getId()+" dans l'annuaire");
		} 

		
		this.updateExplicitManager(appli);
		this.updateProfil(appli);
	}
	
	/**
	 * Mise à jour des profils d'une application
	 * @param appli l'application à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updateProfil(Application appli) throws ToutaticeAnnuaireException
	{
		Name dn = this.buildDn(appli);
		appli.setListeProfils(Helper.supprimerDoublonsCaseNonSensitive(appli.getListeProfils()));
		
		if(appli.getListeProfils().size()>0){
			// update annuaire
			Object[] profils = new Object[appli.getListeProfils().size()];
			int i = 0;
			for (Object o : appli.getListeProfils()) {
				profils[i] = o;
				i++;
			}
			try {
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(ApplicationDao.profils, profils);
			ldapTemplateEcriture.modifyAttributes(context);
			} catch (NamingException e) {
				logger.error("Impossible de mettre à jour les profils de l'application "+appli.getNom());
				logger.error("Contexte : "+context);
				throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour des profils de l'application "+appli.getId()+" dans l'annuaire");
			}
		}else {
			// Si il n'y a plus aucun profil on supprime l'attribut
			Application appliLDAP = this.findByPrimaryKey(appli.getId());
			 if(appliLDAP.getListeProfils().size() > 0) 
			 	{
					BasicAttribute attrMembers = new BasicAttribute(profils, false);
					attrMembers.add(null);
					ModificationItem item = new ModificationItem(
							DirContext.REMOVE_ATTRIBUTE, attrMembers);
					ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
			 	}
			 
	}
	}
	
	/**
	 * Mise à jour des managers d'une application
	 * @param appli l'application à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updateManager(Application appli) throws ToutaticeAnnuaireException
	{
		appli.setListeManagers(Helper.supprimerDoublonsCaseNonSensitive(appli.getListeManagers()));
		Name dn = this.buildDn(appli);
		
		if(appli.getListeManagers().size()>0){
			// update annuaire
			Object[] managers = new Object[appli.getListeManagers().size()];
			int i = 0;
			for (Object o : appli.getListeManagers()) {
				managers[i] = o;
				i++;
			}
			try {
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(ApplicationDao.manager, managers);
			ldapTemplateEcriture.modifyAttributes(context);
			} catch (NamingException e) {
				logger.error("Impossible de mettre à jour les managers de l'application "+appli.getNom());
				logger.error("Contexte : "+context);
				throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour des managers de l'application "+appli.getId()+" dans l'annuaire");
			}
		}else {
			// Si il n'y a plus aucun profil on supprime l'attribut
			Application appliLDAP = this.findByPrimaryKey(appli.getId());
			 if(appliLDAP.getListeManagers().size() > 0) 
			 	{
					BasicAttribute attrMembers = new BasicAttribute(manager, false);
					attrMembers.add(null);
					ModificationItem item = new ModificationItem(
							DirContext.REMOVE_ATTRIBUTE, attrMembers);
					ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
			 	}
				
	}
	}
	
	/**
	 * Mise à jour des managers explicites d'une application
	 * @param appli l'application à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 */
	public void updateExplicitManager(Application appli) throws ToutaticeAnnuaireException
	{
		appli.setListeExplicitManagers(Helper.supprimerDoublonsCaseNonSensitive(appli.getListeExplicitManagers()));
		Name dn = this.buildDn(appli);
		
		if(appli.getListeExplicitManagers().size()>0){
			// update annuaire
			Object[] explicitManagers = new Object[appli.getListeExplicitManagers().size()];
			int i = 0;
			for (Object o : appli.getListeExplicitManagers()) {
				explicitManagers[i] = o;
				i++;
			}
			try {
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(ApplicationDao.explicitManager, explicitManagers);
			ldapTemplateEcriture.modifyAttributes(context);
			} catch (NamingException e) {
				logger.error("Impossible de mettre à jour les managers explicites de l'application "+appli.getNom());
				logger.error("Contexte : "+context);
				throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour des managers explicites de l'application "+appli.getId()+" dans l'annuaire");
			}
		}else {
			// Si il n'y a plus aucun profil on supprime l'attribut
			Application appliLDAP = this.findByPrimaryKey(appli.getId());
			 if(appliLDAP.getListeExplicitManagers().size() > 0) 
			 	{
					BasicAttribute attrMembers = new BasicAttribute(explicitManager, false);
					attrMembers.add(null);
					ModificationItem item = new ModificationItem(
							DirContext.REMOVE_ATTRIBUTE, attrMembers);
					ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
			 	}
				
	}
	}
	
	/**
	 * Création d'une application dans l'annuaire
	 * @param app application à créér
	 * @throws ToutaticeAnnuaireException
	 */
	 public void create(Application app) throws ToutaticeAnnuaireException {
		 logger.debug("entree dans la methode application.create");
		  Name dn = buildDn(app);
		  try {
	      ldapTemplateEcriture.bind(dn, null, buildAttributes(app));
		  } catch (NamingException e) {
				logger.error("Impossible de créér l'application "+ app.getNom());
				throw new ToutaticeAnnuaireException("Erreur lors de la création de l'application "+app.getId()+" dans l'annuaire");
			} 
	   }
		

	

}
