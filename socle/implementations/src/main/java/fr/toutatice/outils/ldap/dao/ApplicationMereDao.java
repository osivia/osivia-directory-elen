package fr.toutatice.outils.ldap.dao;

import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.context.annotation.Scope;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.toutatice.outils.ldap.entity.ApplicationMere;
import fr.toutatice.outils.ldap.entity.Person;

/**
 * Classe permettant d'accéder à l'annuaire LDAP, de consulter 
 * et mettre à jour les informations concernant les applications mères.
 * Les attributs de cette classe sont les libellés des champs dans l'annuaire LDAP
 * Ils sont à renseigner dans un fichier properties et injectés dans cette classe via SPRING
 * @author aguihomat
 *
 */

@Repository("applicationMereDao")
@Qualifier("applicationMereDao")
@Scope("singleton")
public class ApplicationMereDao extends ApplicationDao {

	private static String objectClassMere;
	private static String droitInscription;

	public void setObjectClassMere(String s) {
		objectClassMere = s;
	}
	public void setDroitInscription(String s) {
		droitInscription = s;
	}

	// Classe interne permettant de récupérer une application dans l'annuaire
	protected static class ApplicationMereAttributMapper extends ApplicationAttributMapper implements AttributesMapper {
		
		public Object mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {
			
			ApplicationMere app = (ApplicationMere) context.getBean("applicationMere");
			app = (ApplicationMere) super.valoriser(app,attrs);
			
			Attribute attr = attrs.get(droitInscription);
			if (attr != null) {
				@SuppressWarnings("rawtypes")
				NamingEnumeration m = (attr.getAll());
				while (m.hasMore()) {
					app.addDroitInscription((String)m.next());
				}
			}
			
			app.setExportableCsv(false);
			for(String elt : app.getProprietes())
			{
				if (elt.startsWith("CSV="))
				{app.setExportableCsv(true);}
			}
			
			return app;
			}

	}

	/**
	 * Recherche de toutes les applications mères pour lesquelles l'utilisateur a des droits d'inscription
	 * @param user utilisateur 
	 * @return liste des applications
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public List<ApplicationMere> findApplisMereInscription(Person user)
	{
		logger.debug("entree dans la methode applicationMere.findApplisMereInscription");
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", this.getObjectClass())); 
		filter.and(new EqualsFilter("objectclass", objectClassMere)); 
		
		OrFilter orFilter = new OrFilter();
		for(String profilUser:user.getListeProfils())	
		{
			orFilter.or(new EqualsFilter(droitInscription, profilUser));
		}	
		filter.and(orFilter);
		
		ApplicationMereAttributMapper applicationAttributMapper = new ApplicationMereAttributMapper();
		return (List<ApplicationMere>)ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
	}

}
