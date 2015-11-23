package fr.toutatice.outils.ldap.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

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
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Repository;

import fr.toutatice.outils.ldap.entity.Application;
import fr.toutatice.outils.ldap.entity.OrganizationalUnit;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Classe permettant d'accéder à l'annuaire LDAP, de consulter 
 * et mettre à jour les informations concernant les organizational unit.
 * Les attributs de cette classe sont les libellés des champs dans l'annuaire LDAP
 * Ils sont à renseigner dans un fichier properties et injectés dans cette classe via SPRING
 * @author aguihomat
 *
 */

@Repository
@Scope("singleton")
public class OrganizationalUnitDao  implements ApplicationContextAware {

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

	private static String classeObjet;
	private static String nom;
	private static String BASE_DN="";

	public String getNom() {
		return nom;	}
	public void setNom(String s) {
		nom = s;	}
	public void setClasseObjet(String s) {
		classeObjet = s;	}
	public void setBASE_DN(String s) {
		BASE_DN = s;	}
	
	
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		context = ctx;
	}
	

	//Classe interne permettant de récupérer une application dans l'annuaire
	private static class OrganizationalUnitAttributMapper implements AttributesMapper {
		public Object mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {

			OrganizationalUnit org = (OrganizationalUnit) context.getBean("organizationalUnit");
			org.setId(attrs.get(nom).get().toString());		
			return org;
		}
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
	
	
	
	/**
	 * Recherche des éléments de type organizationalUnitclassés sous le noeud courant dans l'organisation
	 * @param chemin emplacement du noeud courant (DN sans le BASE_DN)
	 * @return liste des organizational unit placées directement sous l'OU courant
	 */
	@SuppressWarnings("unchecked")
	public List<OrganizationalUnit> findSubFolders(String dn)
	{
		logger.debug("entree dans la methode organizationalUnit.findSubFolders");
		String dnRelatif = dn.replace(","+BASE_DN,"");
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classeObjet));
		OrganizationalUnitAttributMapper organizationalUnitAttributMapper = new OrganizationalUnitAttributMapper();
		return ldapTemplateLecture.search(dnRelatif, filter.encode(), SearchControls.ONELEVEL_SCOPE, organizationalUnitAttributMapper);
	}

	/**
	 * Recherche d'un organizational Unit par son DN 
	 * @param path DN de l'organizational unit recherché (sans le base DN)
	 * @return l'organizational unit recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public OrganizationalUnit findByDn(String dn) throws ToutaticeAnnuaireException {
		logger.debug("entree dans la methode organizationalUnit.findByPath");
		String dnRelatif = dn.replace(","+BASE_DN,"");
		OrganizationalUnit org = (OrganizationalUnit) context.getBean("organizationalUnit");
		try {
			OrganizationalUnitAttributMapper orgAttributMapper = new OrganizationalUnitAttributMapper();
			org = (OrganizationalUnit) ldapTemplateLecture.lookup(dnRelatif,orgAttributMapper);
		} catch (NamingException e) {
			logger.error("L'unité d'organisation correspondant au chemin "+dn+" n'a pas été trouvée dans l'annuaire");
			throw new ToutaticeAnnuaireException("Erreur lors de la recherche dans l'annuaire : aucune unité d'organisation n'a été trouvée pour le path : "+dn);
		}
		return org;
	}
	

	
	
}
