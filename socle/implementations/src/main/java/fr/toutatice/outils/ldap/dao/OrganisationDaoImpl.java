package fr.toutatice.outils.ldap.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

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
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Repository
@Qualifier("organisationDao")
@Scope("singleton")

public class OrganisationDaoImpl implements ApplicationContextAware, OrganisationDao{

protected static ApplicationContext context;
protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");

	@Autowired
	@Qualifier("ldapTemplateEcriture")
	protected LdapTemplate ldapTemplateEcriture;
	
	@Autowired
	@Qualifier("ldapTemplateLecture")
	protected LdapTemplate ldapTemplateLecture;

	protected static String id;
	protected static String description;
	protected static String displayName;
	protected static String localisation;
	protected static String postalCode;
	protected static String street;
	protected static String telephoneNumber;
	protected static String faxNumber;
	protected static String classObjet ="";
	private   static String categorieLDAP;
	protected static String seeAlso;
	protected static String manager="";
	protected static String explicitManager;
	protected static String profils ="";
	private   static String BASE_DN="";
	

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setId(java.lang.String)
	 */
	public void setId(String s) {
		id = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setDescription(java.lang.String)
	 */
	public void setDescription(String s) {
		description = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String s) {
		displayName = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setLocalisation(java.lang.String)
	 */
	public void setLocalisation(String s) {
		localisation = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setPostalCode(java.lang.String)
	 */
	public void setPostalCode(String s){
		postalCode = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setStreet(java.lang.String)
	 */
	public void setStreet(String s) {
		street = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setTelephoneNumber(java.lang.String)
	 */
	public void setTelephoneNumber(String s){
		telephoneNumber = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setFaxNumber(java.lang.String)
	 */
	public void setFaxNumber(String s){
		faxNumber = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setSeeAlso(java.lang.String)
	 */
	public void setSeeAlso(String s) {
		seeAlso = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setClasseObjet(java.lang.String)
	 */
	public void setClasseObjet(String s) {
		classObjet = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setCategorieLDAP(java.lang.String)
	 */
	public void setCategorieLDAP(String s) {
		categorieLDAP = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setProfils(java.lang.String)
	 */
	public void setProfils(String s) {
		profils = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setManager(java.lang.String)
	 */
	public void setManager(String s) {
		manager = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setExplicitManager(java.lang.String)
	 */
	public void setExplicitManager(String m) {
		explicitManager = m;
	}
	public static String getId() {
		return id;
	}
	public static String getDescription() {
		return description;
	}
	public static String getDisplayName(){
		return displayName;
	}
	public static String getLocalisation() {
		return localisation;
	}
	public static String getPostalCode(){
		return postalCode;
	}
	public static String getSeeAlso() {
		return seeAlso;
	}
	public static String getManager() {
		return manager;
	}
	public static String getExplicitManager() {
		return explicitManager;
	}
	public static String getProfils() {
		return profils;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#getLdapTemplateEcriture()
	 */
	public LdapTemplate getLdapTemplateEcriture() {
		return ldapTemplateEcriture;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setLdapTemplateEcriture(org.springframework.ldap.core.LdapTemplate)
	 */
	public void setLdapTemplateEcriture(LdapTemplate ldapTemplateEcriture) {
		this.ldapTemplateEcriture = ldapTemplateEcriture;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#getLdapTemplateLecture()
	 */
	public LdapTemplate getLdapTemplateLecture() {
		return ldapTemplateLecture;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setLdapTemplateLecture(org.springframework.ldap.core.LdapTemplate)
	 */
	public void setLdapTemplateLecture(LdapTemplate ldapTemplateLecture) {
		this.ldapTemplateLecture = ldapTemplateLecture;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setBASE_DN(java.lang.String)
	 */
	public void setBASE_DN(String s){
		BASE_DN=s;	}

	// Classe interne permettant de récupérer une application dans l'annuaire
	protected static class OrganisationAttributMapper implements AttributesMapper {
		
		
		public Object mapFromAttributes(Attributes attrs)
		throws javax.naming.NamingException {

	// instanciation d'un objet Organisation via Spring (il ne faut pas
	// utiliser le constructeur par défaut !)
			Organisation org = (Organisation) context.getBean("organisation");
			org = (Organisation) OrganisationDaoImpl.valoriser(org,attrs);
			return org;
		}
	}
		
		@SuppressWarnings("rawtypes")
		public static Object valoriser(Organisation org, Attributes attrs) throws javax.naming.NamingException {
			 
			Attribute attr;
			
			org.setId(attrs.get(id).get().toString());
	
			if (attrs.get(description) != null)
				{ org.setDescription(attrs.get(description).get().toString());}
			else {
				org.setDescription("");
			}
			
			if (attrs.get(displayName) != null)
			{ org.setDisplayName(attrs.get(displayName).get().toString());}
			else {
				org.setDisplayName("");
			}
			
			if (attrs.get(localisation) != null)
			{ org.setLocalisation(attrs.get(localisation).get().toString());}
			else {
				org.setLocalisation("");
			}
			if(attrs.get(postalCode)!=null){
				org.setCodePostal(attrs.get(postalCode).get().toString());}
			else{
				org.setCodePostal("");
			}
			if (attrs.get(street) != null)
			{ org.setAdresse(attrs.get(street).get().toString());}
			else {
				org.setAdresse("");
			}
			if (attrs.get(telephoneNumber) != null)
			{ org.setTelephone(attrs.get(telephoneNumber).get().toString());}
			else {
				org.setTelephone("");
			}
			if (attrs.get(faxNumber) != null)
			{ org.setNumeroFax(attrs.get(faxNumber).get().toString());}
			else {
				org.setNumeroFax("");
			}
			
			if (attrs.get(seeAlso) != null)
			{ org.setSeeAlso(attrs.get(seeAlso).get().toString());}
			else {
				org.setSeeAlso("");
			}
			
			attr = attrs.get(profils);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					org.addProfil((String)m.next());
				}
			}
			
			attr = attrs.get(manager);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					org.addManager((String)m.next());
				}
			}
			
			attr = attrs.get(explicitManager);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					org.addExplicitManager((String) m.next());
				}
			}

			return org;
		}
	

	protected Attributes buildAttributes(Organisation org) {
	      Attributes attrs = new BasicAttributes();
	      BasicAttribute ocattr = new BasicAttribute("objectclass");
	      ocattr.add("top");
	      ocattr.add("organizationalUnit");
	      ocattr.add("ENTOrganisation");
	      ocattr.add("ENTStructure");
	      attrs.put(ocattr);
	      attrs.put(id, org.getId());
	      if(org.getDescription()!=null){
	    	  attrs.put(description, org.getDescription());
	      }
	      if(org.getLocalisation()!=null){
	    	  attrs.put(localisation,org.getLocalisation());
	      }
	      if(org.getCodePostal()!=null){
	    	  attrs.put(postalCode,org.getCodePostal());
	      }
	      if(org.getAdresse()!=null){
	    	  attrs.put(street,org.getAdresse());
	      }
	      if(org.getTelephone()!=null){
	    	  attrs.put(telephoneNumber,org.getTelephone());
	      }
	      if(org.getNumeroFax()!=null){
	    	  attrs.put(faxNumber,org.getNumeroFax());
	      }
	      if(org.getSeeAlso()!=null){
	    	  attrs.put(seeAlso,org.getSeeAlso());
	      }
	      if(org.getListeProfils()!=null && org.getListeProfils().size()>0){
		      BasicAttribute profilsAttr = new BasicAttribute(profils);
		      for (String s : org.getListeProfils())
		      {
		    	  profilsAttr.add(s);
		      }
		      attrs.put(profilsAttr);
	      }
	  
	      return attrs;
	}
	

	/**
	 * Construit le DN d'un profil à partir de son nom
	 * @param nom nom du profil dont on veut le DN
	 * @return DN du profil (sans la base de l'annuaire LDAP, définie dans le fichier properties)
	 */
	private Name buildDn(String nom) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou",categorieLDAP);
		dn.add(id, nom);
		return dn;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#buildFullDn(java.lang.String)
	 */
	public String buildFullDn(String rne) {
		if(rne.trim().isEmpty()){
			return null;
		} else {
			String dn= this.buildDn(rne).toString() + "," + BASE_DN;
			return dn;
		}
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#findByPrimaryKey(java.lang.String)
	 */
	@Cacheable(key = "#identifiant", value = { "organisationByPrimaryKeyCache" })
	public Organisation findByPrimaryKey(String identifiant) {
		Organisation org = null;
		
		if (identifiant != null && !identifiant.trim().isEmpty()) {
			AndFilter filter = new AndFilter();
			filter.and(new EqualsFilter("objectclass", classObjet));
			filter.and(new EqualsFilter(id, identifiant));
			OrganisationAttributMapper strAttributMapper = new OrganisationAttributMapper();
			List<Organisation> liste = ldapTemplateLecture.search("", filter.encode(),SearchControls.SUBTREE_SCOPE ,strAttributMapper);
			if (liste.size()==1){
				org = liste.get(0);
			} 
		}
		return org;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#findByDn(java.lang.String)
	 */
	public Organisation findByDn(String dn) throws ToutaticeAnnuaireException {
		Organisation org = (Organisation) context.getBean("organisation");
		try {
			OrganisationAttributMapper organisationAttributMapper = new OrganisationAttributMapper();
			DirContext DC = ldapTemplateLecture.getContextSource().getReadOnlyContext();
			
			int i = dn.lastIndexOf(BASE_DN);
			String subDn="";
			if(i>0 && dn.length()>i){
				subDn = dn.substring(0, i-1);
				org = (Organisation) ldapTemplateLecture.lookup(new DistinguishedName(subDn), organisationAttributMapper);
			}else{
				org=null;
			}
		} catch (org.springframework.ldap.NameNotFoundException e) {
			logger.warn("Recherche d'une organisation dans l'annuaire : l'identifiant n'existe pas " + dn);
			org=null;
		} 
		return org;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#findAllOrganisations()
	 */
	@SuppressWarnings("unchecked")
	public List<Organisation> findAllOrganisations() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(id, "*"));
		OrganisationAttributMapper strAttributMapper = new OrganisationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(),SearchControls.SUBTREE_SCOPE ,strAttributMapper);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#findOrganisationByPrefixe(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Organisation> findOrganisationByPrefixe(String prefixe) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		filter.and(new LikeFilter(id, prefixe+"*"));
		OrganisationAttributMapper strAttributMapper = new OrganisationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(),SearchControls.SUBTREE_SCOPE ,strAttributMapper);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#findOrganisationsMultiCriteres(java.lang.String, java.lang.String)
	 */
	public List<Organisation> findOrganisationsMultiCriteres(String rne, String nom){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		if(!rne.trim().isEmpty()){
			filter.and(new LikeFilter(id, rne+"*"));
		}
		if(!nom.trim().isEmpty()){
			filter.and(new LikeFilter(displayName, "*"+nom+"*"));
		}
		OrganisationAttributMapper orgAttributMapper = new OrganisationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(),SearchControls.SUBTREE_SCOPE ,orgAttributMapper);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#findOrganisationPersonneByProfil(fr.toutatice.outils.ldap.entity.Person)
	 */
	public List<Organisation> findOrganisationPersonneByProfil(Person p){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		OrFilter orFilter = new OrFilter();
		for(String dnProfil : p.getListeProfils()){
			orFilter.or(new EqualsFilter(profils,dnProfil));
		}
		filter.and(orFilter);
		OrganisationAttributMapper orgAttributMapper = new OrganisationAttributMapper();
		return ldapTemplateLecture.search("", filter.encode(),SearchControls.SUBTREE_SCOPE ,orgAttributMapper);	
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#findFromFiltre(java.lang.String, java.lang.String)
	 */
	public List<Organisation> findFromFiltre(String filtre, String critereTri){
		AggregateDirContextProcessor processor = new AggregateDirContextProcessor() ;
	    SortControlDirContextProcessor sorter = new SortControlDirContextProcessor(critereTri) ;
	    processor.addDirContextProcessor( sorter ) ;
	    SearchControls searchControls = new SearchControls() ;
	    searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE ) ;
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjet));
		
		String filtreGlobal;
		if (!filtre.trim().isEmpty()){
			filtreGlobal = "(&" + filtre.concat(filter.encode()) + ")";
		}
		else{
			filtreGlobal= filter.encode();
		}
		OrganisationAttributMapper orgAttributMapper = new OrganisationAttributMapper();
		return ldapTemplateLecture.search("",filtreGlobal, searchControls, orgAttributMapper, processor);
	
	}
	
/* (non-Javadoc)
 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#prepareContextForUpdate(org.springframework.ldap.core.DirContextOperations, fr.toutatice.outils.ldap.entity.Organisation)
 */
public DirContextOperations prepareContextForUpdate(DirContextOperations context, Organisation orga){
	
	Organisation orgaRef = this.findByPrimaryKey(orga.getId());
	
	if (!orgaRef.getDisplayName().equals(orga.getDisplayName())) {
		context.setAttributeValue(displayName, orga.getDisplayName());
	}
	if (!orgaRef.getDescription().equals(orga.getDescription())) {
		context.setAttributeValue(description, orga.getDescription());
	}
	if (!orgaRef.getLocalisation().equals(orga.getLocalisation())) {
		context.setAttributeValue(localisation, orga.getLocalisation());
	}
	if(!orgaRef.getCodePostal().equals(orga.getCodePostal())){
		context.setAttributeValue(postalCode, orga.getCodePostal());
	}
	if(!orgaRef.getAdresse().equals(orga.getAdresse())){
		context.setAttributeValue(street, orga.getAdresse());
	}
	if(!orgaRef.getTelephone().equals(orga.getTelephone())){
		context.setAttributeValue(telephoneNumber, orga.getTelephone());
	}
	if(!orgaRef.getNumeroFax().equals(orga.getNumeroFax())){
		context.setAttributeValue(faxNumber, orga.getNumeroFax());
	}
	
	
	// Maj liste profils
	orga.setListeProfils(Helper.supprimerDoublonsCaseNonSensitive(orga.getListeProfils()));
	Object[] profils = new Object[orga.getListeProfils().size()];
	int i = 0;
	for (Object o : orga.getListeProfils()) {
		profils[i] = o;
		i++;
	}
	context.setAttributeValues(OrganisationDaoImpl.profils, profils);
	
	// Maj liste managers explicites
	orga.setListeExplicitManagers(Helper.supprimerDoublonsCaseNonSensitive(orga.getListeExplicitManagers()));
	Object[] explicitManagers = new Object[orga.getListeExplicitManagers().size()];
	i = 0;
	for (Object o : orga.getListeExplicitManagers()) {
		explicitManagers[i] = o;
		i++;
	}
	context.setAttributeValues(explicitManager, explicitManagers);

	// Maj liste managers
	orga.setListeManagers(Helper.supprimerDoublonsCaseNonSensitive(orga.getListeManagers()));
	Object[] managers = new Object[orga.getListeManagers().size()];
	i = 0;
	for (Object o : orga.getListeManagers()) {
		managers[i] = o;
		i++;
	}
	context.setAttributeValues(manager, managers);
	
	return context;
}
	
/* (non-Javadoc)
 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#update(fr.toutatice.outils.ldap.entity.Organisation)
 */
	@CacheEvict(value = "organisationByPrimaryKeyCache", key = "#orga.id")
	public void update(Organisation orga) {
		
		Name dn = this.buildDn(orga.getId());
		DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
		
		context = this.prepareContextForUpdate(context,orga);

		ldapTemplateEcriture.modifyAttributes(context);
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.OrgansiationDao#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext ctx)
	throws BeansException {
		context = ctx;

}
	
}
