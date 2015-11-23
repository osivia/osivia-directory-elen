package fr.toutatice.outils.ldap.entity;


import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.StructureDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Structure issue de l'annuaire LDAP
 * @author aguihomat
 *
 */

@Service(value="structure")
@Scope(value=BeanDefinition.SCOPE_PROTOTYPE)
public class StructureImpl extends OrganisationImpl implements Structure{
	
	
	/**
	 * Factory, à instancier via l'IOC de SPRING
	 */
	@Autowired(required=false)
	@Qualifier("structureDao")
	private StructureDao structureDao;  
	
	
	private String noRecepisseCNIL;
	private String typestructure;
	private String nomResponsable;
	private String siren;
	private String email;
	private String siteWeb;
	private String contactENT;
	private String numeroUAI;
	private String latitude;
	private String longitude;
	private ArrayList<String> listeDnResentice = new ArrayList<String>();
	private ArrayList<String> listeDnDaip = new ArrayList<String>();
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getStructureDao()
	 */
	public StructureDao getStructureDao() {
		return structureDao;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setStructureDao(fr.toutatice.outils.ldap.dao.StructureDao)
	 */
	public void setStructureDao(StructureDao structureDao) {
		this.structureDao = structureDao;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getDn()
	 */
	public String getDn() {
		return structureDao.buildFullDn(this.getId());	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getNoRecepisseCNIL()
	 */
	public String getNoRecepisseCNIL() {
		return noRecepisseCNIL;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setNoRecepisseCNIL(java.lang.String)
	 */
	public void setNoRecepisseCNIL(String noRecepisseCNIL) {
		this.noRecepisseCNIL = noRecepisseCNIL;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getTypestructure()
	 */
	public String getTypestructure() {
		return typestructure;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setTypestructure(java.lang.String)
	 */
	public void setTypestructure(String typestructure) {
		this.typestructure = typestructure;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getNomResponsable()
	 */
	public String getNomResponsable() {
		return nomResponsable;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setNomResponsable(java.lang.String)
	 */
	public void setNomResponsable(String responsable) {
		this.nomResponsable = responsable;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getSiren()
	 */
	public String getSiren() {
		return siren;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setSiren(java.lang.String)
	 */
	public void setSiren(String siren) {
		this.siren = siren;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getEmail()
	 */
	public String getEmail() {
		return email;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setEmail(java.lang.String)
	 */
	public void setEmail(String email) {
		this.email = email;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getSiteWeb()
	 */
	public String getSiteWeb() {
		return siteWeb;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setSiteWeb(java.lang.String)
	 */
	public void setSiteWeb(String siteWeb) {
		this.siteWeb = siteWeb;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getContactENT()
	 */
	public String getContactENT() {
		return contactENT;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setContactENT(java.lang.String)
	 */
	public void setContactENT(String contact) {
		this.contactENT = contact;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getNumeroUAI()
	 */
	public String getNumeroUAI() {
		return numeroUAI;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setNumeroUAI(java.lang.String)
	 */
	public void setNumeroUAI(String numeroUAI) {
		this.numeroUAI = numeroUAI;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getLatitude()
	 */
	public String getLatitude() {
		return latitude;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setLatitude(java.lang.String)
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getLongitude()
	 */
	public String getLongitude() {
		return longitude;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setLongitude(java.lang.String)
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;	}	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getListeDnResentice()
	 */
	public ArrayList<String> getListeDnResentice() {
		return listeDnResentice;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setListeDnResentice(java.util.ArrayList)
	 */
	public void setListeDnResentice(ArrayList<String> listeDnResentice) {
		this.listeDnResentice = listeDnResentice;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#getListeDnDaip()
	 */
	public ArrayList<String> getListeDnDaip() {
		return listeDnDaip;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#setListeDnDaip(java.util.ArrayList)
	 */
	public void setListeDnDaip(ArrayList<String> listeDnDaip) {
		this.listeDnDaip = listeDnDaip;		}
	
	/**
	 * Recherche d'une structure par son numéro
	 * @param rne numéro de la structure recherchée (RNE)
	 * @return structure recherchée
	 * @throws ToutaticeAnnuaireException
	 */
	public Structure findStructure(String rne) {
		return structureDao.findByPrimaryKey(rne);
	}
	
	
	public List<Structure> findStructuresPersonneByProfil(Person p) {
		return structureDao.findStructurePersonneByProfil(p);
	}
	
	public List<Structure> findAllStructures() {
		return structureDao.findAllStructures();
	}

	public List<Structure> findStructuresByPrefixe(String prefixe) {
		return structureDao.findStructuresByPrefixe(prefixe);
	}
	
	public List<Structure> findListeStrFiltreLdap(String filtreLdap) throws ToutaticeAnnuaireException{
		return structureDao.findListeStrFiltreLdap(filtreLdap);
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#contientProfil(java.lang.String)
	 */
	@Override
	public boolean contientProfil(String dnProfil){
		boolean trouve = false;
		for(String s:this.getListeProfils()){
			if(s.equalsIgnoreCase(dnProfil)){
				trouve=true;
			}
		}
		return trouve;
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#findApplications()
	 */
	public List<Application> findApplications(){
		Application application = (Application) context.getBean("application");
		return application.findListeApplisStructure(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#addResentice(java.lang.String)
	 */
	public void addResentice(String dn) throws ToutaticeAnnuaireException{
		if(dn==null||dn.trim().isEmpty()){
			throw new ToutaticeAnnuaireException("Dn non renseigné");
		}
		boolean existe=false;
		for(String s:this.getListeDnResentice()){
			if(dn.equalsIgnoreCase(s)){
				existe=true;
			}
		}
		if(!existe){
			this.getListeDnResentice().add(dn);
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#removeResentice(java.lang.String)
	 */
	public void removeResentice(String dn) throws ToutaticeAnnuaireException{
		if(dn==null||dn.trim().isEmpty()){
			throw new ToutaticeAnnuaireException("Dn non renseigné");
		}
		ArrayList<String> liste = new ArrayList<String>();
		for(String s: this.getListeDnResentice()){
			if(!dn.equalsIgnoreCase(s)){
				liste.add(s);
			}
		}
		this.setListeDnResentice(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#addDaip(java.lang.String)
	 */
	public void addDaip(String dn) throws ToutaticeAnnuaireException{
		if(dn==null||dn.trim().isEmpty()){
			throw new ToutaticeAnnuaireException("Dn non renseigné");
		}
		boolean existe=false;
		for(String s:this.getListeDnDaip()){
			if(dn.equalsIgnoreCase(s)){
				existe=true;
			}
		}
		if(!existe){
			this.getListeDnDaip().add(dn);
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#removeDaip(java.lang.String)
	 */
	public void removeDaip(String dn) throws ToutaticeAnnuaireException{
		if(dn==null||dn.trim().isEmpty()){
			throw new ToutaticeAnnuaireException("Dn non renseigné");
		}
		ArrayList<String> liste = new ArrayList<String>();
		for(String s: this.getListeDnDaip()){
			if(!dn.equalsIgnoreCase(s)){
				liste.add(s);
			}
		}
		this.setListeDnDaip(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#create()
	 */
	public void create() throws ToutaticeAnnuaireException
	{
		structureDao.create(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#delete()
	 */
	public void delete() throws ToutaticeAnnuaireException, NamingException
	{
		structureDao.delete(this);
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Structure#update()
	 */
	@Override
	public void update() throws ToutaticeAnnuaireException{
		structureDao.update(this);
	}
	
	
}
