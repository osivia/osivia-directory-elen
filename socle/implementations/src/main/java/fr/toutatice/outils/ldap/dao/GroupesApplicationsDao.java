package fr.toutatice.outils.ldap.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
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
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

import fr.toutatice.outils.ldap.entity.GroupesApplications;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Classe permettant d'accéder à l'annuaire LDAP, de consulter 
 * et mettre à jour les informations concernant les groupes d'applications.
 * Les attributs de cette classe sont les libellés des champs dans l'annuaire LDAP
 * Ils sont à renseigner dans un fichier properties et injectés dans cette classe via SPRING
 * @author aguihomat
 *
 */

@Repository
@Scope("singleton")
public class GroupesApplicationsDao implements ApplicationContextAware{
	
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

private static String nomGroupe;
private static String nomComplet;
private static String memberUrl;
private static String categorie;
private static String sousCategorie;
private static String classeObjet;
private static String profil;
private static String BASE_DN="";


public String getNomGroupe() {
	return nomGroupe;
}
public void setNomGroupe(String nomGrp) {
	nomGroupe = nomGrp;
}
public String getNomComplet() {
	return nomComplet;
}
public void setNomComplet(String nom) {
	nomComplet = nom;
}
public String getMemberUrl() {
	return memberUrl;
}
public void setMemberUrl(String s) {
	memberUrl = s;
}
public void setCategorie(String s) {
	categorie = s;
}
public void setSousCategorie(String s) {
	GroupesApplicationsDao.sousCategorie = s;
}
public void setClasseObjet(String s) {
	classeObjet = s;
}
public void setProfil(String s) {
	GroupesApplicationsDao.profil = s;
}
public void setApplicationContext(ApplicationContext ctx) throws BeansException {
	context = ctx;
}
public void setBASE_DN(String s) {
	BASE_DN = s;	}

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


//Classe interne permettant de récupérer une application dans l'annuaire
private static class GroupesApplicationsAttributMapper implements AttributesMapper {
	public Object mapFromAttributes(Attributes attrs)
			throws javax.naming.NamingException {

		// instanciation d'un objet GroupesApplications via Spring 
		GroupesApplications grpApp = (GroupesApplications) context.getBean("groupesApplications");

		grpApp.setId(attrs.get(nomGroupe).get().toString());
		
		if (attrs.get(nomComplet) != null)
		{
			grpApp.setDisplayName(attrs.get(nomComplet).get().toString());
		}
		else { 
			grpApp.setDisplayName("");
		}
		
		if (attrs.get(memberUrl) != null)
		{
			grpApp.setMemberUrl(attrs.get(memberUrl).get().toString());	
		}
		else { 
			grpApp.setMemberUrl("");
		}
		
		return grpApp;
	}
}


/**
 * Construit le DN d'un groupe d'applications grace à son nom
 * @param cn nom du groupe d'applications
 * @return DN du groupe d'applications (sans la base de l'annuaire LDAP, défini dans le fichier properties)
 */
private Name buildDn(String cn) {
	DistinguishedName dn = new DistinguishedName();
	dn.add("ou", categorie);
	dn.add("ou",sousCategorie);
	dn.add(nomGroupe, cn);
	return dn;
}


/**
 * Recherche d'un groupe par son nom
 * @param cn nom exact du groupe d'applications recherché
 * @return groupe d'applications recherché
 * @throws ToutaticeAnnuaireException 
 */
public GroupesApplications findByPrimaryKey(String id) throws ToutaticeAnnuaireException {
	GroupesApplications groupe = (GroupesApplications) context.getBean("groupesApplications");
	if(!id.trim().isEmpty()){
		Name dn = buildDn(id);
		try {
			GroupesApplicationsAttributMapper groupeAppAttributMapper = new GroupesApplicationsAttributMapper();
			groupe = (GroupesApplications) ldapTemplateLecture.lookup(dn,groupeAppAttributMapper);
		} 		
		catch (NamingException e) {
			logger.error("Le groupe d'applications "+id+" n'a pas été trouvé dans l'annuaire");
			throw new ToutaticeAnnuaireException("Erreur de lecture annuaire : le groupe d'applications "+id+" n'a pas été trouvé");
		}
	} else{
		groupe=null;
	}
	return groupe;
}

/**
 * Recherche d'un groupe d'applications par son adresse dans l'annuaire
 * @param path adresse du groupe dans l'annuaire (sans le base DN)
 * @return groupe d'applications recherché
 * @throws ToutaticeAnnuaireException 
 */
public GroupesApplications findByDn(String dn) throws ToutaticeAnnuaireException {
	GroupesApplications groupe;
	try {
		GroupesApplicationsAttributMapper groupeAppAttributMapper = new GroupesApplicationsAttributMapper();
		groupe = (GroupesApplications) ldapTemplateLecture.lookup(dn,groupeAppAttributMapper);
	} catch (NamingException e) {
		logger.error("Le groupe d'applications correspondant au chemin "+ dn +" n'a pas été trouvé dans l'annuaire");
		throw new ToutaticeAnnuaireException("Erreur de lecture annuaire : le groupe d'applications correspondant à l'adresse "+dn+" n'a pas été trouvé");
	}
	return groupe;
}

/**
 * Recherche des groupes d'applications situés à l'adresse passée en paramètre
 * @param chemin adresse où rechercher les groupes
 * @return liste des groupes d'applications selectionnés
 */
@SuppressWarnings("unchecked")
public List<GroupesApplications> findGroupesNoeud(String dnNoeud) {
	String dnNoeudRelatif = dnNoeud.replace(","+BASE_DN,"");
	AndFilter filter = new AndFilter();
	filter.and(new EqualsFilter("objectclass", classeObjet));
	GroupesApplicationsAttributMapper groupesApplicationsAttributMapper = new GroupesApplicationsAttributMapper();
	return ldapTemplateLecture.search(dnNoeudRelatif, filter.encode(), SearchControls.ONELEVEL_SCOPE , groupesApplicationsAttributMapper);
}

/**
 * Recherche des groupes d'applications situés à l'adresse passée en paramètre et dont
 * l'utilisateur passé en paramètre fait partie via un de ses profils
 * @param chemin
 * @param user
 * @return
 */
@SuppressWarnings("unchecked")
public List<GroupesApplications> findGroupesNoeud(String dnNoeud, Person user) {
	List<String> listeProfils = user.getListeProfils();
	String dnNoeudRelatif = dnNoeud.replace(","+BASE_DN,"");
	OrFilter orFilter = new OrFilter();
	for(String p:listeProfils)	
	{
		orFilter.or(new EqualsFilter(profil, p));
	}	
	
	AndFilter filter = new AndFilter();
	filter.and(new EqualsFilter("objectclass", classeObjet));
	filter.and(orFilter);
	
	GroupesApplicationsAttributMapper groupesApplicationsAttributMapper = new GroupesApplicationsAttributMapper();
	return ldapTemplateLecture.search(dnNoeudRelatif, filter.encode(), SearchControls.ONELEVEL_SCOPE , groupesApplicationsAttributMapper);
}




}
