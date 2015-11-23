package fr.toutatice.outils.ldap.dao;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;

import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface OrganisationDao {

	public abstract void setId(String s);

	public abstract void setDescription(String s);

	public abstract void setDisplayName(String s);

	public abstract void setLocalisation(String s);

	public abstract void setPostalCode(String s);

	public abstract void setStreet(String s);

	public abstract void setTelephoneNumber(String s);

	public abstract void setFaxNumber(String s);

	public abstract void setSeeAlso(String s);

	public abstract void setClasseObjet(String s);

	public abstract void setCategorieLDAP(String s);

	public abstract void setProfils(String s);

	public abstract void setManager(String s);

	public abstract void setExplicitManager(String m);

	public abstract LdapTemplate getLdapTemplateEcriture();

	public abstract void setLdapTemplateEcriture(
			LdapTemplate ldapTemplateEcriture);

	public abstract LdapTemplate getLdapTemplateLecture();

	public abstract void setLdapTemplateLecture(LdapTemplate ldapTemplateLecture);

	public abstract void setBASE_DN(String s);

	public abstract String buildFullDn(String rne);

	/**
	 * Recherche d'une structure par son numéro
	 * @param rne numéro de la structure
	 * @return structure recherchée
	 * @throws ToutaticeAnnuaireException 
	 */
	public abstract Organisation findByPrimaryKey(String identifiant);

	public abstract Organisation findByDn(String dn)
			throws ToutaticeAnnuaireException;

	/**
	 * liste toutes les structures de l'annuaire
	 * @return liste des structures
	 */
	public abstract List<Organisation> findAllOrganisations();

	/**
	 * liste toutes les structures de l'annuaire débutant par la chaine de caractères passée en paramètre
	 * @param prefixe début du nom des structures recherchées
	 * @return liste des structures
	 */
	public abstract List<Organisation> findOrganisationByPrefixe(String prefixe);

	public abstract List<Organisation> findOrganisationsMultiCriteres(
			String rne, String nom);

	public abstract List<Organisation> findOrganisationPersonneByProfil(Person p);

	public abstract List<Organisation> findFromFiltre(String filtre,
			String critereTri);

	public abstract DirContextOperations prepareContextForUpdate(
			DirContextOperations context, Organisation orga);

	public abstract void update(Organisation orga);

	public abstract void setApplicationContext(ApplicationContext ctx)
			throws BeansException;

}