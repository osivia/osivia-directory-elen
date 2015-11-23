package fr.toutatice.outils.ldap.entity;

import java.util.List;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface Etablissement extends Structure {


	public String getDn();

	public String getMinistereTutelle();

	public void setMinistereTutelle(String ministereTutelle);

	public String getContrat();

	public void setContrat(String contrat);

	public String getBassin();

	public void setBassin(String bassin);

	public List<String> getListeClasses();

	public void setListeClasses(List<String> listeClasses);

	public List<String> getListeGroupes();

	public void setListeGroupes(List<String> listeGroupes);

	public Etablissement findEtablissementsByRne(String rne);

	public List<Etablissement> findAllEtablissements();

	public List<Etablissement> findListeEtbFiltreLdap(String filtreLdap)
			throws ToutaticeAnnuaireException;

	public List<Etablissement> findEtablissementsMultiCriteres(
			String ministere, String type, String contrat, String bassin,
			String rne, String nom, String ville, String critereTri);

	public List<Etablissement> findEtablissementsMultiCriteres(
			String ministere, List<String> listeType, String contrat,
			String bassin, String rne, String nom, String ville,
			String critereTri);

}