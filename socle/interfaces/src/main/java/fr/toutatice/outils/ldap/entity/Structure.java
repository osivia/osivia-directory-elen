package fr.toutatice.outils.ldap.entity;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface Structure extends Organisation {

	public String getDn();

	public String getNoRecepisseCNIL();

	public void setNoRecepisseCNIL(String noRecepisseCNIL);

	public String getTypestructure();

	public void setTypestructure(String typestructure);

	public String getNomResponsable();

	public void setNomResponsable(String responsable);

	public String getSiren();

	public void setSiren(String siren);

	public String getEmail();

	public void setEmail(String email);

	public String getSiteWeb();

	public void setSiteWeb(String siteWeb);

	public String getContactENT();

	public void setContactENT(String contact);

	public String getNumeroUAI();

	public void setNumeroUAI(String numeroUAI);

	public String getLatitude();

	public void setLatitude(String latitude);

	public String getLongitude();

	public void setLongitude(String longitude);

	public ArrayList<String> getListeDnResentice();

	public void setListeDnResentice(ArrayList<String> listeDnResentice);

	public ArrayList<String> getListeDnDaip();

	public void setListeDnDaip(ArrayList<String> listeDnDaip);

	/**
	 * Recherche d'une structure par son numéro
	 * @param rne numéro de la structure recherchée (RNE)
	 * @return structure recherchée
	 * @throws ToutaticeAnnuaireException
	 */
	public Structure findStructure(String rne);

	public List<Structure> findStructuresPersonneByProfil(Person p);

	public List<Structure> findAllStructures();

	public List<Structure> findStructuresByPrefixe(String prefixe);
	
	public List<Structure> findListeStrFiltreLdap(String filtreLdap) throws ToutaticeAnnuaireException;

	public boolean contientProfil(String dnProfil);

	public List<Application> findApplications();

	public void addResentice(String dn) throws ToutaticeAnnuaireException;

	public void removeResentice(String dn) throws ToutaticeAnnuaireException;

	public void addDaip(String dn) throws ToutaticeAnnuaireException;

	public void removeDaip(String dn) throws ToutaticeAnnuaireException;

	/**
	 * Création d'une structure dans l'annuaire
	 * @throws ToutaticeAnnuaireException 
	 */
	public void create() throws ToutaticeAnnuaireException;

	public void delete() throws ToutaticeAnnuaireException, NamingException;

	public void update() throws ToutaticeAnnuaireException;

}