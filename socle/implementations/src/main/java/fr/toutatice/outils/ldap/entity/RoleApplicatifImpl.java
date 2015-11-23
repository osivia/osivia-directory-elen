package fr.toutatice.outils.ldap.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.RoleApplicatifDao;
import fr.toutatice.outils.ldap.entity.Profil.typePeuplement;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Role applicatif issu de l'annuaire LDAP
 * @author aguihomat
 *
 */

@Service(value = "roleApplicatif")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class RoleApplicatifImpl implements ApplicationContextAware, RoleApplicatif{
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	private static ApplicationContext context;  
	/**
	 * Factory, à instancier via l'IOC de SPRING
	 */
	@Autowired(required = false)
	private RoleApplicatifDao roleApplicatifDao;
	
	/**
	 * Nom du role applicatif
	 */
	private String cn;
	
	private String displayName;
	/**
	 * Description du role applicatif
	 */
	private String description;
	
	private String type;
	/**
	 * Membres du role applicatif
	 */
	private List<String> listeMembers = new ArrayList<String>();
	/**
	 * Profils inclus dans le role applicatif 
	 */
	private List<String> listeProfils = new ArrayList<String>();
	/**
	 * Propriétaires du role applicatif	
	 */
	private List<String> listeOwners = new ArrayList<String>();
	
	private List<String> listeMemberURL = new ArrayList<String>();
	
	private List<String> listeManagers = new ArrayList<String>();
	
	private List<String> listeExplicitManagers = new ArrayList<String>();
	
	
	
	private String filtreRecherche;
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setRoleApplicatifDao(fr.toutatice.outils.ldap.dao.RoleApplicatifDao)
	 */
	public void setRoleApplicatifDao(RoleApplicatifDao roleApplicatifDao) {
		this.roleApplicatifDao = roleApplicatifDao;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getCn()
	 */
	public String getCn() {
		return cn;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setCn(java.lang.String)
	 */
	public void setCn(String cn) {
		this.cn = cn;}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getDisplayName()
	 */
	public String getDisplayName() {
		return displayName;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getDn()
	 */
	public String getDn() {
		return roleApplicatifDao.buildFullDn(this.getCn());
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getType()
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getFiltreRecherche()
	 */
	public String getFiltreRecherche() {
		return filtreRecherche;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setFiltreRecherche(java.lang.String)
	 */
	public void setFiltreRecherche(String filtreRecherche) {
		this.filtreRecherche = filtreRecherche;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getListeMembers()
	 */
	public List<String> getListeMembers() {
		return listeMembers;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setListeMembers(java.util.List)
	 */
	public void setListeMembers(List<String> listeMembers) {
		this.listeMembers = listeMembers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#addMember(java.lang.String)
	 */
	public void addMember(String dnMember){ 
		boolean existe=false;
		for(String s:this.getListeMembers()){
			if (s.equalsIgnoreCase(dnMember)){
				existe=true;
			}
		}
		if(!existe){
			this.listeMembers.add(dnMember);
		} else {
			logger.error("Erreur : ajout du membre "+dnMember+" au role "+this.getCn()+" - Ce dn existe déjà dans la liste des membres");
		}
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#removeMember(java.lang.String)
	 */
	public void removeMember(String dnMember){ 
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeMembers()) {
			if (!s.equalsIgnoreCase(dnMember)) {
				liste.add(s);
			}
		}
		if(this.getListeMembers().size()==liste.size()){
			logger.error("Erreur : suppression du membre "+dnMember+" du role "+this.getCn()+" - Ce dn n'existe dans la liste des membres");
		}
		this.setListeMembers(liste);
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#isMemberExplicit(java.lang.String)
	 */
	public boolean isMemberExplicit(String dnMember){
		boolean existe = false;
		for(String s : this.getListeMembers()){
			if(s.equalsIgnoreCase(dnMember)){
				existe=true;
			}
		}
		return existe;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getListeProfils()
	 */
	public List<String> getListeProfils() {
		return listeProfils;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setListeProfils(java.util.List)
	 */
	public void setListeProfils(List<String> listeProfils) {
		this.listeProfils = listeProfils;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#addProfil(java.lang.String)
	 */
	public void addProfil(String dnProfil) {
		boolean existe=false;
		for(String s:this.getListeProfils()){
			if (s.equalsIgnoreCase(dnProfil)){
				existe=true;
			}
		}
		if(!existe){
			this.listeProfils.add(dnProfil);
		} else {
			logger.error("Erreur : ajout du profil "+dnProfil+" au role "+this.getCn()+" - Ce profil est déjà affecté au role");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#removeProfil(java.lang.String)
	 */
	public void removeProfil(String dnProfil) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeProfils()) {
			if (!s.equalsIgnoreCase(dnProfil)) {
				liste.add(s);
			}
		}
		if(this.getListeProfils().size()==liste.size()){
			logger.error("Erreur : suppression du profil "+dnProfil+" du role "+this.getCn()+" - Ce profil n'est pas affecté role");
		}
		this.setListeProfils(liste);
	}
	

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getListeOwners()
	 */
	public List<String> getListeOwners() {
		return listeOwners;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setListeOwners(java.util.List)
	 */
	public void setListeOwners(List<String> listeOwner) {
		this.listeOwners = listeOwner;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#addOwner(java.lang.String)
	 */
	public void addOwner(String dnOwner){ //Ajout uniquement si le owner n'est pas déjà présent dans le profil (case non sensitive)
		boolean existe=false;
		for(String s:this.getListeOwners()){
			if (s.equalsIgnoreCase(dnOwner)){
				existe=true;
			}
		}
		if(!existe){
			this.listeOwners.add(dnOwner);
		} else {
			logger.error("Erreur : ajout du owner "+dnOwner+" du role "+this.getCn()+" - Ce dn existe déjà dans la liste des owners");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#removeOwner(java.lang.String)
	 */
	public void removeOwner(String dnOwner){ 
		ArrayList<String> liste= new ArrayList<String>();
		for(String s:this.getListeOwners()){
			if(!s.equalsIgnoreCase(dnOwner)){
				liste.add(s);
			}
		}
		if(this.getListeOwners().size()==liste.size()){
			logger.error("Erreur : suppression du owner "+dnOwner+" du role "+this.getCn()+" - Ce dn n'existe dans la liste des owners");
		}
		this.setListeOwners(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getListeManagers()
	 */
	public List<String> getListeManagers() {
		return listeManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setListeManagers(java.util.List)
	 */
	public void setListeManagers(List<String> listeManagers) {
		this.listeManagers = listeManagers;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#addManager(java.lang.String)
	 */
	public void addManager(String dnManager) {
		boolean existe=false;
		for(String s:this.getListeManagers()){
			if (s.equalsIgnoreCase(dnManager)){
				existe=true;
			}
		}
		if(!existe){
			this.listeManagers.add(dnManager);
		} else {
			logger.error("Erreur : ajout du manager "+dnManager+" au role "+this.getCn()+" - Ce dn existe déjà dans la liste des managers");
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#removeManager(java.lang.String)
	 */
	public void removeManager(String dnManager) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeManagers()) {
			if (!s.equalsIgnoreCase(dnManager)) {
				liste.add(s);
			}
		}
		if(this.getListeManagers().size()==liste.size()){
			logger.error("Erreur : suppression du manager "+dnManager+" du role "+this.getCn()+" - Ce dn n'existe dans la liste des managers");
		}
		this.setListeManagers(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getListeExplicitManagers()
	 */
	public List<String> getListeExplicitManagers() {
		return listeExplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setListeExplicitManagers(java.util.List)
	 */
	public void setListeExplicitManagers(List<String> listeExplicitManagers) {
		this.listeExplicitManagers = listeExplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#addExplicitManager(java.lang.String)
	 */
	public void addExplicitManager(String dnManager){
		boolean existe=false;
		for(String s:this.getListeManagers()){
			if (s.equalsIgnoreCase(dnManager)){
				existe=true;
			}
		}
		for(String s:this.getListeExplicitManagers()){
			if (s.equalsIgnoreCase(dnManager)){
				existe=true;
			}
		}
		if(!existe){
			this.listeExplicitManagers.add(dnManager);
		} else {
			logger.error("Erreur : ajout du manager explicite"+dnManager+" au rôle "+this.getCn()+" - Ce dn existe déjà dans la liste des managers");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#removeExplicitManager(java.lang.String)
	 */
	public void removeExplicitManager(String dnManager) {
		ArrayList<String> liste = new ArrayList<String>();
		for(String s : this.getListeExplicitManagers()) {
			if (!s.equalsIgnoreCase(dnManager)) {
				liste.add(s);
			}
		}
		if(this.getListeExplicitManagers().size()==liste.size()){
			logger.error("Erreur : suppression du manager explicite"+dnManager+" du rôle "+this.getCn()+" - Ce dn n'existe dans la liste des managers explicites");
		}
		this.setListeExplicitManagers(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#create()
	 */
	public void create() throws ToutaticeAnnuaireException{
		roleApplicatifDao.create(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#delete()
	 */
	public void delete() throws ToutaticeAnnuaireException{
		roleApplicatifDao.delete(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#isManagedBy(fr.toutatice.outils.ldap.entity.Person)
	 */
	public boolean isManagedBy(Person p){
		boolean existe = false;
		//Le dn de l'utilisateur est renseigné comme manager
		for(String s : this.getListeManagers()){
			if(s.equalsIgnoreCase(p.getDn())){
				existe=true;
			}
		}
		if(!existe){
			//Le dn de l'utilisateur est renseigné comme explicit manager
			for(String s : this.getListeExplicitManagers()){
				if(s.equalsIgnoreCase(p.getDn())){
					existe=true;
				}
			}
		}
		if (!existe) {
			//Un des profils du user est renseigné comme manager
			for(String profil : p.getListeProfils()) {
				for(String s : this.getListeManagers()){
					if(s.equalsIgnoreCase(profil)){
						existe=true;
					}
				}		
				//Un des profils du user est renseigné comme manager
				for(String s : this.getListeExplicitManagers()){
					if(s.equalsIgnoreCase(profil)){
						existe=true;
					}
				}
			}
		}
		if(!existe){
			//le user a un role renseigné comme manager 
			for(String s:this.getListeManagers()){
				RoleApplicatif role = roleApplicatifDao.findByDn(s);
				if(role!=null){
					if(p.hasRole(role.getCn())){
						existe = true;
					}
				}
			}
			
		}
		if(!existe){
			//le user a un role renseigné comme explicit manager
			for(String s:this.getListeExplicitManagers()){
				RoleApplicatif role = roleApplicatifDao.findByDn(s);
				if(role!=null){
					if(p.hasRole(role.getCn())){
						existe = true;
					}
				}
			}
			
		}
		return existe;
	}

	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#getListeMemberURL()
	 */
	public List<String> getListeMemberURL() {
		return listeMemberURL;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setListeMemberURL(java.util.List)
	 */
	public void setListeMemberURL(List<String> listeMemberURL) {
		this.listeMemberURL = listeMemberURL;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#addMemberURL(java.lang.String)
	 */
	public void addMemberURL(String url) {
		
		if(!this.getListeMemberURL().contains(url)){
			this.listeMemberURL.add(url);
		} else {
			logger.error("Erreur : ajout du memberURL "+url+" au role "+this.getCn()+" - Cette url existe déjà");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#removeMemberURL(java.lang.String)
	 */
	public void removeMemberURL(String url) {
		
		if(!this.getListeMemberURL().contains(url)){
			logger.error("Erreur : suppression du memberURL "+url+" du role "+this.getCn()+" - Ce memberURL n'existe pas");
		} else {
			this.listeMemberURL.remove(url);
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#findFullDn(java.lang.String)
	 */
	public String findFullDn(String cn) {
		return roleApplicatifDao.buildFullDn(cn);	}

	/**
	 * Recherche de la liste des roles applicatifs d'une personne
	 * @param personne personne sur qui porte la recherche
	 * @return liste des roles applicatifs qui lui sont associés (soit directement, soit via un profil)
	 */
	public List<RoleApplicatif> findListRole(Person personne)	{
		return roleApplicatifDao.findRoleAppli(personne);	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#findListDnRole(fr.toutatice.outils.ldap.entity.Person)
	 */
	public List<String> findListDnRole(Person personne)	{	
		List<RoleApplicatif> listeRoles = roleApplicatifDao.findRoleAppli(personne);
		List<String> listeDn = new ArrayList<String>();
		
		for(RoleApplicatif role : listeRoles)
		{
			listeDn.add(roleApplicatifDao.buildFullDn(role.getCn()));
		}
		return listeDn;
	}
	
	/**
	 * Recherche d'un role applicatif par son adresse dans l'annuaire (DN)
	 * @param dn adresse du role dans l'annuaire
	 * @return role applicatif recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public RoleApplicatif findRoleByDn(String dn)	{
		RoleApplicatif role;
		
		role = roleApplicatifDao.findByDn(dn);
	
		return role;
	}
	
	/**
	 * Recherche d'un role applicatif par son nom
	 * @param cn nom du role recherché
	 * @return role applicatif recherché
	 * @throws ToutaticeAnnuaireException
	 */
	public RoleApplicatif findRoleByCn(String cn) throws ToutaticeAnnuaireException	{
		RoleApplicatif role;
		try {
			role = roleApplicatifDao.findByCn(cn);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
		return role;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#updateRole()
	 */
	public void updateRole() throws ToutaticeAnnuaireException	{
		try {
			roleApplicatifDao.updateRole(this);
		} catch (ToutaticeAnnuaireException e) {
			throw e;
		}
	}
	

	public RoleApplicatif getNewRole() {
		RoleApplicatif role = roleApplicatifDao.getNewRole();
		return role;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.RoleApplicatif#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.context = ctx;

	}
}
