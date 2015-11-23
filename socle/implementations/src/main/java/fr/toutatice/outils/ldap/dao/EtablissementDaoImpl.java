package fr.toutatice.outils.ldap.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.control.SortControlDirContextProcessor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.support.AggregateDirContextProcessor;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

import fr.toutatice.outils.ldap.entity.Etablissement;
import fr.toutatice.outils.ldap.entity.Structure;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

@Repository
@Qualifier("etablissementDao")
@Scope("singleton")
public class EtablissementDaoImpl extends StructureDaoImpl implements EtablissementDao{

	private static String categorieLDAP;
	private static String classObjetEtablissement ="";
	private static String BASE_DN="";
	private static String ministereTutelle;
	private static String bassin;
	private static String contrat;
	private static String classes;
	private static String groupes;
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#setCategorieLDAP(java.lang.String)
	 */
	public void setCategorieLDAP(String s) {
		categorieLDAP = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#setClasseObjetEtablissement(java.lang.String)
	 */
	public void setClasseObjetEtablissement(String s) {
		classObjetEtablissement = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#setBASE_DN(java.lang.String)
	 */
	public void setBASE_DN(String s){
		BASE_DN=s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#setMinistereTutelle(java.lang.String)
	 */
	public void setMinistereTutelle(String s) {
		ministereTutelle = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#setBassin(java.lang.String)
	 */
	public void setBassin(String s) {
		bassin = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#setContrat(java.lang.String)
	 */
	public void setContrat(String s) {
		contrat = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#setClasses(java.lang.String)
	 */
	public void setClasses(String s) {
		classes = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#setGroupes(java.lang.String)
	 */
	public void setGroupes(String s) {
		groupes = s;	}
	
	
	// Classe interne permettant de récupérer un etablissement dans l'annuaire
	protected static class EtablissementAttributMapper extends StructureAttributMapper implements AttributesMapper {
		
		public Object mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {

			Etablissement etb = (Etablissement) context.getBean("etablissement");
	
			etb = (Etablissement) StructureDaoImpl.valoriser((Structure)etb,attrs);
			etb = (Etablissement) EtablissementDaoImpl.valoriser(etb, attrs);
			
			
			return etb;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Object valoriser(Etablissement etb, Attributes attrs) throws javax.naming.NamingException {
		
		
		Attribute attr;
		
		if (attrs.get(ministereTutelle) != null)
		{ etb.setMinistereTutelle(attrs.get(ministereTutelle).get().toString());}
		else {
			etb.setMinistereTutelle("");
		}
		if (attrs.get(contrat) != null)
		{ etb.setContrat(attrs.get(contrat).get().toString());}
		else {
			etb.setContrat("");
		}
		if (attrs.get(bassin) != null)
		{ etb.setBassin(attrs.get(bassin).get().toString());}
		else {
			etb.setBassin("");
		}
		attr = attrs.get(classes);
		if(attr != null) {
			NamingEnumeration m = (attr.getAll());
			while(m.hasMore()) {
				etb.getListeClasses().add((String)m.next());
			}
		}
		attr = attrs.get(groupes);
		if(attr != null) {
			NamingEnumeration m = (attr.getAll());
			while(m.hasMore()) {
				etb.getListeGroupes().add((String)m.next());
			}
		}

		return etb;
	}

	protected Attributes buildAttributes(Etablissement etb) {
	     Attributes attrs = super.buildAttributes((Structure)etb);
	     Attribute attr = attrs.get("objectclass");
	     attr.add(classObjetEtablissement);
	     if(etb.getMinistereTutelle()!=null&&!etb.getMinistereTutelle().trim().isEmpty()){
	    	 attrs.put(ministereTutelle, etb.getMinistereTutelle());
	     }
	     if(etb.getContrat()!=null&&!etb.getContrat().trim().isEmpty()){
	    	 attrs.put(contrat,etb.getContrat());
	     }
	     if(etb.getBassin()!=null&&!etb.getBassin().trim().isEmpty()){
	    	 attrs.put(bassin,etb.getBassin());
	     }
	     
	     if(etb.getListeClasses().size()>0){
		     BasicAttribute classesattr = new BasicAttribute(classes);
		      for (String s : etb.getListeClasses())
		      {
		    	  classesattr.add(s);
		      }
		      attrs.put(classesattr);
	     }
	      
	     if(etb.getListeGroupes().size()>0){
		      BasicAttribute groupesattr = new BasicAttribute(groupes);
		      for (String s : etb.getListeGroupes())
		      {
		    	  groupesattr.add(s);
		      }
		      attrs.put(groupesattr);
	     }
	      return attrs;
	}
	
	/**
	 * Construit le DN d'un établissement à partir de son numéro
	 * @param rne numéro de l'établissement
	 * @return DN de l'établissement (sans la base de l'annuaire LDAP, défini dans le fichier properties)
	 */
	protected Name buildDn(String rne) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou", categorieLDAP);
		dn.add(super.getId(), rne);
		return dn;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#buildFullDn(java.lang.String)
	 */
	public String buildFullDn(String rne) {
		if(rne.trim().isEmpty()){
			return null;
		} else {
			String dn= this.buildDn(rne).toString() + "," + BASE_DN;
			return dn;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#findEtablissementByRne(java.lang.String)
	 */
	@Cacheable(key = "#rne", value = { "etablissementByPrimaryKeyCache" })
	public Etablissement findEtablissementByRne(String rne){
	Etablissement etb = null;
		
		if(!rne.trim().isEmpty()){
			AndFilter filter = new AndFilter();
			filter.and(new EqualsFilter("objectclass", classObjetEtablissement));
			filter.and(new EqualsFilter(id, rne));
			EtablissementAttributMapper etbAttributMapper = new EtablissementAttributMapper();
			List<Etablissement> liste = ldapTemplateLecture.search("", filter.encode(),SearchControls.SUBTREE_SCOPE ,etbAttributMapper);
			if (liste.size()==1){
				etb = liste.get(0);
			} 
		}
		return etb;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#findAllEtablissements()
	 */
	@SuppressWarnings("unchecked")
	public List<Etablissement> findAllEtablissements() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetEtablissement));
		filter.and(new LikeFilter(id, "*"));
		EtablissementAttributMapper etbAttributMapper = new EtablissementAttributMapper();
		return ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(),SearchControls.ONELEVEL_SCOPE ,etbAttributMapper);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#findListeEtbFiltreLdap(java.lang.String)
	 */
	public List<Etablissement> findListeEtbFiltreLdap(String filtreLdap) throws ToutaticeAnnuaireException {
		if(filtreLdap==null){
			throw new ToutaticeAnnuaireException("paramètre filtreLdap non renseigné");
		}
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetEtablissement));
		
		String filtreGlobal;
		if (!filtreLdap.trim().isEmpty()){
			filtreGlobal = "(&" + filtreLdap.concat(filter.encode()) + ")";
		}
		else{
			filtreGlobal= filter.encode();
		}
		
		EtablissementAttributMapper etbAttributMapper = new EtablissementAttributMapper();
		return ldapTemplateLecture.search("ou="+categorieLDAP, filtreGlobal,SearchControls.ONELEVEL_SCOPE ,etbAttributMapper);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#findEtablissementsMultiCriteres(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Etablissement> findEtablissementsMultiCriteres(String ministere, String type, String contrat, String bassin, String rne, String nom, String ville, String critereTri){
		AggregateDirContextProcessor processor = new AggregateDirContextProcessor() ;
	    SortControlDirContextProcessor sorter = new SortControlDirContextProcessor(critereTri) ;
	    processor.addDirContextProcessor( sorter ) ;
	    SearchControls searchControls = new SearchControls() ;
	    searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE ) ;
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetEtablissement));
		if(!ministere.trim().isEmpty()){
			filter.and(new EqualsFilter(EtablissementDaoImpl.ministereTutelle, ministere));
		}
		if(!type.trim().isEmpty()){
			filter.and(new EqualsFilter(EtablissementDaoImpl.typeStructure, type));
		}
		if(!contrat.trim().isEmpty()){
			filter.and(new EqualsFilter(EtablissementDaoImpl.contrat, contrat));
		}
		if(!bassin.trim().isEmpty()){
			filter.and(new EqualsFilter(EtablissementDaoImpl.bassin, bassin));
		}
		if(rne!=null){
			rne=rne.replace("*", "");
			filter.and(new LikeFilter(id, rne+"*"));
		}
		if(!nom.trim().isEmpty()){
			nom=nom.replace("*", "");
			filter.and(new LikeFilter(displayName, "*"+nom+"*"));
		}
		if(!ville.trim().isEmpty()){
			ville=ville.replace("*","");
			filter.and(new LikeFilter(localisation, ville+"*"));
		}
		EtablissementAttributMapper etbAttributMapper = new EtablissementAttributMapper();
		
		return ldapTemplateLecture.search("", filter.encode(),searchControls,etbAttributMapper);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.EtablissementDao#findEtablissementsMultiCriteres(java.lang.String, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Etablissement> findEtablissementsMultiCriteres(String ministere, List<String> listeType, String contrat, String bassin, String rne, String nom, String ville, String critereTri){
		AggregateDirContextProcessor processor = new AggregateDirContextProcessor() ;
	    SortControlDirContextProcessor sorter = new SortControlDirContextProcessor(critereTri) ;
	    processor.addDirContextProcessor( sorter ) ;
	    SearchControls searchControls = new SearchControls() ;
	    searchControls.setSearchScope( SearchControls.SUBTREE_SCOPE ) ;
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetEtablissement));
		if(!ministere.trim().isEmpty()){
			filter.and(new EqualsFilter(EtablissementDaoImpl.ministereTutelle, ministere));
		}
		
		if(!listeType.isEmpty()){
			OrFilter orfilter = new OrFilter();
			for(String type:listeType){
				orfilter.or(new EqualsFilter(EtablissementDaoImpl.typeStructure, type));
			}
			filter.and(orfilter);
		}

		
		
		if(!contrat.trim().isEmpty()){
			filter.and(new EqualsFilter(EtablissementDaoImpl.contrat, contrat));
		}
		if(!bassin.trim().isEmpty()){
			filter.and(new EqualsFilter(EtablissementDaoImpl.bassin, bassin));
		}
		if(rne!=null){
			rne=rne.replace("*", "");
			filter.and(new LikeFilter(id, rne+"*"));
		}
		if(!nom.trim().isEmpty()){
			nom=nom.replace("*", "");
			filter.and(new LikeFilter(displayName, "*"+nom+"*"));
		}
		if(!ville.trim().isEmpty()){
			ville=ville.replace("*","");
			filter.and(new LikeFilter(localisation, ville+"*"));
		}
		EtablissementAttributMapper etbAttributMapper = new EtablissementAttributMapper();
		
		return ldapTemplateLecture.search("", filter.encode(),searchControls,etbAttributMapper);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#update(fr.toutatice.outils.ldap.entity.Structure)
	 */
	@CacheEvict(value = "etablissementByPrimaryKeyCache", key = "#etb.id")
	public void update(Etablissement etb) throws ToutaticeAnnuaireException {

		Name dn = this.buildDn(etb.getId());

		DirContextOperations context = ldapTemplateEcriture.lookupContext(dn);
		
		context = super.prepareContextForUpdate(context, etb);
		//context = this.prepareContextForUpdate(context, etb);

		ldapTemplateEcriture.modifyAttributes(context);


	}
	
	
	

}
