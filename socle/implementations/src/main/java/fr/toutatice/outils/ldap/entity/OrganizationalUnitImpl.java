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

import fr.toutatice.outils.ldap.dao.OrganizationalUnitDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Service(value = "organizationalUnit")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class OrganizationalUnitImpl implements ApplicationContextAware, OrganizationalUnit {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	@SuppressWarnings("unused")
	private static ApplicationContext context;

	/**
	 * Factory, à instancier via l'IOC de SPRING
	 */
	@Autowired(required = false)
	private OrganizationalUnitDao organizationalUnitDao;
	/**
	 * Id
	 */
	private String id;
	private String dn;

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.OrganizationalUnit#setOrganizationalUnitDao(fr.toutatice.outils.ldap.dao.OrganizationalUnitDao)
	 */
	public void setOrganizationalUnitDao(OrganizationalUnitDao organizationalUnitDao) {
		this.organizationalUnitDao = organizationalUnitDao;
	}
		

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.OrganizationalUnit#getId()
	 */
	public String getId() {
		return id;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.OrganizationalUnit#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.OrganizationalUnit#getDn()
	 */
	public String getDn() {
		return dn;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.OrganizationalUnit#setDn(java.lang.String)
	 */
	public void setDn(String dn) {
		this.dn = dn;	}


	
	/**
	 * Recherche d'un élément par son adresse dans l'annuaire (DN sans le base DN)
	 * @param path adresse dans l'annuaire
	 * @return organizationalUnit recherché
	 * @throws ToutaticeAnnuaireException
	 */
	public OrganizationalUnit findGroupeByDn(String dn) throws ToutaticeAnnuaireException {
		logger.debug("entree dans la methode OrganizationalUnit.findGroupeByPath");
		OrganizationalUnit org;
		try {
			org = organizationalUnitDao.findByDn(dn);
			org.setDn(dn);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
		return org;
	}

	/**
	 * Recherche des éléments placés directement sous l'élément courant
	 * @return liste des "enfants"
	 */
	public List<OrganizationalUnit> findSubFolders()
	{
		logger.debug("entree dans la methode OrganizationalUnit.findSubFolders");
		List<OrganizationalUnit> liste = organizationalUnitDao.findSubFolders(this.getDn());
		for(OrganizationalUnit org : liste)
		{
			org.setDn("ou="+org.getId()+","+this.getDn());
		}
		return liste;
	}
	
	

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.OrganizationalUnit#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx)
	throws BeansException {
	this.context = ctx;

	}
	
}
