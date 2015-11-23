package fr.toutatice.outils.ldap.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.dao.EtablissementDao;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Service(value = "etablissement")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class EtablissementImpl extends StructureImpl implements Etablissement{
	
	/**
	 * Factory, à instancier via l'IOC de SPRING
	 */
	@Autowired(required = false)
	@Qualifier("etablissementDao")
	private EtablissementDao etablissementDao;  
	
	private String ministereTutelle;
	private String contrat;
	private String bassin;
	private List<String> listeClasses = new ArrayList<String>();
	private List<String> listeGroupes = new ArrayList<String>();
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#getEtablissementDao()
	 */
	public EtablissementDao getEtablissementDao() {
		return etablissementDao;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#setEtablissementDao(fr.toutatice.outils.ldap.dao.EtablissementDao)
	 */
	public void setEtablissementDao(EtablissementDao etablissementDao) {
		this.etablissementDao = etablissementDao;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#getDn()
	 */
	@Override
	public String getDn() {
		return etablissementDao.buildFullDn(this.getId());	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#getMinistereTutelle()
	 */
	public String getMinistereTutelle() {
		return ministereTutelle;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#setMinistereTutelle(java.lang.String)
	 */
	public void setMinistereTutelle(String ministereTutelle) {
		this.ministereTutelle = ministereTutelle;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#getContrat()
	 */
	public String getContrat() {
		return contrat;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#setContrat(java.lang.String)
	 */
	public void setContrat(String contrat) {
		this.contrat = contrat;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#getBassin()
	 */
	public String getBassin() {
		return bassin;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#setBassin(java.lang.String)
	 */
	public void setBassin(String bassin) {
		this.bassin = bassin;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#getListeClasses()
	 */
	public List<String> getListeClasses() {
		return listeClasses;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#setListeClasses(java.util.List)
	 */
	public void setListeClasses(List<String> listeClasses) {
		this.listeClasses = listeClasses;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#getListeGroupes()
	 */
	public List<String> getListeGroupes() {
		return listeGroupes;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.entity.Etablissement#setListeGroupes(java.util.List)
	 */
	public void setListeGroupes(List<String> listeGroupes) {
		this.listeGroupes = listeGroupes;
	}

	public Etablissement findEtablissementsByRne(String rne){
		return etablissementDao.findEtablissementByRne(rne);
	}
	
	public List<Etablissement> findAllEtablissements() {
		return etablissementDao.findAllEtablissements();
	}
	
	public List<Etablissement> findListeEtbFiltreLdap(String filtreLdap) throws ToutaticeAnnuaireException{
		return etablissementDao.findListeEtbFiltreLdap(filtreLdap);
	}
	
	public List<Etablissement> findEtablissementsMultiCriteres(String ministere, String type, String contrat, String bassin, String rne, String nom, String ville, String critereTri){
		return etablissementDao.findEtablissementsMultiCriteres(ministere, type, contrat, bassin, rne, nom, ville, critereTri);
	}
	
	public List<Etablissement> findEtablissementsMultiCriteres(String ministere, List<String> listeType, String contrat, String bassin, String rne, String nom, String ville, String critereTri){
		return etablissementDao.findEtablissementsMultiCriteres(ministere, listeType, contrat, bassin, rne, nom, ville, critereTri);
	}
	
}
