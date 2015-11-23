package fr.toutatice.outils.ldap.dao;

import java.util.List;

import fr.toutatice.outils.ldap.entity.Etablissement;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface EtablissementDao {

	public abstract void setCategorieLDAP(String s);

	public abstract void setClasseObjetEtablissement(String s);

	public abstract void setBASE_DN(String s);

	public abstract void setMinistereTutelle(String s);

	public abstract void setBassin(String s);

	public abstract void setContrat(String s);

	public abstract void setClasses(String s);

	public abstract void setGroupes(String s);

	public abstract String buildFullDn(String rne);

	public abstract Etablissement findEtablissementByRne(String rne);

	/**
	 * liste tous les établissements de l'annuaire
	 * @return liste des structures
	 */
	public abstract List<Etablissement> findAllEtablissements();

	public abstract List<Etablissement> findListeEtbFiltreLdap(String filtreLdap)
			throws ToutaticeAnnuaireException;

	public abstract List<Etablissement> findEtablissementsMultiCriteres(
			String ministere, String type, String contrat, String bassin,
			String rne, String nom, String ville, String critereTri);

	public abstract List<Etablissement> findEtablissementsMultiCriteres(
			String ministere, List<String> listeType, String contrat,
			String bassin, String rne, String nom, String ville,
			String critereTri);

}