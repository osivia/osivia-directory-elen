package fr.toutatice.outils.ldap.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;




/**
 * Classe permettant d'accéder à l'annuaire LDAP, de consulter 
 * et mettre à jour les informations concernant les applications.
 * Les attributs de cette classe sont les libellés des champs dans l'annuaire LDAP
 * Ils sont à renseigner dans un fichier properties et injectés dans cette classe via SPRING
 * @author aguihomat
 *
 */

@Repository
@Scope("singleton")
public class ProfilDao implements ApplicationContextAware {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	protected static final Log loggerCache = LogFactory.getLog("fr.toutatice.annuaire.cache");
	private static ApplicationContext context;
	
	@Autowired
	private PersonDao personDao;
	
	@Autowired
	@Qualifier("ldapTemplateEcriture")
	private LdapTemplate ldapTemplateEcriture;
	
	@Autowired
	@Qualifier("ldapTemplateLecture")
	private LdapTemplate ldapTemplateLecture;
	
	
	// attributs permettant de paramétrer le nom des champs de la base de données dans le fichier de config Spring
	
	private static String cn ="";
	private static String objectClass="";
	private static String owner="";
	private static String member="";
	private static String explicitMember="";
	private static String description="";
	private static String categorie;
	private static String sousCategorie;
	private static String BASE_DN="";
	private static String objectClassClasse="";
	private static String manager="";
	private static String explicitManager;
	private static String displayName;
	private static String filtreRecherche;
	private static String type;
	private static String peuplement;

	
	// Setters, obligatoires pour instanciation via le fichier de config Spring
	public void setCn(String s) {
		cn = s;	}
	public void setDisplayName(String s ) {
		displayName = s ;
	}
	public void setObjectClass(String s) {
		objectClass = s;	}
	public void setObjectClassClasse(String s) {
		objectClassClasse = s;	}
	public void setOwner(String s) {
		owner = s;	}
	public void setMember(String s) {
		member = s;	}
	public void setExplicitMember(String s) {
		explicitMember = s;	}
	public void setDescription(String s) {
		description = s;	}
	public void setCategorie(String s ) {
		categorie = s;	}
	public void setSousCategorie(String s ) {
		sousCategorie = s;	}
	public void setBASE_DN(String s) {
		BASE_DN = s;	}
	public void setManager(String s) {
		manager = s;	}
	public void setExplicitManager(String m) {
		explicitManager = m;	}
	public void setFiltreRecherche(String s){
		filtreRecherche=s;	}
	public void setType(String s){
		type=s;	}
	public void setPeuplement(String s){
		peuplement=s;	}
	
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		context = ctx;
	}
	public PersonDao getPersonDao() {
		return personDao;	}
	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;	}
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



	//Classe interne permettant de récupérer un objet Personne depuis l'annuaire
	private static class ProfilAttributMapper implements AttributesMapper {
		@SuppressWarnings("rawtypes")
		public Object mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {
			
			//instanciation d'un objet Person va Spring (ne pas utiliser le constructeur par défaut !)
			Profil p = (Profil)context.getBean("profil");
			
			p.setCn(attrs.get(cn).get().toString());
			

			Attribute attr = attrs.get(description);
			if (attr != null) {
				p.setDescription(attr.get().toString());			}
			else {
				p.setDescription("");
			}
			
			attr = attrs.get(displayName);
			if(attr!=null) {
				p.setDisplayName(attr.get().toString());
				if(p.getDisplayName().trim().isEmpty()){
					p.setDisplayName(p.getDescription());
				}
			}
			else {
				p.setDisplayName(p.getDescription());
			}
			
		
			attr = attrs.get(manager);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					p.getListeManagers().add((String)m.next());				}
			}
			
			attr = attrs.get(explicitManager);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					p.addExplicitManager((String) m.next());
				}
			}
			
			attr = attrs.get(member);
			if (attr != null) {
				p.setListeMembersAttributes(attr); 
				p.setListeMembers(new ArrayList<String>());
			}
			
			attr = attrs.get(explicitMember);
			if (attr != null) {
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					p.getListeExplicitMembers().add((String)m.next());				}
			}
			
			attr = attrs.get(filtreRecherche);
			if (attr != null) {
				p.setFiltreRecherche(attr.get().toString());
			}
			
			attr = attrs.get(type);
			if (attr != null) {
				p.setType(attr.get().toString());
			}
			
			attr = attrs.get(peuplement);
			if (attr != null) {
				String s = attr.get().toString();
				if(s.equalsIgnoreCase("IMPLICITE")){
					p.setPeuplement(Profil.typePeuplement.IMPLICITE);	
				}else{
					if(s.equalsIgnoreCase("EXPLICITE")){
						p.setPeuplement(Profil.typePeuplement.EXPLICITE);	
					}else{
						p.setPeuplement(Profil.typePeuplement.MIXTE);
					}
				}
				
			}
			if(p.getPeuplement()==null){
				p.setPeuplement(Profil.typePeuplement.MIXTE);
			}
			
			return p;
		}
	}


	protected Attributes buildAttributes(Profil p) throws ToutaticeAnnuaireException {
	      Attributes attrs = new BasicAttributes();
	      BasicAttribute ocattr = new BasicAttribute("objectclass");
	      ocattr.add("top");
	      ocattr.add(objectClass); 
	      attrs.put(ocattr);
	      
	      attrs.put(cn, p.getCn());
	      
	      if(p.getDescription()!=null){
	    	  attrs.put(description, p.getDescription());
	      }
	      if(p.getDisplayName()!=null){
	    	  attrs.put(displayName,p.getDisplayName());
	      }
	      if(p.getType()!=null){
	    	  attrs.put(type,p.getType());
	      }
	      if(p.getPeuplement()!=null){
	    	  attrs.put(peuplement,p.getPeuplement().toString());
	      }
	     
	    
	      if(p.getListeMembers()!=null && p.getListeMembers().size()>0){
		      BasicAttribute membersAttr = new BasicAttribute(member);
		      for (String s : p.getListeMembers())
		      {
		    	  membersAttr.add(s);
		      }
		      attrs.put(membersAttr);
	      }
	      
	      if(p.getListeManagers()!=null && p.getListeManagers().size()>0){
		      BasicAttribute managersAttr = new BasicAttribute(manager);
		      for (String s : p.getListeManagers())
		      {
		    	  managersAttr.add(s);
		      }
		      attrs.put(managersAttr);
	      }
	      
	      if(p.getListeExplicitManagers()!=null && p.getListeExplicitManagers().size()>0){
		      BasicAttribute explicitManagersAttr = new BasicAttribute(explicitManager);
		      for (String s : p.getListeExplicitManagers())
		      {
		    	  explicitManagersAttr.add(s);
		      }
		      attrs.put(explicitManagersAttr);
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
		dn.add("ou",categorie);
		dn.add("ou",sousCategorie);
		dn.add(cn, nom);
		return dn;
	}

	/**
	 * Construit le DN d'un profil
	 * @param profil profil dont on veut le DN
	 * @return DN du profil (sans la base de l'annuaire LDAP, définie dans le fichier properties)
	 */
	private Name buildDn(Profil profil) {
		return this.buildDn(profil.getCn());
	}
	
	public Profil newProfil(){
		return (Profil)context.getBean("profil");
	}
	
	/**
	 * Construit le DN complet d'un profil à partir de son nom
	 * @param cn nom du profil 
	 * @return  DN du profil (avec la base de l'annuaire LDAP, définie dans le fichier properties)
	 */
	public String buildFullDn(String cn) {
		if(cn.trim().isEmpty()){
			return null;
		} else {
			String dn= this.buildDn(cn).toString() + "," + BASE_DN;
			return dn;
		}
	}

	/**
	 * Recherche d'un profil par son nom
	 * @param cn nom exact du profil recherché
	 * @return profil correspondant au cn passé en paramètre
	 * @throws ToutaticeAnnuaireException 
	 */
	public Profil findByPrimaryKey(String cn)  {

		loggerCache.debug("profile : findByPrimaryKey/" + cn);

		Profil profil = (Profil)context.getBean("profil");
		if(!cn.trim().isEmpty()){
			Name dn = buildDn(cn);
			try {
				ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
				profil = (Profil) ldapTemplateLecture.lookup(dn, profilAttributMapper);		
			} catch (NamingException e) {
				logger.debug("Le profil "+cn+" n'a pas été retrouvé dans l'annuaire");
				profil=null;
			} 
		} else {
			profil = null;
		}
		return profil;
	}
	
	/**
	 * Recherche des profils dont fait partie une personne
	 * @param cnPerson nom de la personne sur laquelle porte la recherche
	 * @return liste des profils dont elle fait partie
	 */
	public List<Profil> getProfilByCn(String cnPerson) {
	
		loggerCache.debug("profile : getProfilByCn/" + cnPerson);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new LikeFilter(cn, cnPerson+"*"));
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		return liste;
	}
	
	public List<Profil> getProfilByRneNom(String rne, String cn) {

		loggerCache.debug("profile : getProfilByRneNom/" + rne + "/" + cn);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		if(!rne.equals("")) {
			filter.and(new LikeFilter(this.cn, rne+"*"));
		}
		if(!cn.equals("")) {
			filter.and(new LikeFilter(this.cn, "*"+cn+"*"));
		}
		
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		return liste;
	}
	
	/**
	 * Recherche des profils correspondant à un filtre LDAP
	 * @param filtre filtre LDAP
	 * @return liste des profils correspondant au filtre
	 */
	public List<Profil> getProfilByFiltre(String filtre) {

		loggerCache.debug("profile : getProfilByFiltre/" + filtre);

		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filtre, profilAttributMapper);
		return liste;
	}
	
	/**
	 * Recherche d'un profil grâce à son adresse dans l'annuaire
	 * @param dn adresse complète du profil (avec la base de l'annuaire)
	 * @return profil correspondant au dn passé en paramètre
	 * @throws ToutaticeAnnuaireException 
	 */
	public Profil findByDn(String dn) {
		
		loggerCache.debug("profile : findByDn/" + dn);

		Profil profil = (Profil)context.getBean("profil");
		try {
			ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();	
			DirContext DC = ldapTemplateLecture.getContextSource().getReadOnlyContext();
			String BASE_DN;
			BASE_DN = DC.getNameInNamespace();
			int i = dn.lastIndexOf(BASE_DN);
			if(i>0 && dn.length()>=i) {
				String subDn = dn.substring(0, i-1);
				profil = (Profil) ldapTemplateLecture.lookup(new DistinguishedName(subDn), profilAttributMapper);
			} else {
				profil = null;
			}
		} catch (NamingException e) {
			profil = null;
		}  catch (javax.naming.NamingException e) {
			profil = null;
		} 
	
		return profil;
	}
	
	/**
	 * Création d'un profil dans l'annuaire
	 * @param profil Profil à créér
	 * @throws ToutaticeAnnuaireException
	 */
	 public void create(Profil profil) throws ToutaticeAnnuaireException {
		  Name dn = buildDn(profil);
		  try {
			  Attributes attr = buildAttributes(profil);
			  ldapTemplateEcriture.bind(dn, null, attr);
		  } catch (NamingException e) {
				logger.error("Impossible de créér le profil "+ profil.getCn());
				e.printStackTrace();
				throw new ToutaticeAnnuaireException("Erreur lors de la création du profil "+profil.getCn()+" dans l'annuaire");
		  }
		  for(String dnMembre:profil.getListeMembers()){
			Person membre = personDao.findByDn(dnMembre);
			personDao.ajoutProfilViaMajProfil(membre, profil.getDn());
		  }
	   }
	 
	 /**
	  * Suppression d'un profil dans l'annuaire
	  * @param profil Profil à supprimer
	 * @throws ToutaticeAnnuaireException 
	  */
	   public void delete(Profil profil) throws ToutaticeAnnuaireException {
		   Profil profilLdap = this.findByPrimaryKey(profil.getCn());
		   try {
			   ldapTemplateEcriture.unbind(buildDn(profil));
		   } 		
			catch (NamingException e) {
				logger.error("La suppression du profil "+profil.getCn()+ " a échoué");
				throw new ToutaticeAnnuaireException("Erreur lors de la suppression du profil "+profil.getCn());
			}
			//Suppression du profil dans l'attribut ENTPersonProfil des anciens membres
			for(String dnMembre:profilLdap.getListeMembers()){
			Person membre = personDao.findByDn(dnMembre);
			personDao.supprimerProfilViaMajProfil(membre, profilLdap.getDn());
			}
	   }
	
	
	/**
	 * Mise à jour d'un profil
	 * Mise à jour des attributs nom, displayName, description, membres, membres explicites et managers
	 * @param profil à mettre à jour
	 * @throws ToutaticeAnnuaireException 
	 * @throws javax.naming.NamingException 
	 * @throws javax.naming.NamingException 
	 */

	public void updateProfil(Profil profilMaj) throws ToutaticeAnnuaireException{
		
		//Récupération du profil dans LDAP  pour comparaison
		Profil profilOld = this.findByPrimaryKey(profilMaj.getCn());
		
		Name dn = this.buildDn(profilMaj);
		DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
		
		context.setAttributeValue(cn, profilMaj.getCn());
		context.setAttributeValue(displayName,profilMaj.getDisplayName());
		context.setAttributeValue(description, profilMaj.getDescription());
		
		// Maj liste membres explicites
		profilMaj.setListeExplicitMembers(Helper.supprimerDoublonsCaseNonSensitive(profilMaj.getListeExplicitMembers()));
		Object[] explicitMembers = new Object[profilMaj.getListeExplicitMembers().size()];
		int i = 0;
		for (Object o : profilMaj.getListeExplicitMembers()) {
			explicitMembers[i] = o;
			i++;
		}
		context.setAttributeValues(explicitMember, explicitMembers);
		
		// Maj liste membres
		profilMaj.setListeMembers(Helper.supprimerDoublonsCaseNonSensitive(profilMaj.getListeMembers()));
		
			// vérification : tous les explicitMembres doivent aussi être déclarés members
			List<String> listeMembresLowerCase = new ArrayList<String>();
			for(String s : profilMaj.getListeMembers()) {
				listeMembresLowerCase.add(s.toLowerCase());
			}
			for(String s:profilMaj.getListeExplicitMembers()){
				if(!listeMembresLowerCase.contains(s.toLowerCase())){
					profilMaj.addMember(s);
				}
			}
		
		Object[] members = new Object[profilMaj.getListeMembers().size()];
		i = 0;
		for (Object o : profilMaj.getListeMembers()) {
			members[i] = o;
			i++;
		}
		context.setAttributeValues(member, members);
		
		// Maj liste managers explicites
		profilMaj.setListeExplicitManagers(Helper.supprimerDoublonsCaseNonSensitive(profilMaj.getListeExplicitManagers()));
		Object[] explicitManagers = new Object[profilMaj.getListeExplicitManagers().size()];
		i = 0;
		for (Object o : profilMaj.getListeExplicitManagers()) {
			explicitManagers[i] = o;
			i++;
		}
		context.setAttributeValues(explicitManager, explicitManagers);
		// Maj liste managers
		profilMaj.setListeManagers(Helper.supprimerDoublonsCaseNonSensitive(profilMaj.getListeManagers()));
		Object[] managers = new Object[profilMaj.getListeManagers().size()];
		i = 0;
		for (Object o : profilMaj.getListeManagers()) {
			managers[i] = o;
			i++;
		}
		context.setAttributeValues(manager, managers);
			
		
		ldapTemplateEcriture.modifyAttributes(context);
		
		//Mise à jour de l'attribut ENTPersonProfil des membres qui ont été rajoutés ou supprimés du profil
		this.updatePersonProfilDesMembres(profilMaj, profilOld);
	}
	
	/**
	 * Méthode définissant quels membres ont été ajoutés ou supprimés d'un profil en comparant le profil avant et après mise à jour
	 * et mettant à jour dans l'annuaire l'attribut ENTPersonProfil des personnes impactées
	 * @param profil
	 * @throws ToutaticeAnnuaireException 
	 * @throws javax.naming.NamingException 
	 */
	private void updatePersonProfilDesMembres(Profil profilMaj, Profil profilOld) throws ToutaticeAnnuaireException{
	
		List<String> membresSupprimes = new ArrayList<String>();
		List<String> membresAjoutes = new ArrayList<String>();
		
		List<String> listeMembresMaj = new ArrayList<String>();
		List<String> listeMembresOld = new ArrayList<String>();
		for(String s : profilMaj.getListeMembers()) {
			listeMembresMaj.add(s.toLowerCase());
		}
		for(String s : profilOld.getListeMembers()){
			listeMembresOld.add(s.toLowerCase());
		}
		
		for(String membre:profilOld.getListeMembers()) {
			if(! listeMembresMaj.contains(membre.toLowerCase())) {
				membresSupprimes.add(membre);
			}
		}
		for(String membre:profilMaj.getListeMembers()) {
			if(! listeMembresOld.contains(membre.toLowerCase())) {
				membresAjoutes.add(membre);
			}
		}
		
		// Mise à jour des personnes impactées
		for (String dnMembre : membresSupprimes) {
			try {
				Person membre = personDao.findByDn(dnMembre);
				personDao.supprimerProfilViaMajProfil(membre, profilOld.getDn());
			} catch (ToutaticeAnnuaireException e) {
				logger.error("Erreur lors de la mise à jour des profils de la personne " + dnMembre);
				e.printStackTrace();
			}
		}
		for (String dnMembre : membresAjoutes) {
			try {
				Person membre = personDao.findByDn(dnMembre);
				personDao.ajoutProfilViaMajProfil(membre, profilOld.getDn());
			} catch (ToutaticeAnnuaireException e) {
				logger.error("Erreur lors de la mise à jour des profils de la personne " + dnMembre);
				e.printStackTrace();
			}
		}
	
	}
	
	

	
	/**
	 * Méthode appelée lors de la mise à jour d'une personne par la classe personDAO: si on modifie la liste des profils d'une personne on doit ajouter/supprimer
	 * cette personne de la liste des membres des profils concernés
	 * @param dnProfil DN du profil à modifier
	 * @param dnMembre DN du membre à ajouter au profil
	 * @throws ToutaticeAnnuaireException
	 * @throws javax.naming.NamingException 
	 */
	public void ajoutMembreViaMajMembre(String dnProfil, String dnMembre) throws ToutaticeAnnuaireException, javax.naming.NamingException {
		Profil profilMaj = this.findByDn(dnProfil);
		profilMaj.addMember(dnMembre);
		//elimination des doublons
		profilMaj.setListeExplicitMembers(new ArrayList(new HashSet(profilMaj.getListeExplicitMembers())));
		Name dn = this.buildDn(profilMaj);
		if (profilMaj.getListeMembers().size() > 0) {
			Object[] members = new Object[profilMaj.getListeMembers().size()];
			int i = 0;
			for (Object o : profilMaj.getListeMembers()) {
				members[i] = o;
				i++;
			}
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(member, members);
			ldapTemplateEcriture.modifyAttributes(context);
		} else 
		// Si il n'y a aucun membre on supprime l'attribut
		{
			if(profilMaj.getListeMembers().size() > 0) 
		 	{
				BasicAttribute attrMembers = new BasicAttribute(member, false);
				attrMembers.add(null);
				ModificationItem item = new ModificationItem(
						DirContext.REMOVE_ATTRIBUTE, attrMembers);
				ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
		 	}
		}
	}
	
	/**
	 * Méthode appelée lors de la mise à jour d'une personne par la classe personDAO: si on modifie la liste des profils d'une personne on doit ajouter/supprimer
	 * cette personne de la liste des membres des profils concernés
	 * @param dnProfil DN du profil à modifier
	 * @param dnMembre DN du membre à supprimer du profil
	 * @throws ToutaticeAnnuaireException
	 * @throws javax.naming.NamingException 
	 */
	public void supprimerMembreViaMajMembre(String dnProfil, String dnMembre) throws ToutaticeAnnuaireException, javax.naming.NamingException {
		Profil profilMaj = this.findByDn(dnProfil);
		profilMaj.removeMember(dnMembre);
		//si le membre était un membre explicite on le retire aussi de cette liste
		profilMaj.removeExplicitMember(dnMembre);
		//elimination des doublons
		profilMaj.setListeExplicitMembers(new ArrayList(new HashSet(profilMaj.getListeExplicitMembers())));
		Name dn = this.buildDn(profilMaj);
		DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
		
		Object[] explicitMembers = new Object[profilMaj.getListeExplicitMembers().size()];
		int i = 0;
		for (Object o : profilMaj.getListeExplicitMembers()) {
			explicitMembers[i] = o;
			i++;
		}
		context.setAttributeValues(explicitMember, explicitMembers);
		
		Object[] members = new Object[profilMaj.getListeMembers().size()];
		i = 0;
		for (Object o : profilMaj.getListeMembers()) {
			members[i] = o;
			i++;
		}
		context.setAttributeValues(member, members);
		
		ldapTemplateEcriture.modifyAttributes(context);
	}
	
	/**
	 * Recherche de tous les profils de la personne passée en paramètre
	 * @param p
	 * @return
	 */
	public List<Profil> findProfilsPerson(Person p){
		
		loggerCache.debug("profile : findProfilsPerson/" + p.getUid());

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new EqualsFilter(this.member, p.getDn()));
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		String s = "ou="+this.sousCategorie+",ou="+this.categorie;
		List<Profil> liste = ldapTemplateLecture.search(s, filter.encode(), profilAttributMapper);
		return liste;
	}
	
	public List<Profil> findProfilsPersonByOrga(Person p, String idOrga){
		
		loggerCache.debug("profile : findProfilsPerson/" + p.getUid() + "/" + idOrga);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new LikeFilter(this.cn, idOrga+"*"));
		filter.and(new EqualsFilter(this.member, p.getDn()));
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		String s = "ou="+this.sousCategorie+",ou="+this.categorie;
		List<Profil> liste = ldapTemplateLecture.search(s, filter.encode(), profilAttributMapper);
		return liste;
	}
	
	/**
	 * Recherche de tous les profils managés explicitement par une personne
	 * @param p
	 * @return
	 */
	public List<Profil> findProfilsGeresPerson(Person p){

		loggerCache.debug("profile : findProfilsGeresPerson/" + p.getUid());

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		OrFilter orFilter = new OrFilter();
		orFilter.or(new EqualsFilter(this.explicitManager,p.getDn()));
		orFilter.or(new EqualsFilter(this.manager,p.getDn()));
		for(String dnProfil:p.getListeProfils()){
			orFilter.or(new EqualsFilter(this.explicitManager,dnProfil));
		}
		for(String dnProfil:p.getListeProfils()){
			orFilter.or(new EqualsFilter(this.manager,dnProfil));
		}
		filter.and(orFilter);		
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		return liste;
	
	}
	
	public List<Profil> findProfilsGeresPersonParOrga(Person p, String idOrga){

		loggerCache.debug("profile : findProfilsGeresPersonParOrga/" + p.getUid() + "/" + idOrga);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new LikeFilter(this.cn, idOrga+"*"));
		OrFilter orFilter = new OrFilter();
		orFilter.or(new EqualsFilter(this.explicitManager,p.getDn()));
		orFilter.or(new EqualsFilter(this.manager,p.getDn()));
		for(String dnProfil:p.getListeProfils()){
			orFilter.or(new EqualsFilter(this.explicitManager,dnProfil));
		}
		for(String dnProfil:p.getListeProfils()){
			orFilter.or(new EqualsFilter(this.manager,dnProfil));
		}
		
		filter.and(orFilter);		
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		return liste;
		
	}
	
	/**
	 * Recherche de tous les profils managés implicitement par une personne
	 * @param p
	 * @return
	 */
	public List<Profil> findProfilsGeresImplicitementPerson(Person p){
		
		loggerCache.debug("profile : findProfilsGeresImplicitementPerson/" + p.getUid());

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		OrFilter orFilter = new OrFilter();
		orFilter.or(new EqualsFilter(this.manager,p.getDn()));
		for(String dnProfil:p.getListeProfils()){
			orFilter.or(new EqualsFilter(this.manager,dnProfil));
		}
		filter.and(orFilter);	
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		return liste;
	}
	
	public List<Profil> findProfilsGeresImplicitementPersonParOrga(Person p, String idOrga){
		
		loggerCache.debug("profile : findProfilsGeresImplicitementPersonParOrga/" + p.getUid() + "/" + idOrga);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new LikeFilter(this.cn, idOrga+"*"));
		OrFilter orFilter = new OrFilter();
		orFilter.or(new EqualsFilter(this.manager,p.getDn()));
		for(String dnProfil:p.getListeProfils()){
			orFilter.or(new EqualsFilter(this.manager,dnProfil));
		}
		filter.and(orFilter);	
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		return liste;
	}
	
	/**
	 * Recherche de tous les profils managés explicitement par une personne
	 * @param p
	 * @return
	 */
	public List<Profil> findProfilsGeresExplicitementPerson(Person p){
		
		loggerCache.debug("profile : findProfilsGeresExplicitementPerson/" + p.getUid());

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		OrFilter orFilter = new OrFilter();
		orFilter.or(new EqualsFilter(this.explicitManager,p.getDn()));
		for(String dnProfil:p.getListeProfils()){
			orFilter.or(new EqualsFilter(this.explicitManager,dnProfil));
		}
		filter.and(orFilter);		
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		return liste;
	}
	
	public List<Profil> findProfilsGeresExplicitementPersonParOrga(Person p, String idOrga){
		
		loggerCache.debug("profile : findProfilsGeresExplicitementPersonParOrga/" + p.getUid() + "/" + idOrga);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new LikeFilter(this.cn, idOrga+"*"));
		OrFilter orFilter = new OrFilter();
		orFilter.or(new EqualsFilter(this.explicitManager,p.getDn()));
		for(String dnProfil:p.getListeProfils()){
			orFilter.or(new EqualsFilter(this.explicitManager,dnProfil));
		}
		filter.and(orFilter);		
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		return liste;
	}
	

	public Profil findClasseEleve(Person eleve) {
	
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new EqualsFilter("objectclass", objectClassClasse));
		filter.and(new LikeFilter(member, eleve.getDn()+"*"));
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("", filter.encode(), profilAttributMapper);
		if (liste.size() > 1) {
			logger.info("l'élève "+eleve.getDisplayName()+" a plus d'une classe associée !");
		}
		
		if(liste.size()<1){
			return null;
		}else{
			return liste.get(0);
		}
		
		
	}
			
	/**
	 * Recherche de tous les profils de la personne passée en paramètre
	 * @param p
	 * @return
	 */
	public boolean isMember(Profil profil, String dnUser){

		loggerCache.debug("profile : isMember/" + profil.getCn() + "/" + dnUser);

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClass));
		filter.and(new EqualsFilter(this.member, dnUser));
		filter.and(new EqualsFilter(this.cn, profil.getCn()));
		ProfilAttributMapper profilAttributMapper = new ProfilAttributMapper();
		@SuppressWarnings("unchecked")
		List<Profil> liste = ldapTemplateLecture.search("ou="+this.sousCategorie+",ou="+this.categorie, filter.encode(), profilAttributMapper);
		if(liste.size()<1){
			return false;}
		else{
			return true;
		}
	}
	
	
	
    
}
	


