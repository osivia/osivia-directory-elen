package fr.toutatice.outils.ldap.entity;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.GroupesApplicationsDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Service(value = "groupesApplications")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class GroupesApplicationsImpl implements ApplicationContextAware, GroupesApplications{
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	
	private static ApplicationContext context;  
	
	/**
	 * Factory, à instancier via l'IOC de SPRING
	 */
	@Autowired(required = false)
	private GroupesApplicationsDao groupesApplicationsDao;
	/**
	 * Nom du groupe d'applications
	 */
	private String id;
	/**
	 * Nom complet du groupe (sans abréviation)
	 */
	private String displayName;
	/**
	 * Filtre LDAP permettant de retrouver les applications faisant partie du groupe
	 */
	private String memberUrl;
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.GroupesApplication#setGroupesApplicationsDao(fr.toutatice.outils.ldap.dao.GroupesApplicationsDao)
	 */
	public void setGroupesApplicationsDao(
			GroupesApplicationsDao groupesApplicationsDao) {
		this.groupesApplicationsDao = groupesApplicationsDao;
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.GroupesApplication#getId()
	 */
	public String getId() {
		return id;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.GroupesApplication#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.GroupesApplication#getDisplayName()
	 */
	public String getDisplayName() {
		return displayName;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.GroupesApplication#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.GroupesApplication#getMemberUrl()
	 */
	public String getMemberUrl() {
		return memberUrl;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.GroupesApplication#setMemberUrl(java.lang.String)
	 */
	public void setMemberUrl(String memberUrl) {
		this.memberUrl = memberUrl;	}
		

	/**
	 * Recherche d'un groupe d'applications par son identifiant
	 * @param nom nom du groupe recherché
	 * @return groupe d'applications recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public GroupesApplications findGroupeById(String id) throws ToutaticeAnnuaireException {
		GroupesApplications grp;
		try {
			grp = groupesApplicationsDao.findByPrimaryKey(id);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
		return grp;
	}
	 
	/**
	 * Recherche d'un groupe d'application par son adresse dans l'annuaire
	 * @param path adresse du groupe dans l'annuaire
	 * @return groupe d'applications recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public GroupesApplications findGroupeByDn(String dn) throws ToutaticeAnnuaireException {
		GroupesApplications grp;
		try {
			grp = groupesApplicationsDao.findByDn(dn);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
		return grp;
	}
	
	/**
	 * Recherche de tous les groupes d'applications existants sous le chemin passé en paramètre
	 * @param chemin base de l'annuaire à partir de laquelle lancer la recherche
	 * @return Liste de tous les groupes sélectionnés
	 */
	public List<GroupesApplications> findGroupesNoeud(String dnNoeud) {
		
		List<GroupesApplications> liste = groupesApplicationsDao.findGroupesNoeud(dnNoeud);
		return liste; 
	}
	
	/**
	 * Recherche des groupes d'applications autorisés pour un utilisateur
	 * @return Liste des groupes recherchés
	 */
	public List<GroupesApplications> findGroupesNoeud(String dnNoeud, Person user) {
		List<GroupesApplications> liste = groupesApplicationsDao.findGroupesNoeud(dnNoeud, user);
		return liste; 
	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.GroupesApplication#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.context = ctx;

	}

}
