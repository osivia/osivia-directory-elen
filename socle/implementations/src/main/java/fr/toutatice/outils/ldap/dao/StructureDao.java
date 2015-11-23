package fr.toutatice.outils.ldap.dao;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.ldap.core.DirContextOperations;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Structure;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface StructureDao {

	public abstract void setCategorieLDAP(String s);

	public abstract void setClasseObjetStructure(String s);

	public abstract void setNoRecepisseCnil(String s);

	public abstract void setTypeStructure(String s);

	public abstract void setResponsable(String s);

	public abstract void setSiren(String s);

	public abstract void setEmail(String s);

	public abstract void setSiteweb(String s);

	public abstract void setContactENT(String s);

	public abstract void setNumeroUAI(String s);

	public abstract void setGeoLoc(String s);

	public abstract void setResentice(String s);

	public abstract void setDaip(String s);

	public abstract void setBASE_DN(String s);

	public abstract String buildFullDn(String rne);

	/**
	 * Recherche d'une structure par son numéro
	 * @param rne numéro de la structure
	 * @return structure recherchée
	 * @throws ToutaticeAnnuaireException 
	 */
	public abstract Structure findByPrimaryKey(String rne);

	/**
	 * liste toutes les structures de l'annuaire
	 * @return liste des structures
	 */
	public abstract List<Structure> findAllStructures();

	/**
	 * liste toutes les structures de l'annuaire débutant par la chaine de caractères passée en paramètre
	 * @param prefixe début du nom des structures recherchées
	 * @return liste des structures
	 */
	public abstract List<Structure> findStructuresByPrefixe(String prefixe);

	public abstract List<Structure> findListeStrFiltreLdap(String filtreLdap)
			throws ToutaticeAnnuaireException;

	public abstract List<Structure> findStructurePersonneByProfil(Person p);

	public abstract List<Structure> findStructuresLieesProfil(String dnProfil);

	/**
	 * Creation d'une structure dans l'annuaire
	 * @param str Structure à créér
	 * @throws ToutaticeAnnuaireException
	 */
	public abstract void create(Structure str)
			throws ToutaticeAnnuaireException;

	/**
	 * Suppression d'une structure dans l'annuaire
	 * @param str Structure à supprimer
	 * @throws ToutaticeAnnuaireException 
	 */
	public abstract void delete(Structure str)
			throws ToutaticeAnnuaireException;

	public abstract DirContextOperations prepareContextForUpdate(
			DirContextOperations context, Structure str);

	public abstract void update(Structure str)
			throws ToutaticeAnnuaireException;

	public abstract void setApplicationContext(ApplicationContext ctx)
			throws BeansException;

}