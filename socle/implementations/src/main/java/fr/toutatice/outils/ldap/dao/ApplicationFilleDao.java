package fr.toutatice.outils.ldap.dao;

import java.util.List;

import javax.naming.Name;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;


import org.springframework.context.annotation.Scope;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;

import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.toutatice.outils.ldap.entity.ApplicationFille;
import fr.toutatice.outils.ldap.entity.ApplicationMere;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Classe permettant d'accéder à l'annuaire LDAP, de consulter 
 * et mettre à jour les informations concernant les applications filles.
 * Les attributs de cette classe sont les libellés des champs dans l'annuaire LDAP
 * Ils sont à renseigner dans un fichier properties et injectés dans cette classe via SPRING
 * @author aguihomat
 *
 */
@Repository("applicationFilleDao")
@Qualifier("applicationFilleDao")
@Scope("singleton")
public class ApplicationFilleDao extends ApplicationDao {
	
	
	private static String objectClassFille;
	private static String proprietaire;
	private static String applicationMere;

	public void setObjectClassFille(String s) {
		objectClassFille = s;
	}
	public void setProprietaire(String s) {
		proprietaire = s;
	}
	public void setApplicationMere(String s) {
		applicationMere = s;
	}


	// Classe interne permettant de récupérer une application dans l'annuaire
	protected static class ApplicationFilleAttributMapper extends ApplicationAttributMapper implements AttributesMapper {
		
		public Object mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {
			
			ApplicationFille app = (ApplicationFille) context.getBean("applicationFille");
			app = (ApplicationFille) super.valoriser(app,attrs);
	
			if (attrs.get(proprietaire) != null)
			{
			app.setProprietaire(attrs.get(proprietaire).get().toString());
			}
			else
			{
				logger.info("L'application fille "+app.getId()+" n'a aucun propriétaire de renseigné dans l'annuaire");
				app.setProprietaire("");
			}
			if(attrs.get(applicationMere) !=null){
				app.setApplicationMere(attrs.get(applicationMere).get().toString());
			}else{
				app.setApplicationMere("");
			}
					
			return app;
		}
	}
	
	private Attributes buildAttributes(ApplicationFille app) {
	      Attributes attrs = new BasicAttributes();
	      BasicAttribute ocattr = new BasicAttribute("objectclass");
	      ocattr.add("top");
	      ocattr.add(super.getObjectClass());
	      ocattr.add(objectClassFille);
	      attrs.put(ocattr);
	      attrs.put(super.getId(), app.getId());
	      attrs.put(super.getNom(), app.getNom());
	      attrs.put(proprietaire, app.getProprietaire());
	      attrs.put(applicationMere, app.getApplicationMere());
	      attrs.put(super.getUrl(),app.getUrl());
	      attrs.put(super.getDescription(),app.getDescription());
	      
	      BasicAttribute pwdattr = new BasicAttribute(super.getPasswords());
	      for (byte[] o : app.getListePasswords())
	      {
	    	  pwdattr.add(o);
	      }
	      attrs.put(pwdattr);
	      
	      BasicAttribute catattr = new BasicAttribute(super.getCategories());
	      for (String s : app.getCategories())
	      {
	    	  catattr.add(s);
	      }
	      attrs.put(catattr);
	      
	      return attrs;
	   }


	/**
	 * Recherche d'une application fille par propriétaire et application mère
	 * @param user propriétaire de l'application fille
	 * @param appMere application mère dont dépend l'application fille
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ApplicationFille findAppliFille(Person user, ApplicationMere appMere)
	{
		ApplicationFille app = (ApplicationFille) context.getBean("applicationFille");
		
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClassFille));
		filter.and(new EqualsFilter(proprietaire, user.findFullDn(user.getUid()) ));
		filter.and(new EqualsFilter(applicationMere, appMere.findFullDn(appMere.getId()) ));
		
		ApplicationFilleAttributMapper applicationAttributMapper = new ApplicationFilleAttributMapper();
		List<ApplicationFille> liste = ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
		
		if (! liste.isEmpty())
			{app = liste.get(0);}
		else{
			app=null;
		}
		return app;
		
	}
	
	/**
	 * Recherche de la liste des applications filles d'une application mère donnée
	 * @param appMere application mère
	 * @return liste des applications filles
	 */
	@SuppressWarnings("unchecked")
	public List<ApplicationFille> findAppliFille(ApplicationMere appMere)
	{
		logger.debug("entree dans la methode applicationfille.findAppliFille(appMere)");
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", objectClassFille));
		filter.and(new EqualsFilter(applicationMere, appMere.findFullDn(appMere.getId()) ));
		
		ApplicationFilleAttributMapper applicationAttributMapper = new ApplicationFilleAttributMapper();
		List<ApplicationFille> liste = ldapTemplateLecture.search("", filter.encode(), applicationAttributMapper);
		
		return liste;
		
	}
	
	
	/**
	 * Création d'une application fille
	 * @param app application fille à ajouter dans l'annuaire
	 * @throws ToutaticeAnnuaireException 
	 */
	 public void create(ApplicationFille app) throws ToutaticeAnnuaireException {
		  Name dn = buildDn(app);
		  Attributes attrs = null;
		  try {
			attrs = buildAttributes(app);
			ldapTemplateEcriture.bind(dn, null,attrs );
		  } 		
			catch (NamingException e) {
				logger.error("La création de l'application "+app.getNom()+ " a échoué");
				logger.error("DN Application = "+dn);
				logger.error("Valeur attributs : "+attrs);
				logger.error(e);
				throw new ToutaticeAnnuaireException("Erreur lors de la création de l'application fille "+app.getId());
			}
	   }

	 /**
	  * Suppression d'une application fille dans l'annuaire
	  * @param app application fille à supprimer
	 * @throws ToutaticeAnnuaireException 
	  */
	   public void delete(ApplicationFille app) throws ToutaticeAnnuaireException {
		   logger.debug("entree dans la methode applicationFille.delete");
		   try {
	      ldapTemplateEcriture.unbind(buildDn(app));
		   } 		
			catch (NamingException e) {
				logger.error("La suppression de l'application "+app.getNom()+ " a échoué");
				throw new ToutaticeAnnuaireException("Erreur lors de la suppression de l'application fille "+app.getId());
			}
	   }

}
