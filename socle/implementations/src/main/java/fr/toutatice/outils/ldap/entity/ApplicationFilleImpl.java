package fr.toutatice.outils.ldap.entity;

import java.security.NoSuchAlgorithmException;

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

import fr.toutatice.outils.ldap.dao.ApplicationFilleDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Application fille issue de l'annuaire LDAP
 * @author aguihomat
 *
 */
@Service(value = "applicationFille")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ApplicationFilleImpl extends ApplicationImpl implements ApplicationFille, ApplicationContextAware {
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	
	private static ApplicationContext context;
	
	@Autowired(required = false)
	@Qualifier("applicationFilleDao")
	private ApplicationFilleDao applicationFilleDao; 
	
	/**
	 * Propriétaire de l'application Fille (DN complet)
	 */
	private String proprietaire;
	/**
	 * Application mère liée (DN complet)
	 */
	private String applicationMere;
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#getProprietaire()
	 */
	public String getProprietaire() {
		return proprietaire;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#setProprietaire(java.lang.String)
	 */
	public void setProprietaire(String proprietaire) {
		this.proprietaire = proprietaire;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#getApplicationMere()
	 */
	public String getApplicationMere() {
		return applicationMere;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#setApplicationMere(java.lang.String)
	 */
	public void setApplicationMere(String applicationMere) {
		this.applicationMere = applicationMere;
	}



	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#setApplicationFilleDao(fr.toutatice.outils.ldap.dao.ApplicationFilleDao)
	 */
	public void setApplicationFilleDao(ApplicationFilleDao applicationFilleDao) {
		this.applicationFilleDao = applicationFilleDao;
	}


	/**
	 * Recherche d'une application fille
	 * @param user propriétaire de l'application fille recherchée
	 * @param app application mère dont dépend l'application fille recherchée
	 * @return application fille recherchée
	 */
	public ApplicationFille findAppliFille(Person user, ApplicationMere app)
	{
		return applicationFilleDao.findAppliFille(user, app);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#presenceAppliFille(fr.toutatice.outils.ldap.entity.Person, fr.toutatice.outils.ldap.entity.ApplicationMere)
	 */
	public boolean presenceAppliFille(Person user, ApplicationMere app)
	{
		logger.debug("entree dans la methode ApplicationFille.presenceAppliFille(user,appMere)");
		boolean presence;
		if (applicationFilleDao.findAppliFille(user, app).getId() == null)
		{ presence = false;}
		else
		{ presence = true;}
		return presence;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#presenceAppliFille(fr.toutatice.outils.ldap.entity.ApplicationMere)
	 */
	public boolean presenceAppliFille(ApplicationMere app)
	{
		logger.debug("entree dans la methode ApplicationFille.presenceAppliFille(appMere)");
		boolean presence;
		if (applicationFilleDao.findAppliFille(app).isEmpty())
		{ presence = false;}
		else
		{ presence = true;}
		return presence;
	}

	/**
	 * Création d'une application fille
	 * @param user propriétaire de l'application à créér
	 * @param appliMere application mère dont dépendra l'application fille
	 * @return application fille créé dans l'annuaire
	 * @throws ToutaticeAnnuaireException 
	 */
	@SuppressWarnings("static-access")
	public ApplicationFille creerAppliFille(Person user, ApplicationMere appliMere) throws ToutaticeAnnuaireException
	{
		ApplicationFille appFille = (ApplicationFille) context.getBean("applicationFille");
		
		appFille.setId(appliMere.getId() + "_" + user.getUid());
		appFille.setNom(appliMere.getNom());
		appFille.setCategories(appliMere.getCategories());
		appFille.setProprietaire(user.findFullDn(user.getUid()));
		appFille.setUrl(appliMere.getUrl());
		appFille.setApplicationMere(appliMere.findFullDn(appliMere.getId()));
		
		String description="";
		for (String s : appliMere.getProprietes())
		{
			if (s.startsWith("libelleApplisFilles="))
			{description=s.substring(20);}
		}
		appFille.setDescription(description);
		
		try {
			appFille.addPassword(ApplicationImpl.fonctionBase64Sha1(user.getUid()).getBytes());
		} catch (NoSuchAlgorithmException e) {
			logger.error("Problème d'encodage du mot de passe");
		}
		
		try {
			applicationFilleDao.create(appFille);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
		return this.findAppliFille(user, appliMere);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#supprimerAppliFille()
	 */
	public void supprimerAppliFille() throws ToutaticeAnnuaireException
	{
		logger.debug("entree dans la methode ApplicationFille.supprimerAppliFille");
		try {
			applicationFilleDao.delete(this);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationFille#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx)
	throws BeansException {
	this.context = ctx;
	}
}
