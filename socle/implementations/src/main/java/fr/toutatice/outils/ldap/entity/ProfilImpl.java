package fr.toutatice.outils.ldap.entity;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.ProfilDao;
import fr.toutatice.outils.ldap.dao.StructureDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Profil issue de l'annuaire LDAP
 * @author aguihomat
 *
 */

@Service(value="profil")
@Scope(value=BeanDefinition.SCOPE_PROTOTYPE)
public class ProfilImpl implements ApplicationContextAware, Profil{

	protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
	protected static ApplicationContext context;
	/**
	 * Factory, à instancier via l'IOC de SPRING
	 */
	@Autowired(required=false)
	private ProfilDao profilDao;
	
	//@Autowired 
	//private StructureDao structureDao;

	private String cn;
	private String displayName;
	private String description;

	
	private List<String> listeMembers = new ArrayList<String>();
	private Attribute listeMembersAttributes;
	
	private List<String> listeExplicitMembers = new ArrayList<String>();
	private List<String> listeManagers = new ArrayList<String>();
	private List<String> listeExplicitManagers = new ArrayList<String>();
	private String filtreRecherche;
	private String type;
	private typePeuplement peuplement;

	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setProfilDao(fr.toutatice.outils.ldap.dao.ProfilDao)
	 */
	public void setProfilDao(ProfilDao profilDao) {
		this.profilDao = profilDao;	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getCn()
	 */
	public String getCn() {
		return cn;	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setCn(java.lang.String)
	 */
	public void setCn(String cn) {
		this.cn = cn;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getDisplayName()
	 */
	public String getDisplayName() {
		return displayName;
	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setDisplayName(java.lang.String)
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getDn()
	 */
	public String getDn() {
		return profilDao.buildFullDn(this.getCn());
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getDescription()
	 */
	public String getDescription() {
		return description;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getType()
	 */
	public String getType() {
		return type;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getPeuplement()
	 */
	public typePeuplement getPeuplement() {
		return peuplement;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setPeuplement(fr.toutatice.outils.ldap.entity.ProfilImpl.typePeuplement)
	 */
	public void setPeuplement(typePeuplement peuplement) {
		this.peuplement = peuplement;	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getListeMembers()
	 */
	public List<String> getListeMembers() throws ToutaticeAnnuaireException {
		
		if(listeMembers.size() < 1){
			List<String> liste = new ArrayList<String>();
			if (this.listeMembersAttributes != null){
				NamingEnumeration<?> m;
				try {
					m = this.listeMembersAttributes.getAll();
					while (m.hasMore()) {
						liste.add((String)m.next());}
				} catch (NamingException e) {
					e.printStackTrace();
					throw new ToutaticeAnnuaireException("Erreur lors de la récupération des membres du profil "+this.getCn());
				}	
			}
			this.setListeMembers(liste);
		}
		return listeMembers;
	}



	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setListeMembers(java.util.List)
	 */
	public void setListeMembers(List<String> listeMembers) {
		this.listeMembers = listeMembers;
	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#addMember(java.lang.String)
	 */
	public void addMember(String dnMember) throws ToutaticeAnnuaireException{ 
		boolean existe=false;
		for(String s:this.getListeMembers()){
			if (s.equalsIgnoreCase(dnMember)){
				existe=true;
			}
		}
		if(!existe){
			this.listeMembers.add(dnMember);
		} else {
			logger.info("Erreur : ajout du membre "+dnMember+" du profil "+this.getCn()+" - Ce dn existe déjà dans la liste des membres");
		}
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getListeMembersAttributes()
	 */
	public Attribute getListeMembersAttributes() {
		return listeMembersAttributes;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setListeMembersAttributes(javax.naming.directory.Attribute)
	 */
	public void setListeMembersAttributes(Attribute listeMembersAttributes) {
		this.listeMembersAttributes = listeMembersAttributes;	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#removeMember(java.lang.String)
	 */
	public void removeMember(String dnMember) throws ToutaticeAnnuaireException{ 
		List<String> liste = new ArrayList<String>();
		for(String s : this.getListeMembers()) {
			if (!s.equalsIgnoreCase(dnMember)) {
				liste.add(s);
			}
		}
		if(this.getListeMembers().size()==liste.size()){
			logger.info("Erreur : suppression du membre "+dnMember+" du profil "+this.getCn()+" - Ce dn n'existe dans la liste des membres");
		}
		else {
			this.removeMemberFromMemberAttributes(dnMember);
			this.setListeMembers(liste);
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#removeMemberFromMemberAttributes(java.lang.String)
	 */
	public void removeMemberFromMemberAttributes(String dnMember){
		if(this.getListeMembersAttributes()!=null){
			this.getListeMembersAttributes().remove(dnMember);
		}
	}

	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#isMember(java.lang.String)
	 */
	public boolean isMember(String dnMember){
		return profilDao.isMember(this, dnMember);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getListeExplicitMembers()
	 */
	public List<String> getListeExplicitMembers() {
		return listeExplicitMembers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setListeExplicitMembers(java.util.List)
	 */
	public void setListeExplicitMembers(List<String> listeExplicitMembers) {
		this.listeExplicitMembers = listeExplicitMembers;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#addExplicitMember(java.lang.String)
	 */
	public void addExplicitMember(String dnExplicitMember) throws ToutaticeAnnuaireException{
		if(this.getPeuplement().equals(typePeuplement.MIXTE)){
			boolean existe=false;
			for(String s:this.getListeExplicitMembers()){
				if (s.equalsIgnoreCase(dnExplicitMember)){
					existe=true;
				}
			}
			for(String s:this.getListeMembers()){
				if (s.equalsIgnoreCase(dnExplicitMember)){
					existe=true;
				}
			}
			
			if(!existe){
				this.listeExplicitMembers.add(dnExplicitMember);
				this.listeMembers.add(dnExplicitMember);
			} else {
				logger.info("Erreur : ajout du membre explicite "+dnExplicitMember+" du profil "+this.getCn()+" - Ce dn existe déjà dans la liste des membres explicites");
			}
		}
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#removeExplicitMember(java.lang.String)
	 */
	public void removeExplicitMember(String dnExplicitMember) throws ToutaticeAnnuaireException{
			List<String> liste = new ArrayList<String>();
			for(String s : this.getListeExplicitMembers()) {
				if (!s.equalsIgnoreCase(dnExplicitMember)) {
					liste.add(s);
				}
			}
			if(this.getListeExplicitMembers().size()==liste.size()){
				logger.info("Erreur : suppression du membre explicite "+dnExplicitMember+" du profil "+this.getCn()+" - Ce dn n'existe dans la liste des membres explicites");
			} else {
				this.removeMember(dnExplicitMember);
			}
			this.setListeExplicitMembers(liste);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#isExplicitMember(java.lang.String)
	 */
	public boolean isExplicitMember(String dnMember){
		if(this.getPeuplement().equals(typePeuplement.MIXTE)){
			boolean existe = false;
			for(String s : this.getListeExplicitMembers()){
				if(s.equalsIgnoreCase(dnMember)){
					existe=true;
				}
			}
			return existe;
		}else{
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getListeManagers()
	 */
	public List<String> getListeManagers() {
		return listeManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setListeManagers(java.util.List)
	 */
	public void setListeManagers(List<String> listeManagers) {
		this.listeManagers = listeManagers;
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getListeExplicitManagers()
	 */
	public List<String> getListeExplicitManagers() {
		return listeExplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setListeExplicitManagers(java.util.List)
	 */
	public void setListeExplicitManagers(List<String> listeExplicitManagers) {
		this.listeExplicitManagers = listeExplicitManagers;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#addExplicitManager(java.lang.String)
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
			logger.info("Erreur : ajout du manager explicite"+dnManager+" au profil "+this.getCn()+" - Ce dn existe déjà dans la liste des managers");
		}
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#removeExplicitManager(java.lang.String)
	 */
	public void removeExplicitManager(String dnManager) {
		List<String> liste = new ArrayList<String>();
		for(String s : this.getListeExplicitManagers()) {
			if (!s.equalsIgnoreCase(dnManager)) {
				liste.add(s);
			}
		}
		if(this.getListeExplicitManagers().size()==liste.size()){
			logger.info("Erreur : suppression du manager explicite"+dnManager+" du profil "+this.getCn()+" - Ce dn n'existe dans la liste des managers explicites");
		}
		this.setListeExplicitManagers(liste);
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#getFiltreRecherche()
	 */
	public String getFiltreRecherche() {
		return filtreRecherche;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setFiltreRecherche(java.lang.String)
	 */
	public void setFiltreRecherche(String filtreRecherche) {
		this.filtreRecherche = filtreRecherche;	}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#isManagedBy(fr.toutatice.outils.ldap.entity.Person)
	 */
	public boolean isManagedBy(Person p){
		boolean existe = false;
		for(String s : this.getListeManagers()){
			if(s.equalsIgnoreCase(p.getDn())){
				existe=true;
			}
		}
		for(String s : this.getListeExplicitManagers()){
			if(s.equalsIgnoreCase(p.getDn())){
				existe=true;
			}
		}
		if (!existe) {
			for(String profil : p.getListeProfils()) {
				for(String s : this.getListeManagers()){
					if(s.equalsIgnoreCase(profil)){
						existe=true;
					}
				}
				for(String s : this.getListeExplicitManagers()){
					if(s.equalsIgnoreCase(profil)){
						existe=true;
					}
				}
			}
		}
		return existe;
	}
	
	
	

	public Profil getNewProfil(String cn, String displayName, String description,
			String type, typePeuplement peuplement) {
		Profil p = profilDao.newProfil();
		p.setCn(cn);
		p.setDisplayName(displayName);
		p.setDescription(description);
		p.setType(type);
		p.setPeuplement(peuplement);
		return p;
	}



	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#findFullDn(java.lang.String)
	 */
	public String findFullDn(String cn) {
		return profilDao.buildFullDn(cn);
	}
	
	/**
	 * Recherche d'un profil par son adresse (DN)
	 * @param dn adresse du profil recherché
	 * @return profil recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public Profil findProfilByDn(String dn) 
	{
		return profilDao.findByDn(dn);
	}
	
	/**
	 * Recherche d'un profil par son nom
	 * @param cn nom exact du profil recherché
	 * @return profil recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public Profil findProfilByCn(String cn)
	{
		Profil p;
		p = profilDao.findByPrimaryKey(cn);
		return p;
	}
	
	/**
	 * Recherche de profils par nom
	 * @param cn début du nom des profils recherchés
	 * @return liste des profils correspondant aux critères
	 */
	public List<Profil> findProfilByDebutCn(String cn)
	{
		return profilDao.getProfilByCn(cn);
	}
	
	public List<Profil> findProfilByRneNom(String rne, String cn) {
		return profilDao.getProfilByRneNom(rne, cn);
	}
	
	/**
	 * Recherche de profils via un filtre LDAP
	 * @param filtre filtre LDAP
	 * @return profils correspondant au filtre
	 */
	public List<Profil> findProfilByFiltre(String filtre)
	{
		return	profilDao.getProfilByFiltre(filtre);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#create()
	 */
	public void create() throws ToutaticeAnnuaireException{
		profilDao.create(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#delete()
	 */
	public void delete() throws ToutaticeAnnuaireException{
		profilDao.delete(this);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#updateProfil()
	 */
	public void updateProfil() throws ToutaticeAnnuaireException
	{
		profilDao.updateProfil(this);
	}
	

	
	/**
	 * Recherche des profils d'une personne
	 * @param p personne dont on recherche les profils
	 * @return liste des objets profils associés à la personne
	 */
	public List<Profil> findProfilsPersonne(Person p) {
		List<Profil> listeProfils = new ArrayList<Profil>();
		for (String profil:p.getListeProfils()) {
	
				Profil pr = this.findProfilByDn(profil);
				if (pr != null) {
					listeProfils.add(pr);
				}
		}
		return listeProfils;
	}
	
	/**
	 * Recherche de la liste des classes d'un établissement
	 * @param rne
	 * @return liste des profils des classes de l'établissement
	 */
	public List<Profil> findListeClassesEtb(String rne) {
		String filtre="(&(objectClass=ENTClasse)(cn="+rne+"*))";
		return	profilDao.getProfilByFiltre(filtre);
	}
	
	/**
	 * Recherche de la classe d'un élève
	 * @param eleve
	 * @return profil de la classe de l'élève
	 */
	public Profil findClasseEleve(Person eleve) {
		return profilDao.findClasseEleve(eleve);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#findOrganisationsLiees()
	 */
	public List<Structure> findOrganisationsLiees(){
			StructureDao structureDao = (StructureDao) context.getBean("structureDao");
			return structureDao.findStructuresLieesProfil(this.getDn());
		}


	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Profil#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.context = ctx;

	}
}
