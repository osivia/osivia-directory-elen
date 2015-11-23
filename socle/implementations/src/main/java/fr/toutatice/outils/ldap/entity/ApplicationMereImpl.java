package fr.toutatice.outils.ldap.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.ApplicationMereDao;


/**
 * Application mère issue de l'annuaire LDAP
 * @author aguihomat
 *
 */

@Service(value = "applicationMere")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ApplicationMereImpl extends ApplicationImpl implements ApplicationMere  {
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	

	@Autowired(required = false)
	@Qualifier("applicationMereDao")
	private ApplicationMereDao applicationMereDao;  
	/**
	 * liste des profils autorisés à inscrire des personnes (ie à créér des applications filles sur cette application mère)
	 */
	private List<String> droitsInscription = new ArrayList<String>();
	/**
	 * Flag indiquant si un export CSV est possible
	 */
	private boolean exportableCsv;
	/**
	 * Indique si l'utilisateur connecté est propriétaire d'une appli fille liée à l'application mère
	 */
	private boolean presenceAppliFillePrUserConnecte;
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#setApplicationMereDao(fr.toutatice.outils.ldap.dao.ApplicationMereDao)
	 */
	public void setApplicationMereDao(ApplicationMereDao applicationMereDao) {
		this.applicationMereDao = applicationMereDao;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#getDroitsInscription()
	 */
	public List<String> getDroitsInscription() {
		return droitsInscription;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#setDroitsInscription(java.util.List)
	 */
	public void setDroitsInscription(List<String> droitsInscription) {
		this.droitsInscription = droitsInscription;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#addDroitInscription(java.lang.String)
	 */
	public void addDroitInscription(String droit) {
		this.droitsInscription.add(droit);
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#isExportableCsv()
	 */
	public boolean isExportableCsv() {
		return exportableCsv;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#setExportableCsv(boolean)
	 */
	public void setExportableCsv(boolean exportableCsv) {
		this.exportableCsv = exportableCsv;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#isPresenceAppliFillePrUserConnecte()
	 */
	public boolean isPresenceAppliFillePrUserConnecte() {
		return presenceAppliFillePrUserConnecte;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#setPresenceAppliFillePrUserConnecte(boolean)
	 */
	public void setPresenceAppliFillePrUserConnecte(boolean presenceAppliFillePrUserConnecte) {
		this.presenceAppliFillePrUserConnecte = presenceAppliFillePrUserConnecte;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#presenceAppliFille()
	 */
	public boolean presenceAppliFille() {
		logger.debug("entree dans la methode ApplicationMere.presenceAppliFille");
		ApplicationFille appFille = (ApplicationFille) super.getContext().getBean("applicationFille");
		return (appFille.presenceAppliFille(this));
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.ApplicationMere#presenceAppliFillePourUser(fr.toutatice.outils.ldap.entity.Person)
	 */
	public boolean presenceAppliFillePourUser(Person user) {
		logger.debug("entree dans la methode ApplicationMere.presenceappliFillePourUser");
		ApplicationFille appFille = (ApplicationFille) super.getContext().getBean("applicationFille");
		return (appFille.presenceAppliFille(user,this));
	}

	/**
	 * Recherche de la liste des applications mères pour lesquelles l'utilisateur a des droits d'inscription
	 * @param user
	 * @return
	 */
	public List<ApplicationMere> findListeApplisMereInscription(Person user)
	{
		logger.debug("entree dans la methode ApplicationMere.findListeapplisMereInscription");
		return applicationMereDao.findApplisMereInscription(user);
	}
	


	
}
