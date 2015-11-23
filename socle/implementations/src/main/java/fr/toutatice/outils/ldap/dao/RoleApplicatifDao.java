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
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.entity.RoleApplicatif;
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
public class RoleApplicatifDao implements ApplicationContextAware {
	
		protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	
		private static ApplicationContext context;
		
		@Autowired
		@Qualifier("ldapTemplateEcriture")
		private LdapTemplate ldapTemplateEcriture;
		
		@Autowired
		@Qualifier("ldapTemplateLecture")
		private LdapTemplate ldapTemplateLecture;

		// attributs permettant de paramétrer le nom des champs de la base de
		// données dans le fichier de config Spring

		private static String nom;
		private static String description;
		private static String owner;
		private static String membres;
		private static String profilsApplicatifs;
		private static String categorie;
		private static String sousCategorie;
		private static String classeObjet;
		private static String BASE_DN;
		private static String memberURL;
		private static String manager;
		private static String explicitManager;
		private static String displayName;
		private static String filtreRecherche;
		private static String type;


		// Setters, obligatoires pour instanciation va le fichier de config Spring
		
		public void setNom(String s ) {
			nom = s ;
		}
		public void setDisplayName(String s ) {
			displayName = s ;
		}
		public void setDescription(String s ) {
			description = s ;
		}
		public void setOwner(String s ) {
			owner = s ;
		}
		public void setMembres(String s) {
			membres = s ;
		}
		public void setProfilsApplicatifs(String s ) {
			profilsApplicatifs = s;
		}
		public void setCategorie(String s ) {
			categorie = s;
		}
		public void setSousCategorie(String s ) {
			sousCategorie = s;
		}
		public void setClasseObjet(String s ) {
			classeObjet = s;
		}
		public void setBASE_DN(String s) {
			BASE_DN = s;
		}
		public void setApplicationContext(ApplicationContext ctx) throws BeansException {
			context = ctx;
		}
		public void setMemberURL(String s) {
			memberURL = s;
		}
		public void setManager(String s) {
			manager = s;
		}
		public void setExplicitManager(String m) {
			explicitManager = m;
		}
		public void setFiltreRecherche(String s){
			filtreRecherche=s;
		}
		public void setType(String s){
			type=s;
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


		// Classe interne permettant de récupérer un role applicatif dans l'annuaire
		private static class RoleApplicatifAttributMapper implements AttributesMapper {
			@SuppressWarnings("rawtypes")
			public Object mapFromAttributes(Attributes attrs)
					throws javax.naming.NamingException {

				// instanciation d'un objet Application via Spring (il ne faut pas
				// utiliser le constructeur par défaut !)
				RoleApplicatif role = (RoleApplicatif) context.getBean("roleApplicatif");

					role.setCn(attrs.get(nom).get().toString());
					
					
					
					Attribute attr = attrs.get(description);
					if (attr != null) {
						role.setDescription(attr.get().toString());
					}
					else {
						role.setDescription("");
					}
					
					attr = attrs.get(displayName);
					if(attr!=null) {
						role.setDisplayName(attr.get().toString());
						if(role.getDisplayName().trim().isEmpty()){
							role.setDisplayName(role.getDescription());
						}
					}
					else {
						role.setDisplayName(role.getDescription());
					}
					
					attr = attrs.get(type);
					if (attr != null) {
						role.setType(attr.get().toString());
					}
					else {
						role.setType("");
					}
					
					attr = attrs.get(filtreRecherche);
					if (attr != null) {
						role.setFiltreRecherche(attr.get().toString());
					}
					
					
					attr = attrs.get(owner);
					if (attr != null) {
						NamingEnumeration m = (attr.getAll());
						while (m.hasMore()) {
							role.addOwner((String) m.next());
						}
					}
					
	
					attr = attrs.get(membres);
					if (attr != null) {
						NamingEnumeration m = (attr.getAll());
						while (m.hasMore()) {
							role.addMember((String) m.next());
						}
					}
					
					attr = attrs.get(manager);
					if (attr != null) {
						NamingEnumeration m = (attr.getAll());
						while (m.hasMore()) {
							role.addManager((String) m.next());
						}
					}
					
					attr = attrs.get(explicitManager);
					if (attr != null) {
						NamingEnumeration m = (attr.getAll());
						while (m.hasMore()) {
							role.addExplicitManager((String) m.next());
						}
					}
					
					attr = attrs.get(profilsApplicatifs);
					if (attr != null) {
						NamingEnumeration m = (attr.getAll());
						while (m.hasMore()) {
							role.addProfil((String) m.next());
						}
					}
					
					attr = attrs.get(memberURL);
					if (attr != null) {
						NamingEnumeration m = (attr.getAll());
						while (m.hasMore()) {
							role.addMemberURL((String) m.next());
						}
					}

				return role;
			}
		}
		
		
		
		protected Attributes buildAttributes(RoleApplicatif role) throws ToutaticeAnnuaireException {
		      Attributes attrs = new BasicAttributes();
		      BasicAttribute ocattr = new BasicAttribute("objectclass");
		      ocattr.add("top");
		      ocattr.add(classeObjet); 
		      attrs.put(ocattr);
		      
		      attrs.put(nom, role.getCn());
		      
		      if(role.getDescription()!=null){
		    	  attrs.put(description, role.getDescription());
		      }
		      if(role.getDisplayName()!=null){
		    	  attrs.put(displayName,role.getDisplayName());
		      }
		      if(role.getType()!=null){
		    	  attrs.put(type,role.getType());
		      }
		     
		     
		    
		      if(role.getListeMembers()!=null && role.getListeMembers().size()>0){
			      BasicAttribute membersAttr = new BasicAttribute(membres);
			      for (String s : role.getListeMembers())
			      {
			    	  membersAttr.add(s);
			      }
			      attrs.put(membersAttr);
		      }
		      
		      if(role.getListeProfils()!=null && role.getListeProfils().size()>0){
			      BasicAttribute profilsAttr = new BasicAttribute(profilsApplicatifs);
			      for (String s : role.getListeProfils())
			      {
			    	  profilsAttr.add(s);
			      }
			      attrs.put(profilsAttr);
		      }
		      
		      if(role.getListeMemberURL()!=null && role.getListeMemberURL().size()>0){
			      BasicAttribute memberUrlAttr = new BasicAttribute(memberURL);
			      for (String s : role.getListeMemberURL())
			      {
			    	  memberUrlAttr.add(s);
			      }
			      attrs.put(memberUrlAttr);
		      }
		      
		      if(role.getListeManagers()!=null && role.getListeManagers().size()>0){
			      BasicAttribute managersAttr = new BasicAttribute(manager);
			      for (String s : role.getListeManagers())
			      {
			    	  managersAttr.add(s);
			      }
			      attrs.put(managersAttr);
		      }
		      
		      if(role.getListeExplicitManagers()!=null && role.getListeExplicitManagers().size()>0){
			      BasicAttribute explicitManagersAttr = new BasicAttribute(explicitManager);
			      for (String s : role.getListeExplicitManagers())
			      {
			    	  explicitManagersAttr.add(s);
			      }
			      attrs.put(explicitManagersAttr);
		      }
		      
	  
		      return attrs;
		}
		

		public RoleApplicatif getNewRole(){
			return (RoleApplicatif)context.getBean("roleApplicatif");
		}

		/**
		 * Construit le DN d'un role applicatif à partir de son id
		 * @param cn identifiant du role appliactif dont on veut le DN
		 * @return DN du role applicatif (sans la base de l'annuaire LDAP, définie dans le fichier properties)
		 */
		private Name buildDn(String cn) {
			logger.debug("entree dans la methode roleApplicatifDAO.buildDn(cn)");
			DistinguishedName dn = new DistinguishedName();
			dn.add("ou",categorie);
			dn.add("ou",sousCategorie);
			dn.add(nom, cn);
			return dn;
		}

		/**
		 * 	Construit le DN d'un role applicatif	
		 * @param role role applicatif dont on veut le DN
		 * @return DN du role applicatif (sans la base de l'annuaire LDAP, définie dans le fichier properties)
		 */
		private Name buildDn(RoleApplicatif role) {
			logger.debug("entree dans la methode roleApplicatifDAO.buildDn(role)");
			return buildDn(role.getCn());
		}
		
		/**
		 * 	Construit le DN complet d'un role applciatif à partir de son nom
		 * @param cn nom du role applicatif
		 * @return DN complet du role applicatif (avec la base de l'annuaire LDAP, défini dans le fichier properties)
		 */
		public String buildFullDn(String cn)
		{
			if(cn.trim().isEmpty()){
				return null;
			} else {
				String dn= this.buildDn(cn).toString() + "," + BASE_DN;
				return dn;
			}
		}
		
		/**
		 * Création d'un role applicatif dans l'annuaire
		 * @param role Role Applicatif à créér
		 * @throws ToutaticeAnnuaireException
		 */
		 public void create(RoleApplicatif role) throws ToutaticeAnnuaireException {
			  Name dn = buildDn(role);
			  try {
				  Attributes attr = buildAttributes(role);
				  ldapTemplateEcriture.bind(dn, null, attr);
			  } catch (NamingException e) {
					logger.error("Impossible de créér le role "+ role.getCn());
					e.printStackTrace();
					throw new ToutaticeAnnuaireException("Erreur lors de la création du role "+role.getCn()+" dans l'annuaire");
			  }		
		   }
		 
		 
		 /**
		  * Suppression d'un role dans l'annuaire
		  * @param role Role Applicatif à supprimer
		 * @throws ToutaticeAnnuaireException 
		  */
		   public void delete(RoleApplicatif role) throws ToutaticeAnnuaireException {
			 
			   try {
				   ldapTemplateEcriture.unbind(buildDn(role));
			   } 		
				catch (NamingException e) {
					logger.error("La suppression du role "+role.getCn()+ " a échoué");
					throw new ToutaticeAnnuaireException("Erreur lors de la suppression du role "+role.getCn());
				}
			
		   }

		/**
		 * Recherche de la liste des roles applicatifs d'une personne
		 * @param personne personne dont on recherche la liste de roles
		 * @return liste des roles applicatifs de cette personne
		 */
		public List<RoleApplicatif> findRoleAppli(Person personne) {
			
			OrFilter filterOR = new OrFilter();
			filterOR.or(new EqualsFilter(membres,personne.getDn()));
			for(String p:personne.getListeProfils())
			{
				filterOR.or(new EqualsFilter(profilsApplicatifs,p));
			}
			
			AndFilter filterAND = new AndFilter();
			filterAND.and(new EqualsFilter("objectclass", classeObjet));
			filterAND.and(filterOR);
			
			RoleApplicatifAttributMapper roleApplicatifAttributMapper = new RoleApplicatifAttributMapper();
			String branche = "ou="+this.sousCategorie+",ou="+this.categorie;
			List<RoleApplicatif> liste = ldapTemplateLecture.search(branche, filterAND.encode(), roleApplicatifAttributMapper);
			return liste;
		}
		
		/**
		 * Recherche d'un role applicatif par son DN
		 * @param dn DN du role recherché
		 * @return Role applicatif correspondant à la recherche
		 * @throws ToutaticeAnnuaireException 
		 */
		public RoleApplicatif findByDn(String dn)  {
			RoleApplicatif role = (RoleApplicatif) context.getBean("roleApplicatif");
			try {
				RoleApplicatifAttributMapper roleApplicatifAttributMapper = new RoleApplicatifAttributMapper();
				DirContext DC = ldapTemplateLecture.getContextSource().getReadOnlyContext();
				String BASE_DN = DC.getNameInNamespace();
				int i = dn.lastIndexOf(BASE_DN);
				if(i>=0){
					String subDn = dn.substring(0, i-1);
					role = (RoleApplicatif) ldapTemplateLecture.lookup(new DistinguishedName(subDn), roleApplicatifAttributMapper);		
				} else {
					role = null;
					logger.error("Recherche de role applicatif : le dn "+dn+" n'existe pas dans l'annuaire" );
				}
			} 
			catch (NameNotFoundException e) {
				logger.error("Recherche de role applicatif : le dn "+dn+" n'existe pas dans l'annuaire" );
				role=null;
			} catch (javax.naming.NamingException e) {
				e.printStackTrace();
				role=null;
			}
			return role;
		}

		/**
		 * Recherche d'un role applicatif par son nom
		 * @param cn nom du role recherché
		 * @return Role applicatif recherché
		 * @throws ToutaticeAnnuaireException 
		 */
		public RoleApplicatif findByCn(String cn) throws ToutaticeAnnuaireException {
			RoleApplicatif role = (RoleApplicatif) context.getBean("roleApplicatif");
			if(!cn.trim().isEmpty()){
			Name dn = buildDn(cn);
				try {
					RoleApplicatifAttributMapper roleAppAttributMapper = new RoleApplicatifAttributMapper();
					role = (RoleApplicatif) ldapTemplateLecture.lookup(dn, roleAppAttributMapper);
				} catch (NameNotFoundException e) {
						logger.warn("Recherche de role applicatif : le role "+cn+" n'existe pas dans l'annuaire" );
						role=null;
				} 
			} else {
				role=null;
			}
			return role;
		}
	

	

		/**
		 * Mise à jour d'un role applicatif
		 * Mise à jour des attributs NOM, DISPLAYNAME, DESCRIPTION, MEMBERS, PROFILS et OWNERS
		 * @param role à mettre à jour
		 * @throws ToutaticeAnnuaireException 
		 */
		public void updateRole(RoleApplicatif role) throws ToutaticeAnnuaireException {
			logger.debug("entree dans la methode roleApplicatifDAO.updateRole");
			 Name dn = this.buildDn(role);
			 
			 Attribute attrCn = new BasicAttribute(nom, role.getCn());
		     ModificationItem itemCn = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrCn);
		     Attribute attrDisplayName = new BasicAttribute(displayName, role.getDisplayName());
		     ModificationItem itemDisplayName = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrDisplayName);
			 Attribute attrDesc = new BasicAttribute(description, role.getDescription());
		     ModificationItem itemDesc = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrDesc);
		     
		     try {
		     ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] {itemCn, itemDisplayName, itemDesc});
		     } catch (NamingException e) {
					logger.error("Le role " + role.getCn() + "n'a pas pu être mis à jour" );
					throw new ToutaticeAnnuaireException("Erreur de mise à jour pour le role applicatif "+role.getCn());
			 } 
		     try {
			     this.updateMembers(role);
			     this.updateProfils(role);
			     this.updateMemberURL(role);
			     this.updateOwners(role);
			     this.updateExplicitManagers(role);
		     } catch (ToutaticeAnnuaireException e)
		     {
		    	 throw e;
		     }
		    
		}
		
		/**
		 * Mise à jour des membres du role applicatif
		 * @param role le role à mettre à jour
		 * @throws ToutaticeAnnuaireException 
		 */
		public void updateMembers(RoleApplicatif role) throws ToutaticeAnnuaireException
		{
			logger.debug("entree dans la methode roleApplicatifDAO.updateMembers");
			Name dn = buildDn(role);
			Object[] members = new Object[role.getListeMembers().size()];
			int i = 0;
			for (Object o : role.getListeMembers()) {
				members[i] = o;
				i++;
			}
			try {
			DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
			context.setAttributeValues(RoleApplicatifDao.membres, members);
			ldapTemplateEcriture.modifyAttributes(context);
			} catch (NamingException e) {
				logger.error("Les membres du role " + role.getCn() + "n'ont pas pu être mis à jour" );
				throw new ToutaticeAnnuaireException("Erreur de mise à jour des membres du role applicatif "+role.getCn());
		 } 
		}
		
		/**
		 * Mise à jour des managers du role applicatif
		 * @param role le role à mettre à jour
		 * @throws ToutaticeAnnuaireException 
		 */
		public void updateManagers(RoleApplicatif role) throws ToutaticeAnnuaireException
		{
			Name dn = buildDn(role);
			role.setListeManagers(Helper.supprimerDoublonsCaseNonSensitive(role.getListeManagers()));
			
			if(role.getListeManagers().size()>0){
				role.setListeManagers(Helper.supprimerDoublonsCaseNonSensitive(role.getListeManagers()));		
				Object[] managers = new Object[role.getListeManagers().size()];
				int i = 0;
				for (Object o : role.getListeManagers()) {
					managers[i] = o;
					i++;
				}
				try {
				DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
				context.setAttributeValues(RoleApplicatifDao.manager, managers);
				ldapTemplateEcriture.modifyAttributes(context);
				} catch (NamingException e) {
					logger.error("Les managers du role " + role.getCn() + "n'ont pas pu être mis à jour" );
					throw new ToutaticeAnnuaireException("Erreur de mise à jour des managers du role applicatif "+role.getCn());
					}
			} else {
				// Si il n'y a plus aucun managers on supprime l'attribut
				RoleApplicatif roleLDAP = this.findByCn(role.getCn());
				 if(roleLDAP.getListeManagers().size() > 0) 
				 	{
						BasicAttribute attrMembers = new BasicAttribute(this.manager, false);
						attrMembers.add(null);
						ModificationItem item = new ModificationItem(
								DirContext.REMOVE_ATTRIBUTE, attrMembers);
						ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
				 	}
		}

		}
		
		/**
		 * Mise à jour des managers explicites du role applicatif
		 * @param role le role à mettre à jour
		 * @throws ToutaticeAnnuaireException 
		 */
		public void updateExplicitManagers(RoleApplicatif role) throws ToutaticeAnnuaireException
		{
			Name dn = buildDn(role);
			role.setListeExplicitManagers(Helper.supprimerDoublonsCaseNonSensitive(role.getListeExplicitManagers()));
			
			if(role.getListeExplicitManagers().size()>0){
				Object[] explicitManagers = new Object[role.getListeExplicitManagers().size()];
				int i = 0;
				for (Object o : role.getListeExplicitManagers()) {
					explicitManagers[i] = o;
					i++;
				}
				try {
				DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
				context.setAttributeValues(RoleApplicatifDao.explicitManager, explicitManagers);
				ldapTemplateEcriture.modifyAttributes(context);
				} catch (NamingException e) {
					logger.error("Les managers explciites du role " + role.getCn() + "n'ont pas pu être mis à jour" );
					throw new ToutaticeAnnuaireException("Erreur de mise à jour des managers explicites du role applicatif "+role.getCn());
					}
			} else {
				// Si il n'y a plus aucun managers on supprime l'attribut
				RoleApplicatif roleLDAP = this.findByCn(role.getCn());
				 if(roleLDAP.getListeExplicitManagers().size() > 0) 
				 	{
						BasicAttribute attrMembers = new BasicAttribute(this.explicitManager, false);
						attrMembers.add(null);
						ModificationItem item = new ModificationItem(
								DirContext.REMOVE_ATTRIBUTE, attrMembers);
						ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
				 	}
		}

		}
		
		/**
		 * Mise à jour des profils du role applicatif
		 * @param role le role à mettre à jour
		 * @throws ToutaticeAnnuaireException 
		 */
		public void updateProfils(RoleApplicatif role) throws ToutaticeAnnuaireException
		{
			role.setListeProfils(Helper.supprimerDoublonsCaseNonSensitive(role.getListeProfils()));
			Name dn = buildDn(role);
			
			if(role.getListeProfils().size()>0){
				Object[] profil = new Object[role.getListeProfils().size()];
				int i = 0;
				for (Object o : role.getListeProfils()) {
					profil[i] = o;
					i++;
				}
				try {
				DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
				context.setAttributeValues(RoleApplicatifDao.profilsApplicatifs, profil);
				ldapTemplateEcriture.modifyAttributes(context);
				} catch (NamingException e) {
					logger.error("Les profils du role " + role.getCn() + "n'ont pas pu être mis à jour" );
					throw new ToutaticeAnnuaireException("Erreur de mise à jour des profils du role applicatif "+role.getCn());
				}
			}
			else {
				// Si il n'y a plus aucun profil on supprime l'attribut
					RoleApplicatif roleLDAP = this.findByCn(role.getCn());
					 if(roleLDAP.getListeProfils().size() > 0) 
					 	{
							BasicAttribute attrMembers = new BasicAttribute(this.profilsApplicatifs, false);
							attrMembers.add(null);
							ModificationItem item = new ModificationItem(
									DirContext.REMOVE_ATTRIBUTE, attrMembers);
							ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
					 	}
			}
		}
		
		/**
		 * Mise à jour des membersURL du role applicatif
		 * @param role le role à mettre à jour
		 * @throws ToutaticeAnnuaireException 
		 */
		public void updateMemberURL(RoleApplicatif role) throws ToutaticeAnnuaireException
		{
			role.setListeMemberURL(Helper.supprimerDoublonsCaseNonSensitive(role.getListeMemberURL()));
			Name dn = buildDn(role);
			
			if(role.getListeMemberURL().size()>0) {
				Object[] memberURLTab = new Object[role.getListeMemberURL().size()];
				int i = 0;
				for (Object o : role.getListeMemberURL()) {
				memberURLTab[i] = o;
					i++;
				}
				try {
				DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
				context.setAttributeValues(RoleApplicatifDao.memberURL, memberURLTab);
				ldapTemplateEcriture.modifyAttributes(context);
				} catch (NamingException e) {
					logger.error("Les memberURL du role " + role.getCn() + "n'ont pas pu être mis à jour" );
					throw new ToutaticeAnnuaireException("Erreur de mise à jour des membersURL du role applicatif "+role.getCn());
				}
			} else {
				// Si il n'y a plus aucun filtre on supprime l'attribut
				RoleApplicatif roleLDAP = this.findByCn(role.getCn());
				 if(roleLDAP.getListeMemberURL().size() > 0) 
				 	{
						BasicAttribute attrMembers = new BasicAttribute(this.memberURL, false);
						attrMembers.add(null);
						ModificationItem item = new ModificationItem(
								DirContext.REMOVE_ATTRIBUTE, attrMembers);
						ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
				 	}
		}
		}
		
		public void updateOwners(RoleApplicatif role) throws ToutaticeAnnuaireException
		{
			role.setListeOwners(Helper.supprimerDoublonsCaseNonSensitive(role.getListeOwners()));
			Name dn = buildDn(role);
			if(role.getListeOwners().size()>0){
				Object[] owner = new Object[role.getListeOwners().size()];
				int i = 0;
				for (Object o : role.getListeOwners()) {
					owner[i] = o;
					i++;
				}
				try {
				DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
				context.setAttributeValues(RoleApplicatifDao.owner, owner);
				ldapTemplateEcriture.modifyAttributes(context);
				} catch (NamingException e) {
					logger.error("Les propriétaires du role " + role.getCn() + "n'ont pas pu être mis à jour" );
					throw new ToutaticeAnnuaireException("Erreur de mise à jour des propriétaires du role applicatif "+role.getCn());
		 	    } 
			}
			else {
				// Si il n'y a plus aucun propriétaire on supprime l'attribut
					RoleApplicatif roleLDAP = this.findByCn(role.getCn());
					 if(roleLDAP.getListeOwners().size() > 0) 
					 	{
							BasicAttribute attrMembers = new BasicAttribute(this.owner, false);
							attrMembers.add(null);
							ModificationItem item = new ModificationItem(
									DirContext.REMOVE_ATTRIBUTE, attrMembers);
							ldapTemplateEcriture.modifyAttributes(dn, new ModificationItem[] { item });
					 	}
			}
		}

}
