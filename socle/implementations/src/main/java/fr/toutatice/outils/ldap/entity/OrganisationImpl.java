package fr.toutatice.outils.ldap.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.OrganisationDao;
import fr.toutatice.outils.ldap.dao.OrganisationDao;
import fr.toutatice.outils.ldap.dao.ProfilDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Service(value = "organisation")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class OrganisationImpl implements ApplicationContextAware, Organisation{
	
	protected static ApplicationContext context;
	
		protected static final Log logger = LogFactory.getLog("fr.toutatice.outils.ldap");
		
		/**
		 * Factory, à instancier via l'IOC de SPRING
		 */
		@Autowired(required=false)
		@Qualifier("organisationDao")
		private OrganisationDao organisationDao; 
		
		// ATTRIBUTS OBLIGATOIRES
		/**
		 * Identifiant de l'organsiation
		 */
		private String id;    			
		
		private String displayName;
		/**
		 * Description de l'organsiation
		 */
		private String description;		
		/**
		 * Localisation de l'organisation
		 */
		private String localisation;
		private String codePostal;
		private String adresse;
		private String telephone;
		private String numeroFax;
		
		private String seeAlso;

		
		/**
		 * Liste des DN des profils affectés à l'organisation
		 */
		private List<String> listeProfils = new ArrayList<String>();
		
		/**
		 * Liste des DN des managers affectés à l'organsiation
		 */
		private List<String> listeManagers = new ArrayList<String>();
		private List<String> listeExplicitManagers = new ArrayList<String>();
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getId()
		 */
		public String getId() {
			return id;		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setId(java.lang.String)
		 */
		public void setId(String id) {
			this.id = id;		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getDescription()
		 */
		public String getDescription() {
			return description;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setDescription(java.lang.String)
		 */
		public void setDescription(String description) {
			this.description = description;		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getLocalisation()
		 */
		public String getLocalisation() {
			return localisation;		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getCodePostal()
		 */
		public String getCodePostal() {
			return codePostal;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setCodePostal(java.lang.String)
		 */
		public void setCodePostal(String codePostal) {
			this.codePostal = codePostal;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getAdresse()
		 */
		public String getAdresse() {
			return adresse;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setAdresse(java.lang.String)
		 */
		public void setAdresse(String adresse) {
			this.adresse = adresse;
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getTelephone()
		 */
		public String getTelephone() {
			return telephone;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setTelephone(java.lang.String)
		 */
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getNumeroFax()
		 */
		public String getNumeroFax() {
			return numeroFax;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setNumeroFax(java.lang.String)
		 */
		public void setNumeroFax(String numeroFax) {
			this.numeroFax = numeroFax;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getSeeAlso()
		 */
		public String getSeeAlso() {
			return seeAlso;		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setSeeAlso(java.lang.String)
		 */
		public void setSeeAlso(String seeAlso) {
			this.seeAlso = seeAlso;
		}
		public static Log getLogger() {
			return logger;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getOrganisationDao()
		 */
		public OrganisationDao getOrganisationDao() {
			return organisationDao;
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getDisplayName()
		 */
		public String getDisplayName() {
				return displayName;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setDisplayName(java.lang.String)
		 */
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		

		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setLocalisation(java.lang.String)
		 */
		public void setLocalisation(String localisation) {
			this.localisation = localisation;
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setOrganisationDao(fr.toutatice.outils.ldap.dao.OrganisationDao)
		 */
		public void setOrganisationDao(OrganisationDao organisationDao) {
			this.organisationDao = organisationDao;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getListeProfils()
		 */
		public List<String> getListeProfils() {
			return listeProfils;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setListeProfils(java.util.List)
		 */
		public void setListeProfils(List<String> listeProfils) {
			this.listeProfils = listeProfils;
		}

		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#addProfil(java.lang.String)
		 */
		public void addProfil(String dnProfil){
			String dnProfilLowerCase = dnProfil.toLowerCase();
			List<String> liste = new ArrayList<String>();
			for(String s : this.listeProfils){
				liste.add(s.toLowerCase());
			}
			if(!liste.contains(dnProfilLowerCase)){
				this.listeProfils.add(dnProfil);
			}
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#removeProfil(java.lang.String)
		 */
		public void removeProfil(String dnProfil){
			List<String> liste = new ArrayList<String>();
			for(String s : this.getListeProfils()) {
				if (!s.equalsIgnoreCase(dnProfil)) {
					liste.add(s);
				}
			}
			this.setListeProfils(liste);
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getListeManagers()
		 */
		public List<String> getListeManagers() {
			return listeManagers;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setListeManagers(java.util.List)
		 */
		public void setListeManagers(List<String> listeManagers) {
			this.listeManagers = listeManagers;
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#addManager(java.lang.String)
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
			}
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#removeManager(java.lang.String)
		 */
		public void removeManager(String dnManager) {
			List<String> liste = new ArrayList<String>();
			for(String s : this.getListeManagers()) {
				if (!s.equalsIgnoreCase(dnManager)) {
					liste.add(s);
				}
			}
			this.setListeManagers(liste);
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#getListeExplicitManagers()
		 */
		public List<String> getListeExplicitManagers() {
			return listeExplicitManagers;
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setListeExplicitManagers(java.util.List)
		 */
		public void setListeExplicitManagers(List<String> listeExplicitManagers) {
			this.listeExplicitManagers = listeExplicitManagers;
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#addExplicitManager(java.lang.String)
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
			}
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#removeExplicitManager(java.lang.String)
		 */
		public void removeExplicitManager(String dnManager) {
			List<String> liste = new ArrayList<String>();
			for(String s : this.getListeExplicitManagers()) {
				if (!s.equalsIgnoreCase(dnManager)) {
					liste.add(s);
				}
			}
			this.setListeExplicitManagers(liste);
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#isManagedBy(fr.toutatice.outils.ldap.entity.Person)
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
		
		
		/**
		 * Recherche d'une organsiation par son numéro
		 * @param rne numéro de la structure recherchée (RNE)
		 * @return structure recherchée
		 * @throws ToutaticeAnnuaireException
		 */
		public Organisation findOrganisation(String rne) {
			return organisationDao.findByPrimaryKey(rne);
		}
		
		public Organisation findOrganisationByDn(String dn) throws ToutaticeAnnuaireException {
			Organisation org = null;
			if(dn==null||dn.trim().isEmpty()){
				throw new ToutaticeAnnuaireException("DN non renseigné");
			}
			try {
				org = organisationDao.findByDn(dn);
			} catch (ToutaticeAnnuaireException e) {
				logger.info("organsiation non trouvé dans l'annuaire - recherche par dn : "+dn);
				org = null;
			}
			return org;
		}
		
		public List<Organisation> findOrganisationPersonneByProfil(Person p) {
			return organisationDao.findOrganisationPersonneByProfil(p);
		}
		
		public List<Organisation> findAllOrganisations() {
			return organisationDao.findAllOrganisations();
		}

		public List<Organisation> findOrganisationByPrefixe(String prefixe) {
			return organisationDao.findOrganisationByPrefixe(prefixe);
		}
		
		public List<Organisation> findOrganisationsMultiCriteres(String rne, String nom){
			return organisationDao.findOrganisationsMultiCriteres(rne,nom);
		}
		
		public boolean isEtablissement(){
			Etablissement etablissementInstance = (Etablissement) context.getBean("etablissement");
			Etablissement etb = etablissementInstance.findEtablissementsByRne(this.getId());
			if(etb!=null){
				return true;
			}else{
				return false;
			}
		}
		
		
		public List<Organisation> findFromFiltre(String filtre, String critereTri){
			return organisationDao.findFromFiltre(filtre, critereTri);
		}
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#findProfilsMission()
		 */
		public List<Profil> findProfilsMission(){
			String filtre="(&(objectClass=ENTProfil)(cn="+this.id+"_*)(ENTProfilType=mission))";
			ProfilDao profilDao = (ProfilDao) context.getBean("profilDao");
			return	profilDao.getProfilByFiltre(filtre);
		}
		

		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#contientProfil(java.lang.String)
		 */
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
		 * @see fr.toutatice.outils.ldap.entity.Organisation#contientProfil(java.util.List)
		 */
		public boolean contientProfil(List<String> listeDnProfils){
			boolean trouve = false;
			for(String s:listeDnProfils){
				trouve=trouve||this.contientProfil(s);
			}
			
			return trouve;
		}
		
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#update()
		 */
		public void update()throws ToutaticeAnnuaireException{
			organisationDao.update(this);
		}
		
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.entity.Organisation#setApplicationContext(org.springframework.context.ApplicationContext)
		 */
		@SuppressWarnings("static-access")
		public void setApplicationContext(ApplicationContext ctx)
				throws BeansException {
			this.context = ctx;

		}
		

}
