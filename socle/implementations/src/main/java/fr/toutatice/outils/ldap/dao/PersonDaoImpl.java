package fr.toutatice.outils.ldap.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.NoSuchAttributeException;
import org.springframework.ldap.control.SortControlDirContextProcessor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AggregateDirContextProcessor;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.entity.RoleApplicatif;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;
import fr.toutatice.outils.ldap.util.PersonComparator;
import fr.toutatice.outils.ldap.util.ToutaticeAnnuaireConfig;


/**
 * Classe permettant d'accéder à l'annuaire LDAP, de consulter 
 * et mettre à jour les informations de personnes.
 * Les attributs de cette classe sont les libellés des champs dans l'annuaire LDAP
 * Ils sont à renseigner dans un fichier properties et injectés dans cette classe via SPRING
 * @author aguihomat
 *
 */

// l'Interface BeanFactoryAware permet d'instancier des objets via Spring via un getBean
@Repository
@Scope("singleton")
public class PersonDaoImpl implements ApplicationContextAware, PersonDao{

	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	protected static final Log loggerModif = LogFactory.getLog("fr.toutatice.annuaire.modif");
	protected static final Log loggerCache = LogFactory.getLog("fr.toutatice.annuaire.cache");
	private static ApplicationContext context;

	@Autowired
	@Qualifier("annuaireConfig")
	private ToutaticeAnnuaireConfig annuaireConfig;
	
	@Autowired
	@Qualifier("ldapTemplateEcriture")
	private LdapTemplate ldapTemplateEcriture;
	
	@Autowired
	@Qualifier("ldapTemplateLecture")
	private LdapTemplate ldapTemplateLecture;
	
	@Autowired
	@Qualifier("ldapTemplateLectureNonPoolee")
	private LdapTemplate ldapTemplateLectureNonPoolee;
	
	
	// attributs permettant de paramétrer le nom des champs de la base de données dans le fichier de config Spring
	
	private static String prenom ="";
	private static String prenomNom ="";
	private static String nom ="";
	private static String nomPrenom =""; 
	private static String id ="";
	private static String alias ="";
	private static String sexe="";
	private static String rne ="";
	private static String password ="";
	private static String idsurcharge ="";
	private static String profils ="";
	private static String categorie ="";
	private static String classObjet ="";
	private static String BASE_DN="";
	private static String email="";
	private static String title="";
	private static String divcod="";
	private static String dateNaissance="";
	private static String manager="";
	private static String explicitManager;
	private static String sourceSI;
	private static String elevesConcernes;
	private static String idSiecle;
	private static String personJointure;
	private static String nationalProfil;
	private static String personalTitle;
	private static String disciplinePoste;

	// Setters, obligatoires pour instanciation via le fichier de config Spring
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setPrenom(java.lang.String)
	 */
	public void setPrenom(String s) {
		prenom = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setPrenomNom(java.lang.String)
	 */
	public void setPrenomNom(String s) {
		prenomNom = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setNomPrenom(java.lang.String)
	 */
	public void setNomPrenom(String s) {
		nomPrenom = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setNom(java.lang.String)
	 */
	public void setNom(String s) {
		nom = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setId(java.lang.String)
	 */
	public void setId(String s) {
		id = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setAlias(java.lang.String)
	 */
	public void setAlias(String s) {
		alias = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setSexe(java.lang.String)
	 */
	public void setSexe(String s) {
		sexe = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setRne(java.lang.String)
	 */
	public void setRne(String s) {
		rne = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setPassword(java.lang.String)
	 */
	public void setPassword(String s) {
		password = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setIdsurcharge(java.lang.String)
	 */
	public void setIdsurcharge(String s) {
		idsurcharge = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setProfils(java.lang.String)
	 */
	public void setProfils(String s) {
		profils = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setCategorie(java.lang.String)
	 */
	public void setCategorie(String s) {
		categorie = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setClasseObjet(java.lang.String)
	 */
	public void setClasseObjet(String s) {
		classObjet = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setBASE_DN(java.lang.String)
	 */
	public void setBASE_DN(String s) {
		BASE_DN = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setTitle(java.lang.String)
	 */
	public void setTitle(String s) {
		title = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setEmail(java.lang.String)
	 */
	public void setEmail(String s) {
		email = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setDivcod(java.lang.String)
	 */
	public void setDivcod(String s) {
		divcod = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setDateNaissance(java.lang.String)
	 */
	public void setDateNaissance(String s) {
		dateNaissance = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setManager(java.lang.String)
	 */
	public void setManager(String s) {
		manager = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setExplicitManager(java.lang.String)
	 */
	public void setExplicitManager(String m) {
		explicitManager = m;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setSourceSI(java.lang.String)
	 */
	public void setSourceSI(String s) {
		sourceSI = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setElevesConcernes(java.lang.String)
	 */
	public void setElevesConcernes(String s) {
		elevesConcernes = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setIdSiecle(java.lang.String)
	 */
	public void setIdSiecle(String s) {
		idSiecle = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setPersonJointure(java.lang.String)
	 */
	public void setPersonJointure(String s) {
		personJointure = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setNationalProfil(java.lang.String)
	 */
	public void setNationalProfil(String s) {
		nationalProfil = s;
	}	
	public void setPersonalTitle(String s) {
		personalTitle = s;
	}
	public void setDisciplinePoste(String s){
		disciplinePoste = s;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getLdapTemplateEcriture()
	 */
	public LdapTemplate getLdapTemplateEcriture() {
		return ldapTemplateEcriture;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setLdapTemplateEcriture(org.springframework.ldap.core.LdapTemplate)
	 */
	public void setLdapTemplateEcriture(LdapTemplate ldapTemplateEcriture) {
		this.ldapTemplateEcriture = ldapTemplateEcriture;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getLdapTemplateLecture()
	 */
	public LdapTemplate getLdapTemplateLecture() {
		return ldapTemplateLecture;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setLdapTemplateLecture(org.springframework.ldap.core.LdapTemplate)
	 */
	public void setLdapTemplateLecture(LdapTemplate ldapTemplateLecture) {
		this.ldapTemplateLecture = ldapTemplateLecture;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getLdapTemplateLectureNonPoolee()
	 */
	public LdapTemplate getLdapTemplateLectureNonPoolee() {
		return ldapTemplateLectureNonPoolee;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setLdapTemplateLectureNonPoolee(org.springframework.ldap.core.LdapTemplate)
	 */
	public void setLdapTemplateLectureNonPoolee(LdapTemplate ldapTemplateLectureNonPoolee) {
		this.ldapTemplateLectureNonPoolee = ldapTemplateLectureNonPoolee;
	}




	//Classe interne permettant de récupérer un objet Personne depuis l'annuaire
	private static class PersonAttributMapper implements AttributesMapper {
		@SuppressWarnings("rawtypes")
		public Object mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {
			
			//instanciation d'un objet Person va Spring (ne pas utiliser le constructeur par défaut !)
			Person p = (Person) context.getBean("person");
			
			Attribute attr = attrs.get(id);
			if (attr != null)
			{
				p.setUid(attr.get().toString());
			}
			else {logger.error("Erreur d'accès à l'annuaire : recherche de personne");}
			
			attr = attrs.get(prenom);
			if (attr != null) {
				p.setGivenName(attr.get().toString());
			}
			else { 
				p.setGivenName("");}
			
			attr = attrs.get(prenomNom);
			if (attr != null) {
				p.setDisplayName(attr.get().toString());
			}
			else { 
				p.setDisplayName("");}

			attr = attrs.get(nomPrenom);
			if (attr != null)
			{
				p.setCn(attr.get().toString());
			}
			else { 
				p.setCn("");}
			
			attr = attrs.get(nom);
			if (attr != null)
			{
				p.setSn(attr.get().toString());
			}
			else { 
				p.setSn("");}
			
			attr = attrs.get(personalTitle);
			if (attr != null)
			{
				p.setPersonalTitle(attr.get().toString());
			}
			else { 
				p.setPersonalTitle("");}
			
			attr = attrs.get(disciplinePoste);
			if (attr != null)
			{
				p.setDisciplinePoste(attr.get().toString());
			}
			else { 
				p.setDisciplinePoste("");}
			
			attr = attrs.get(alias);
			if (attr != null)
			{
				p.setAlias(attr.get().toString());
			}
			else { 
				p.setAlias("");}
			
			attr = attrs.get(sexe);
			if (attr != null)
			{
				String s = attr.get().toString();
				if(s.equals("1")){
					p.setSexe("M");
				}
				if(s.equals("2")){
					p.setSexe("F");
				}
			}
			else { 
				p.setSexe("");
				}
			
			attr = attrs.get(dateNaissance);
			if (attr != null) {
				p.setDateNaissance(attr.get().toString());
			}
			else { 
				p.setDateNaissance("");}
			
			
			attr = attrs.get(rne);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					p.addRne((String)m.next());
				}
			}
			
			attr = attrs.get(idsurcharge);
			if (attr != null) {
				String s = attr.get().toString();
				String[] tab = s.split("\\|");
				if(tab.length>0 && tab[0]!=null){
					p.setIdSurcharge(tab[0]);
				}
				if(tab.length>1 && tab[1]!=null){
					p.setTypeSurcharge(tab[1]);
				}
				if(tab.length>2 && tab[2]!=null){
					p.setMotifSurcharge(tab[2]);
				}
				
			}
			
			attr = attrs.get(email);
			if (attr != null) {
				p.setEmail(attr.get().toString());
			}
			else { 
				p.setEmail("");}
			
			attr = attrs.get(title);
			if (attr != null) {
				p.setTitle(attr.get().toString());
			}
			else { 
				p.setTitle("");}
			
			attr = attrs.get(sourceSI);
			if (attr != null) {
				p.setSourceSI(attr.get().toString());
			}
			else { 
				p.setSourceSI("");}
			
			attr = attrs.get(divcod);
			if (attr != null) {
				p.setDivcod(attr.get().toString());
			}
			else { if (p.getTitle().toLowerCase().equals("ele")) {
					p.setDivcod("");}}
			
			attr = attrs.get(idSiecle);
			if (attr != null) {
				p.setIdSiecle(attr.get().toString());
			}
			attr = attrs.get(personJointure);
			if (attr != null) {
				p.setPersonJointure(attr.get().toString());
			}
			attr = attrs.get(nationalProfil);
			if (attr != null) {
				p.setNationalProfil(attr.get().toString());
			}
			
			attr = attrs.get(password);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					try {
						p.addPasswordSSHA(new String((byte[])m.next()));
					} catch (ToutaticeAnnuaireException e) {
						e.printStackTrace();
					}
				}				
			}
		
			attr = attrs.get(profils);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					p.addProfil((String)m.next());
				}
			}
			
			attr = attrs.get(manager);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					p.addImplicitManager((String)m.next());
				}
			}
			
			attr = attrs.get(explicitManager);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					p.addExplicitManager((String) m.next());
				}
			}
			
			attr = attrs.get(elevesConcernes);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					String idSiecleEleve = (String)m.next();
					p.getListeIdSiecleElevesConcernes().add(idSiecleEleve);
					
					Person eleve = p.findEleveByIdSiecle(idSiecleEleve);
					if(eleve!=null){
						p.getListeUidElevesConcernes().add(eleve.getUid());
					}
					
				}
			}
		
			return p;
		}
	}
	
	
	protected Attributes buildAttributes(Person p) {
	      Attributes attrs = new BasicAttributes();
	      BasicAttribute ocattr = new BasicAttribute("objectclass");
	      ocattr.add("top");
	      ocattr.add(classObjet);
	      ocattr.add("person");
	      ocattr.add("organizationalPerson");
	      ocattr.add("inetOrgPerson");

	      if(p.getTitle()!=null){
		      if(p.getTitle().equals("ELE")){
		    	  ocattr.add("ENTEleve");
		      }
		      if(p.getUid().endsWith("@ATEN")){
		    	  ocattr.add("ENTAuxPersRelEleve");
		      }
	      }
	      
	      attrs.put(ocattr);
	      attrs.put(id, p.getUid());
	      attrs.put(nom, p.getSn());
	      attrs.put(prenom,p.getGivenName());
	      attrs.put(prenomNom,p.getDisplayName());
	      attrs.put(nomPrenom,p.getCn());
	      if(p.getAlias()!=null){
	    	  attrs.put(alias,p.getAlias());
	      }
	      if(p.getPersonalTitle()!=null){
	    	  attrs.put(personalTitle,p.getPersonalTitle());
	      }
	      if(p.getDisciplinePoste()!=null){
	    	  attrs.put(disciplinePoste,p.getDisciplinePoste());
	      }
	      if(p.getEmail()!=null){
	    	  attrs.put(email,p.getEmail());
	      }
	      if(p.getSexe()!=null){
	    	  if(p.getSexe().equals("F")){
	    		  attrs.put(sexe,"2");
	    	  }else{
	    		  attrs.put(sexe,"1");
	    	  }
	      }
	      if(p.getTitle()!=null){
	    	  attrs.put(title,p.getTitle());
	      }
	      if(p.getDivcod()!=null){
	      attrs.put(divcod,p.getDivcod());
	      }
	      if(p.getDateNaissance()!=null){
	    	  attrs.put(dateNaissance,p.getDateNaissance());
	      }
	      if(p.getSourceSI()!=null){
	    	  attrs.put(sourceSI,p.getSourceSI());
	      }
	     
	      if(p.getIdSiecle()!=null){
		      attrs.put(idSiecle,p.getIdSiecle());
		      }
	      if(p.getPersonJointure()!=null){
		      attrs.put(personJointure,p.getPersonJointure());
		      }
	      if(p.getNationalProfil()!=null){
		      attrs.put(nationalProfil,p.getNationalProfil());
		      }
	      if(p.getListeIdSiecleElevesConcernes().size()>0){
		      BasicAttribute ecattr = new BasicAttribute(elevesConcernes);
		      for (String s : p.getListeIdSiecleElevesConcernes())
		      {
		    	  ecattr.add(s);
		      }
		      attrs.put(ecattr);
	      }
	      
	      if(p.getListePasswords().size()>0){
		      BasicAttribute pwdattr = new BasicAttribute(password);
		      for (String s : p.getListePasswords())
		      {
		    	  pwdattr.add(s);
		      }
		      attrs.put(pwdattr);
	      }
	      
	      if(p.getListeRnes().size()>0){
		      BasicAttribute rneattr = new BasicAttribute(rne);
		      for (String s : p.getListeRnes())
		      {
		    	  rneattr.add(s);
		      }
		      attrs.put(rneattr);
	      }
	      
	      if(p.getListeProfils().size()>0){
		      BasicAttribute profilsattr = new BasicAttribute(profils);
		      for (String s : p.getListeProfils())
		      {
		    	  profilsattr.add(s);
		      }
		      attrs.put(profilsattr);
	      }
	      
	      if(p.getListeImplicitManagers().size()>0){
		      BasicAttribute managersIattr = new BasicAttribute(manager);
		      for (String s : p.getListeImplicitManagers())
		      {
		    	  managersIattr.add(s);
		      }
		      attrs.put(managersIattr);
	      }
	      
	      if(p.getListeExplicitManagers().size()>0){
		      BasicAttribute managersEattr = new BasicAttribute(explicitManager);
		      for (String s : p.getListeExplicitManagers())
		      {
		    	  managersEattr.add(s);
		      }
		      attrs.put(managersEattr);
	      }
	  
	      return attrs;
	}

	
	/**
	 * Construit le DN d'une personne à partir de son id
	 * @param uid identifiant de la personne dont on veut le DN
	 * @return DN de la personne (sans la base de l'annuaire LDAP, défini dans le fichier properties)
	 */
	private static Name buildDn(String uid) {
		
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou", categorie);
		dn.add(id, uid);
		return dn;
	}

	/**
	 * Construit le DN d'une personne
	 * @param p personne dont on veut le DN
	 * @return DN de la personne (sans la base de l'annuaire LDAP, défini dans le fichier properties)
	 */
	private Name buildDn(Person p) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou", categorie);
		dn.add(id, p.getUid());
		return dn;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#buildFullDn(java.lang.String)
	 */
	public String buildFullDn(String uid)
	{
		if(uid.trim().isEmpty()){
			return null;
		} else {
			String dn= buildDn(uid).toString() + "," + BASE_DN;
			return dn;
		}
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#findByPrimaryKey(java.lang.String)
	 */
	@Cacheable(key = "#uid", value = { "personByPrimaryKeyCache" })
	public Person findByPrimaryKey(String uid){

		loggerCache.debug("findByPrimaryKey/" + uid);

		Person person = null;
		if(!uid.trim().isEmpty()){
			
			Name dn = buildDn(uid);
			
			try {
				PersonAttributMapper personAttributMapper = new PersonAttributMapper();
				person = (Person) ldapTemplateLecture.lookup(dn, personAttributMapper);
			} 
			catch (NameNotFoundException e) {
				logger.warn("Recherche d'une personne dans l'annuaire : l'identifiant n'existe pas " + uid);
			}
			catch(Exception e) {
				logger.warn("Erreur lors de la recherche de la personne "+uid+" dans l'annuaire");
				logger.warn(e.toString());
			}
		}
		return person;
	
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#findByDn(java.lang.String)
	 */
	public Person findByDn(String dn) throws ToutaticeAnnuaireException {

		loggerCache.debug("findByDn/" + dn);

		Person p = (Person) context.getBean("person");
		try {
			PersonAttributMapper personAttributMapper = new PersonAttributMapper();
			DirContext DC = ldapTemplateLecture.getContextSource().getReadOnlyContext();
			
			int i = dn.lastIndexOf(BASE_DN);
			String subDn="";
			if(i>0 && dn.length()>i){
				subDn = dn.substring(0, i-1);
				p = (Person) ldapTemplateLecture.lookup(new DistinguishedName(subDn), personAttributMapper);
			}else{
				p=null;
			}
		} catch (org.springframework.ldap.NameNotFoundException e) {
			logger.warn("Recherche d'une personne dans l'annuaire : l'identifiant n'existe pas " + dn);
			p=null;
		} 
		return p;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getPersonByUid(java.lang.String)
	 */
	public List<Person> getPersonByUid(String uid) {

		loggerCache.debug("getPersonByUid/" + uid);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(id, uid+"*"));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);
		return liste;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getPersonByName(java.lang.String)
	 */
	public List<Person> getPersonByName(String name) {

		loggerCache.debug("getPersonByName/" + name);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(nom, name+"*"));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);
		return liste;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getPersonByMail(java.lang.String)
	 */
	public List<Person> getPersonByMail(String mail) {

		loggerCache.debug("getPersonByMail/" + mail);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(email, mail+"*"));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);
		return liste;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getPersonByTitle(java.lang.String)
	 */
	public List<Person> getPersonByTitle(String titre) {

		loggerCache.debug("getPersonByTitle/" + titre);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(title, titre+"*"));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);
		return liste;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getPersonByRne(java.lang.String)
	 */
	public List<Person> getPersonByRne(String noRne) {

		loggerCache.debug("getPersonByRne/" + noRne);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		rne = rne.concat("*");
		filter.and(new LikeFilter(rne, noRne));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);
		return liste;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getPersonByNomIdRne(java.lang.String, java.lang.String)
	 */
	public List<Person> getPersonByNomIdRne(String nomId, String noRne) {
	
		loggerCache.debug("getPersonByNomIdRne/" + nomId + "/" + noRne);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		if (!(noRne.trim().isEmpty()||noRne.equals("*"))) {
			filter.and(new LikeFilter(this.profils, "cn="+noRne+"*"));
		}
		OrFilter filterOr = new OrFilter();
		nomId = nomId.concat("*");
		filterOr.or(new LikeFilter(id, nomId));
		filterOr.or(new LikeFilter(nom, nomId));
		filter.and(filterOr);
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(),personAttributMapper);
		return liste;
		
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getPersonByCriteres(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Person> getPersonByCriteres(String nomId, String noRne,	String mail, String titre, String critereTri) {
		return this.getPersonByCriteres(nomId, noRne, mail, titre, null, critereTri);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getPersonByCriteres(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Person> getPersonByCriteres(String nomId, String noRne,	String mail, String titre, String divcod_, String critereTri) {

		loggerCache.debug("getPersonByCriteres/" + nomId + "/" + noRne + "/" + mail
				+ "/" + titre + "/" + critereTri);

	    SearchControls searchControls = new SearchControls() ;
	    searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE ) ;
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		/*if (!(noRne.trim().isEmpty())) {
			noRne = noRne.concat("*");
			filter.and(new LikeFilter(rne, noRne));
		}*/
		
		if (!(divcod_.trim().isEmpty())) {
			//divcod = divcod.concat("*");
			filter.and(new EqualsFilter(divcod, divcod_));
		}
		
		if (!(noRne.trim().isEmpty()||noRne.equals("*"))) {
			filter.and(new LikeFilter(this.profils, "cn="+noRne+"*"));
		}
		
		if (!(mail.trim().isEmpty())) {
			mail = mail.concat("*");
			filter.and(new LikeFilter(email, mail));
		}
		if (!(titre.trim().isEmpty())) {
			titre = titre.concat("*");
			filter.and(new LikeFilter(title, titre));
		}

		
		OrFilter filterOr = new OrFilter();
		nomId = nomId.concat("*");
		filterOr.or(new LikeFilter(id, nomId));
		filterOr.or(new LikeFilter(nom, nomId));
		filter.and(filterOr);
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		
		List<Person> liste = null;
		if(annuaireConfig.getSortingEnabled()) {
			AggregateDirContextProcessor processor = new AggregateDirContextProcessor() ;
		    SortControlDirContextProcessor sorter = new SortControlDirContextProcessor(critereTri) ;
		    processor.addDirContextProcessor( sorter ) ;
			
			liste = ldapTemplateLecture.search("", filter.encode(), searchControls,personAttributMapper, processor);
		}
		else {
			liste = ldapTemplateLecture.search("", filter.encode(), searchControls,personAttributMapper);
			
			Collections.sort(liste, new PersonComparator());
			
		}
		
		return liste;

	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#findPersonneMultiCriteres(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String)
	 */
	public List<Person> findPersonneMultiCriteres(String nomId, String rne, List<String> listeDnProfils, String filtre, String critereTri){

		loggerCache.debug("findPersonneMultiCriteres/" + nomId + "/" + rne + "/"
				+ listeDnProfils + "/" + filtre + "/" + critereTri);


	    SearchControls searchControls = new SearchControls() ;
	    searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE ) ;
	    AndFilter filter = new AndFilter();
	    
	    if(listeDnProfils!=null){
		    for(String dn:listeDnProfils){
		    	if(!dn.trim().isEmpty()){
		    		filter.and(new EqualsFilter(this.profils, dn));
		    	}
		    }
	    }
	    if(!nomId.trim().isEmpty()||nomId.equals("*")){
			OrFilter filterOr = new OrFilter();
			filterOr.or(new LikeFilter(id, nomId+"*"));
			filterOr.or(new LikeFilter(nom, nomId+"*"));
			filterOr.or(new LikeFilter(nomPrenom,nomId+"*"));
			filter.and(filterOr);
	    }
		
		String filtreGlobal;
		if (filtre != null && !filtre.trim().isEmpty()){
			filtreGlobal = "(&" + filtre.concat(filter.encode()) + ")";
		}
		else{
			filtreGlobal= filter.encode();
		}
		
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		

		List<Person> listeProvisoire = null;
		if(annuaireConfig.getSortingEnabled()) {
			AggregateDirContextProcessor processor = new AggregateDirContextProcessor() ;
		    SortControlDirContextProcessor sorter = new SortControlDirContextProcessor(critereTri) ;
		    processor.addDirContextProcessor( sorter ) ;
			
		    listeProvisoire = ldapTemplateLecture.search("", filtreGlobal, searchControls,personAttributMapper, processor);
		}
		else {
			listeProvisoire = ldapTemplateLecture.search("", filtreGlobal, searchControls,personAttributMapper);
			
			
			Collections.sort(listeProvisoire, new PersonComparator());
		}
		
		
		List<Person> liste = new ArrayList<Person>();

        if (rne == null)
            rne = "*";

        if (!(rne.trim().isEmpty() || rne.equals("*"))) {
		    for(Person person:listeProvisoire){
		    	if(person.hasStructure(rne)){
		    		liste.add(person);
		    	}
		    }
		   
		  } 
		 else{
		    	liste = listeProvisoire;
		 }	 
		
		return liste;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#findListePersonnesByDnProfil(java.lang.String)
	 */
	public List<Person> findListePersonnesByDnProfil(String dnProfil) {

		loggerCache.debug("findListePersonnesByDnProfil/" + dnProfil);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(profils, dnProfil));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);
		
		return liste;	
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#findEleveByIdSiecle(java.lang.String)
	 */
	public Person findEleveByIdSiecle(String id){

		loggerCache.debug("findEleveByIdSiecle/" + id);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "ENTEleve"));
		filter.and(new EqualsFilter(idSiecle, id));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);	
		if(liste.size()==1){
			return liste.get(0);
		}else{
			return null;
		}
	}
	
	public List<Person> findParents(String idSiecle){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "ENTAuxPersRelEleve"));
		filter.and(new EqualsFilter(elevesConcernes, idSiecle));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);	
		return liste;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getSurcharge(java.lang.String)
	 */
	public List<Person> getSurcharge(String critereTriResultat) {

	    SearchControls searchControls = new SearchControls() ;
	    searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE ) ;
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(idsurcharge, "*"));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		
		List<Person> liste;
		if(annuaireConfig.getSortingEnabled()) {
			
			AggregateDirContextProcessor processor = new AggregateDirContextProcessor() ;
			SortControlDirContextProcessor sorter;
			if(critereTriResultat.trim().isEmpty()){
				sorter = new SortControlDirContextProcessor("cn") ;
			} else{
				sorter = new SortControlDirContextProcessor(critereTriResultat) ;
			}
		    processor.addDirContextProcessor( sorter ) ;
			
			liste = ldapTemplateLecture.search("", filter.encode(), searchControls,personAttributMapper, processor);
		}
		else {
			liste = ldapTemplateLecture.search("", filter.encode(), searchControls,personAttributMapper);
			
			Collections.sort(liste, new PersonComparator());
		}
		
		
		List<Person> listeFinale = new ArrayList<Person>();
		
		for(Person person : liste)
		{
			// Cas où le mot de passe n'est plus surchargé mais l'attribut EntPersonSmdp est toujours renseigné
			// -> on supprime l'attribut qui ne sert plus à rien
			if (person.getListePasswords().size()<2){
				try {
					this.deletePersonSmdp(person);
				} catch (ToutaticeAnnuaireException e) {
					e.printStackTrace();
				}
			}
			else{
				listeFinale.add(person);
			}
		}
		return liste;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getSurchargeParUtilisateur(java.lang.String, java.lang.String)
	 */
	public List<Person> getSurchargeParUtilisateur(String uid,String critereTriResultat) {

	    SearchControls searchControls = new SearchControls() ;
	    searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE ) ;
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(idsurcharge, uid+"|*"));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		
		List<Person> liste;
		if(annuaireConfig.getSortingEnabled()) {
			
			AggregateDirContextProcessor processor = new AggregateDirContextProcessor() ;
			SortControlDirContextProcessor sorter;
			if(critereTriResultat.trim().isEmpty()){
				sorter = new SortControlDirContextProcessor("cn") ;
			} else{
				sorter = new SortControlDirContextProcessor(critereTriResultat) ;
			}
		    processor.addDirContextProcessor( sorter ) ;
			
			liste = ldapTemplateLecture.search("", filter.encode(), searchControls,personAttributMapper, processor);	
		}
		else {
			liste = ldapTemplateLecture.search("", filter.encode(), searchControls,personAttributMapper);
			
			Collections.sort(liste, new PersonComparator());			
		}
		
		
		
		List<Person> listeFinale = new ArrayList<Person>();
		for(Person person : liste)
		{
			// On élimine les personnes considérées à tort comme surchargées
			if (person.getListePasswords().size()<2)
			{
				try {
					this.deletePersonSmdp(person);
				} catch (ToutaticeAnnuaireException e) {
					e.printStackTrace();
				}
			}
			else
			{
				listeFinale.add(person);
			}
		}
		return listeFinale;
	}
	

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#update(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#p.uid")
	public void update(Person p) throws ToutaticeAnnuaireException, javax.naming.NamingException {
		try {
		    List<ModificationItem> modifiedItems = new ArrayList<ModificationItem>();
			Name dn = buildDn(p);
			
            if (prenom != null && p.getGivenName() != null && StringUtils.isNotBlank(p.getGivenName())) {
                modifiedItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(prenom, p.getGivenName())));

            }

            if (nom != null && p.getSn() != null && StringUtils.isNotBlank(p.getSn())) {
                modifiedItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(nom, p.getSn())));

            }

            if (nomPrenom != null && p.getCn() != null && StringUtils.isNotBlank(p.getCn())) {
                modifiedItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(nomPrenom, p.getCn())));

            }

            if (prenomNom != null && p.getDisplayName() != null && StringUtils.isNotBlank(p.getDisplayName())) {
                modifiedItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(prenomNom, p.getDisplayName())));

            }

            if (alias != null && p.getAlias() != null && StringUtils.isNotBlank(p.getAlias())) {
                modifiedItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(alias, p.getAlias())));

            }

            if (dateNaissance != null && p.getDateNaissance() != null && StringUtils.isNotBlank(p.getDateNaissance())) {
                modifiedItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(dateNaissance, p.getDateNaissance())));

            }

            if (title != null && p.getTitle() != null && StringUtils.isNotBlank(p.getTitle())) {
                modifiedItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(title, p.getTitle())));

            }

            if (divcod != null && p.getDivcod() != null && StringUtils.isNotBlank(p.getDivcod())) {
                modifiedItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(divcod, p.getDivcod())));

            }
            
			
			this.updateEmail(p);
			this.updateRne(p);			
			this.updateProfil(p);
			this.updateExplicitManagers(p);
			
            ldapTemplateEcriture.modifyAttributes(dn, modifiedItems.toArray(new ModificationItem[]{}));
			loggerModif.info("La personne "+p.getUid()+" a été modifiée");
		}
		catch(NamingException e)
		{ 	logger.error("Impossible de mettre à jour la fiche de l'utilisateur "+p.getCn());
			logger.error(e.toString());
			throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour de la fiche de la personne "+p.getCn());
		}
		
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#ajoutProfilViaMajProfil(java.lang.String, java.lang.String)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#personMaj.uid")
	public void ajoutProfilViaMajProfil(Person personMaj, String dnProfil) throws ToutaticeAnnuaireException {
		// Person personMaj = this.findByDn(dnMembre);
		
		//elimination des doublons
		boolean existe = false;
		for(String dn : personMaj.getListeProfils()) {
			if (dn.toLowerCase().equals(dnProfil.toLowerCase())) {
				existe = true;
			}
		}
		if(!existe) {
			personMaj.addProfil(dnProfil);
		}
		
		Name dn = buildDn(personMaj);
		if(personMaj.getListeProfils().size() > 0) 
		{
			Object[] listeProfils = new Object[personMaj.getListeProfils().size()];
			int i = 0;
			for (Object o : personMaj.getListeProfils()) {
				listeProfils[i] = o;
				i++;
			}
			try {
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
				context.setAttributeValues(this.profils, listeProfils);
			ldapTemplateEcriture.modifyAttributes(context);
			} catch (NamingException e) {
				logger.error("Impossible de mettre à jour les profils de la personne "+personMaj.getUid());
				logger.error(e.toString());
				throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour des profils de la personne "+personMaj.getUid()+" dans l'annuaire");
			} 
		} else
		// Si il n'y a aucun membre on supprime l'attribut
		{
			if(personMaj.getListeProfils().size() > 0) 
		 	{
				BasicAttribute attrMembers = new BasicAttribute(this.profils,
						false);
				attrMembers.add(null);
				ModificationItem item = new ModificationItem(
						DirContext.REMOVE_ATTRIBUTE, attrMembers);
				ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
		 	}
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#supprimerProfilViaMajProfil(java.lang.String, java.lang.String)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#personMaj.uid")
	public void supprimerProfilViaMajProfil(Person personMaj, String dnProfil) throws ToutaticeAnnuaireException {
		// Person personMaj = this.findByDn(dnMembre);
		personMaj.removeProfil(dnProfil);
		Name dn = buildDn(personMaj);
	
		if (personMaj.getListeProfils().size() > 0)
		{
			Object[] listeProfils = new Object[personMaj.getListeProfils().size()];
			int i = 0;
			for (Object o : personMaj.getListeProfils()) {
				listeProfils[i] = o;
				i++;
			}
			try {
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
				context.setAttributeValues(this.profils, listeProfils);
			ldapTemplateEcriture.modifyAttributes(context);
			} catch (NamingException e) {
				logger.error("Impossible de mettre à jour les profils de la personne "+personMaj.getUid());
				logger.error(e.toString());
				throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour des profils de la personne "+personMaj.getUid()+" dans l'annuaire");
			} 
		} else
			// Si il n'y a aucun membre on supprime l'attribut
			{
//				if(personMaj.getListeProfils().size() > 0) 
//			 	{
				BasicAttribute attrMembers = new BasicAttribute(this.profils,
						false);
					attrMembers.add(null);
					ModificationItem item = new ModificationItem(
							DirContext.REMOVE_ATTRIBUTE, attrMembers);
					ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
//			 	}
			}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#updateProfil(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#personMaj.uid")
	public void updateProfil(Person personMaj) throws ToutaticeAnnuaireException, javax.naming.NamingException
	{
		ProfilDao profilDao = (ProfilDao) context.getBean("profilDao");
		
		//elimination des doublons
		personMaj.setListeProfils(Helper.supprimerDoublonsCaseNonSensitive(personMaj.getListeProfils()));
		
		
		//mise à jour des objets profils liés à la personne dans l'annuaire
		Person personLDAP = this.findByPrimaryKey(personMaj.getUid());
		
		//Comparaison du profil à mettre à jour avec le profil dans LDAP pour identifier les membres ajoutés et supprimés
		//test non case-sensitive
		List<String> profilsSupprimes = new ArrayList<String>();
		List<String> profilsAjoutes = new ArrayList<String>();
		
		List<String> listeProfilsMaj = new ArrayList<String>();
		List<String> listeProfilsLDAP = new ArrayList<String>();
		for(String s : personMaj.getListeProfils()) {
			listeProfilsMaj.add(s.toLowerCase());
		}
		for(String s : personLDAP.getListeProfils()){
			listeProfilsLDAP.add(s.toLowerCase());
		}
		
		for(String profil:personLDAP.getListeProfils()) {
			if(! listeProfilsMaj.contains(profil.toLowerCase())) {
				profilsSupprimes.add(profil);
			}
		}
		for(String profil:personMaj.getListeProfils()) {
			if(! listeProfilsLDAP.contains(profil.toLowerCase())) {
				profilsAjoutes.add(profil);
			}
		}
		
		// Mise à jour des profils impactées
		for(String profil : profilsSupprimes) {
			profilDao.supprimerMembreViaMajMembre(profil, personLDAP.getDn());
		}
		for(String profil : profilsAjoutes) {
			profilDao.ajoutMembreViaMajMembre(profil, personLDAP.getDn());
		}
		
		// maj de la personne
		Name dn = buildDn(personMaj);
		if(personMaj.getListeProfils().size() > 0) 
		{
			Object[] listeProfils = new Object[personMaj.getListeProfils().size()];
			int i = 0;
			for (Object o : personMaj.getListeProfils()) {
				listeProfils[i] = o;
				i++;
			}
			try {
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
				context.setAttributeValues(this.profils, listeProfils);
			ldapTemplateEcriture.modifyAttributes(context);
			loggerModif.info("La personne "+personMaj.getUid()+" a été modifiée");
			} catch (NamingException e) {
				logger.error("Impossible de mettre à jour les profils de la personne "+personMaj.getUid());
				logger.error(e.toString());
				throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour des profils de la personne "+personMaj.getUid()+" dans l'annuaire");
			} 
		} else
			// Si il n'y a aucun profils on supprime l'attribut
			{
				if(personMaj.getListeProfils().size() > 0) 
			 	{
				BasicAttribute attrMembers = new BasicAttribute(this.profils,
						false);
					attrMembers.add(null);
					ModificationItem item = new ModificationItem(
							DirContext.REMOVE_ATTRIBUTE, attrMembers);
					ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
					loggerModif.info("La personne "+personMaj.getUid()+" a été modifiée");
			 	}
			}
	}
	

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#updateImplicitManagers(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#personMaj.uid")
	public void updateImplicitManagers(Person personMaj) throws ToutaticeAnnuaireException {
		//elimination des doublons
		personMaj.setListeImplicitManagers(Helper.supprimerDoublonsCaseNonSensitive(personMaj.getListeImplicitManagers()));

		Name dn = this.buildDn(personMaj);
		if (personMaj.getListeImplicitManagers().size() > 0) {
			Object[] managers = new Object[personMaj.getListeImplicitManagers().size()];
			int i = 0;
			for (Object o : personMaj.getListeImplicitManagers()) {
				managers[i] = o;
				i++;
			}
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(manager, managers);
			ldapTemplateEcriture.modifyAttributes(context);
			loggerModif.info("La personne "+personMaj.getUid()+" a été modifiée");
		} else 
		// Si il n'y a aucun membre on supprime l'attribut
		{
			Person personLDAP = this.findByPrimaryKey(personMaj.getUid());
			 if(personLDAP.getListeImplicitManagers().size() > 0) 
			 	{
					BasicAttribute attrMembers = new BasicAttribute(manager, false);
					attrMembers.add(null);
					ModificationItem item = new ModificationItem(
							DirContext.REMOVE_ATTRIBUTE, attrMembers);
					ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
					loggerModif.info("La personne "+personMaj.getUid()+" a été modifiée");
			 	}
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#updateExplicitManagers(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#personMaj.uid")
	public void updateExplicitManagers(Person personMaj) throws ToutaticeAnnuaireException {
		//elimination des doublons
		personMaj.setListeExplicitManagers(Helper.supprimerDoublonsCaseNonSensitive(personMaj.getListeExplicitManagers()));

		Name dn = this.buildDn(personMaj);
		if (personMaj.getListeExplicitManagers().size() > 0) {
			Object[] explicitManagers = new Object[personMaj.getListeExplicitManagers().size()];
			int i = 0;
			for (Object o : personMaj.getListeExplicitManagers()) {
				explicitManagers[i] = o;
				i++;
			}
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(explicitManager, explicitManagers);
			ldapTemplateEcriture.modifyAttributes(context);
			loggerModif.info("La personne "+personMaj.getUid()+" a été modifiée");
		} else 
		// Si il n'y a aucun membre on supprime l'attribut
		{
			Person personLDAP = this.findByPrimaryKey(personMaj.getUid());
			 if(personLDAP.getListeExplicitManagers().size() > 0) 
			 	{
					BasicAttribute attrMembers = new BasicAttribute(explicitManager, false);
					attrMembers.add(null);
					ModificationItem item = new ModificationItem(
							DirContext.REMOVE_ATTRIBUTE, attrMembers);
					ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
					loggerModif.info("La personne "+personMaj.getUid()+" a été modifiée");
			 	}
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#updateRne(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#p.uid")
	public void updateRne(Person p) throws ToutaticeAnnuaireException
	{
		//elimination des doublons
		p.setListeRnes(Helper.supprimerDoublonsCaseNonSensitive(p.getListeRnes()));
		
		
		Name dn = buildDn(p);
		Object[] listeRne = new Object[p.getListeRnes().size()];
		int i = 0;
		for (Object o : p.getListeRnes()) {
			listeRne[i] = o;
			i++;
		}
		try {
		DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(this.rne, listeRne);
		ldapTemplateEcriture.modifyAttributes(context);
		loggerModif.info("La personne "+p.getUid()+" a été modifiée");
		} catch (NamingException e) {
			logger.error("Impossible de mettre à jour les rne de la personne "+p.getUid());
			logger.error(e.toString());
			throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour des rne de la personne "+p.getUid()+" dans l'annuaire");
		} 
	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#updatePassword(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#p.uid")
	public void updatePassword(Person p) throws ToutaticeAnnuaireException {
		Name dn = buildDn(p);
		if (p.getListePasswords().size() > 1)
		{		
			List<byte[]> listeMdpByte = new ArrayList<byte[]>();
			for(String s:p.getListePasswords())
			{
				listeMdpByte.add(s.getBytes());
			}
			Object[] pwd = new Object[p.getListePasswords().size()];
			int i = 0;
			for (Object o : listeMdpByte) {
				pwd[i] = o;
				i++;
			}		
			try{
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(password, pwd);
			ldapTemplateEcriture.modifyAttributes(context);
			loggerModif.info("La personne "+p.getUid()+" a été modifiée");
			} catch (NamingException e) {
				logger.error("Impossible de mettre le mot de passe à jour pour l'utilisateur "+p.getCn());
				logger.error(e.toString());
				throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour du mot de passe pour la personne "+p.getCn());
			}
		}
		
		else
		{
			try {
			Attribute attr = new BasicAttribute(password, p.getListePasswords().get(0));
			ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
			ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] {item});
			loggerModif.info("La personne "+p.getUid()+" a été modifiée");
			} catch (NamingException e) {
				logger.error("Impossible de mettre le mot de passe à jour pour l'utilisateur "+p.getCn());
				logger.error(e.toString()); 
				throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour du mot de passe pour la personne "+p.getCn()); }
			}
		
	}	
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#authenticate(java.lang.String, java.lang.String)
	 */
	public boolean authenticate(String uid, String mdpNonCode){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new EqualsFilter(id, uid));
		return this.getLdapTemplateLectureNonPoolee().authenticate("", filter.toString(), mdpNonCode);
	}
	


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#addPersonSmdp(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#p.uid")
	public void addPersonSmdp(Person p) throws ToutaticeAnnuaireException {
		try {
			Name dn = buildDn(p);
			String s = "";
			if(p.getIdSurcharge()!=null){
				s=p.getIdSurcharge();
				if(p.getTypeSurcharge()!=null){
					s=s+"|"+p.getTypeSurcharge();
					if(p.getMotifSurcharge()!=null){
						s=s+"|"+p.getMotifSurcharge();
					}
				}
			}
			Attribute attr = new BasicAttribute(idsurcharge,s);
			ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
			ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] {item});
		}
		catch(NamingException e) {
			logger.error("Impossible de mettre à jour l'attribut "+idsurcharge+" pour l'utilisateur "+p.getCn());
			logger.error(e.toString());
			throw new ToutaticeAnnuaireException("Erreur lors de la surcharge sur la personne "+p.getCn()+". Impossible de mettre à jour l'attribut personSmdp");
		}

	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#deletePersonSmdp(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#p.uid")
	public void deletePersonSmdp(Person p) throws ToutaticeAnnuaireException {
		Name dn = buildDn(p);
		BasicAttribute battr = new BasicAttribute(idsurcharge, false);
		battr.add(null);
		ModificationItem item = new ModificationItem(
				DirContext.REMOVE_ATTRIBUTE, battr);
		try {
			ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
			loggerModif.info("La personne "+p.getUid()+" a été modifiée");
		}catch(NoSuchAttributeException e) {
			logger.warn("la personne "+p.getDisplayName()+" était surchargée sans entpersonSmdp de renseignée");
		}
		catch (NamingException e) {
			logger.error("Impossible de supprimer l'attribut " + idsurcharge + " pour l'utilisateur " + p.getCn());
			logger.error(e.toString());
			throw new ToutaticeAnnuaireException("Erreur lors de la suppression de la surcharge sur la personne "+p.getCn()+". Impossible de supprimer l'attribut personSmdp");
		}
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#updateEmail(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#p.uid")
	public void updateEmail(Person p) throws ToutaticeAnnuaireException {
		try {
			Name dn = buildDn(p);
			Attribute attr = new BasicAttribute(email,p.getEmail());
			ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
			ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] {item});
			loggerModif.info("La personne "+p.getUid()+" a été modifiée");
		}
		catch(NamingException e)
		{ logger.error("Impossible de mettre à jour l'adresse email pour l'utilisateur "+p.getCn());
		logger.error(e.toString());
		throw new ToutaticeAnnuaireException("Erreur lors de la mise à jour de l'adresse email de la personne "+p.getCn());
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#personHasRole(fr.toutatice.outils.ldap.entity.Person, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean personHasRole(Person user,String cnRole) {
		ProfilDao profilDao = (ProfilDao) context.getBean("profilDao");
		RoleApplicatifDao roleApplicatifDao = (RoleApplicatifDao) context.getBean("roleApplicatifDao");
		boolean reponse = false;
		RoleApplicatif role = null;
		try {
		role = roleApplicatifDao.findByCn(cnRole); 
		if(role!=null){
			//recherche par filtre
			for(String filtre : role.getListeMemberURL()) {
				if(filtre.contains("#{organisation}")) {
					for(String rne : user.getListeRnes()){
						String filtre1 = filtre.replace("#{organisation}", rne);
						String filtreGlobal = "(&" + filtre1.concat(new EqualsFilter(id, user.getUid()).encode()) + ")";
						PersonAttributMapper personAttributMapper = new PersonAttributMapper();
						List<Person> liste = ldapTemplateLecture.search("", filtreGlobal, personAttributMapper);
						if (liste.size() == 1) 
						{reponse = true;}
					}
				}
			}
			//recherche par profil
			if(!reponse) {
				for(String profil : role.getListeProfils()) {
					Profil profilR = profilDao.findByDn(profil);
					if(profilR !=null){
						if(profilR.isMember(user.getDn())){
								reponse = true;
							}
					} 
				}
			}
			
			//recherche par uid
			if(!reponse) {
				if(role.isMemberExplicit(user.getDn())){
					reponse = true;
				}
			}
		}else{
			reponse=false;
		}
		
		} catch (ToutaticeAnnuaireException e) {
			reponse = false;
		}

		return reponse;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#personHasStructure(fr.toutatice.outils.ldap.entity.Person, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean personHasStructure(Person user,String rne) {
		Organisation organisation = (Organisation) context.getBean("organisation");
		boolean reponse = false;
		/*
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(profils, rne+"*"));
		filter.and(new EqualsFilter(id,user.getUid()));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();
		@SuppressWarnings("unchecked")
		List<Person> liste = ldapTemplateLecture.search("", filter.encode(), personAttributMapper);
		if (liste.size() > 0){
			reponse = true;
		}*/
		if (!(rne.trim().isEmpty()||rne.equals("*"))){
			Organisation org = organisation.findOrganisation(rne);
			if(org!=null){
				reponse = org.contientProfil(user.getListeProfils());
			}
		}
		return reponse;
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#getListePersonnesAyantProfilTrie(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("static-access")
	public List<Person> getListePersonnesAyantProfilTrie(String dnProfil, String critereTri) {
		

	     SearchControls searchControls = new SearchControls() ;
	     searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE ) ;
	        
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new EqualsFilter(this.profils, dnProfil));
		PersonAttributMapper personAttributMapper = new PersonAttributMapper();

		List<Person> liste = null;
		if (annuaireConfig.getSortingEnabled()) {
			AggregateDirContextProcessor processor = new AggregateDirContextProcessor();
			SortControlDirContextProcessor sorter = new SortControlDirContextProcessor(critereTri);
			processor.addDirContextProcessor(sorter);

			liste = ldapTemplateLecture.search("", filter.encode(),	searchControls, personAttributMapper, processor);
		} else {
			liste = ldapTemplateLecture.search("", filter.encode(),	searchControls, personAttributMapper);
			
			Collections.sort(liste, new PersonComparator());
		}
		
		return liste;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#create(fr.toutatice.outils.ldap.entity.Person)
	 */
	 @CacheEvict(value = "personByPrimaryKeyCache", key = "#p.uid")
	 public void create(Person p) throws ToutaticeAnnuaireException {
		  Name dn = buildDn(p);
		  try {
			  Attributes attr = buildAttributes(p);
			  ldapTemplateEcriture.bind(dn, null, attr);
		  } catch (NamingException e) {
				logger.error("Impossible de créér la personne "+ p.getUid());
				e.printStackTrace();
				throw new ToutaticeAnnuaireException("Erreur lors de la création de la personne "+p.getUid()+" dans l'annuaire");
			} 
	   }
	 
	 /* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#delete(fr.toutatice.outils.ldap.entity.Person)
	 */
	@CacheEvict(value = "personByPrimaryKeyCache", key = "#p.uid")
	   public void delete(Person p) throws ToutaticeAnnuaireException {
		   try {
	      ldapTemplateEcriture.unbind(buildDn(p));
		   } 		
			catch (NamingException e) {
				logger.error("La suppression de la personne "+p.getUid()+ " a échoué");
				throw new ToutaticeAnnuaireException("Erreur lors de la suppression de la personne "+p.getUid());
			}
	   }
	
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.PersonDao#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context = arg0;
	}
	
	

}
