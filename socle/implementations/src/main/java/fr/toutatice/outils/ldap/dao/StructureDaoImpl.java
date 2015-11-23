package fr.toutatice.outils.ldap.dao;

import java.util.List;









import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.SearchControls;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

import fr.toutatice.outils.ldap.dao.EtablissementDaoImpl.EtablissementAttributMapper;
import fr.toutatice.outils.ldap.entity.ApplicationFille;
import fr.toutatice.outils.ldap.entity.Etablissement;
import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Structure;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

/**
 * Classe permettant d'accéder à l'annuaire LDAP, de consulter 
 * et mettre à jour les informations concernant les applications.
 * Les attributs de cette classe sont les libellés des champs dans l'annuaire LDAP
 * Ils sont à renseigner dans un fichier properties et injectés dans cette classe via SPRING
 * @author aguihomat
 *
 */

@Repository
@Qualifier("structureDao")
@Scope("singleton")
public class StructureDaoImpl extends OrganisationDaoImpl implements StructureDao {

	private static String categorieLDAP;
	private static String classObjetStructure ="";
	protected static String noRecepisseCnil;
	protected static String typeStructure;
	protected static String responsable;
	protected static String siren;
	protected static String email;
	protected static String siteweb;
	protected static String contactENT;
	protected static String numeroUAI;
	protected static String geoLoc;
	protected static String resentice;
	protected static String daip;
	private static String BASE_DN="";
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setCategorieLDAP(java.lang.String)
	 */
	public void setCategorieLDAP(String s) {
		categorieLDAP = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setClasseObjetStructure(java.lang.String)
	 */
	public void setClasseObjetStructure(String s) {
		classObjetStructure = s;	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setNoRecepisseCnil(java.lang.String)
	 */
	public void setNoRecepisseCnil(String s) {
		noRecepisseCnil = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setTypeStructure(java.lang.String)
	 */
	public void setTypeStructure(String s) {
		typeStructure = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setResponsable(java.lang.String)
	 */
	public void setResponsable(String s) {
		responsable = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setSiren(java.lang.String)
	 */
	public void setSiren(String s) {
		siren = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setEmail(java.lang.String)
	 */
	public void setEmail(String s) {
		email = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setSiteweb(java.lang.String)
	 */
	public void setSiteweb(String s) {
		siteweb = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setContactENT(java.lang.String)
	 */
	public void setContactENT(String s) {
		contactENT = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setNumeroUAI(java.lang.String)
	 */
	public void setNumeroUAI(String s) {
		numeroUAI = s;	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setGeoLoc(java.lang.String)
	 */
	public void setGeoLoc(String s){
		geoLoc = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setResentice(java.lang.String)
	 */
	public void setResentice(String s){
		resentice = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setDaip(java.lang.String)
	 */
	public void setDaip(String s){
		daip = s;
	}
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setBASE_DN(java.lang.String)
	 */
	public void setBASE_DN(String s){
		BASE_DN=s;
	}

	// Classe interne permettant de récupérer une application dans l'annuaire
	protected static class StructureAttributMapper extends OrganisationAttributMapper implements AttributesMapper {
		
		public Object mapFromAttributes(Attributes attrs)
				throws javax.naming.NamingException {

			Structure str = (Structure) context.getBean("structure");
			
			
			str = (Structure) StructureDaoImpl.valoriser(str,attrs);
			
		
			return str;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Object valoriser(Structure str, Attributes attrs) throws javax.naming.NamingException {
		
		
		Attribute attr;
		
		str = (Structure) OrganisationDaoImpl.valoriser((Organisation)str,attrs);
		
		if (attrs.get(noRecepisseCnil) != null)
		{ str.setNoRecepisseCNIL(attrs.get(noRecepisseCnil).get().toString());}
		else {
			str.setNoRecepisseCNIL("");
		}
		
		if (attrs.get(typeStructure) != null)
		{ str.setTypestructure(attrs.get(typeStructure).get().toString());}
		else {
			str.setTypestructure("");
		}
		
		if (attrs.get(responsable) != null)
		{ str.setNomResponsable(attrs.get(responsable).get().toString());}
		else {
			str.setNomResponsable("");
		}
		
		if (attrs.get(siren) != null)
		{ str.setSiren(attrs.get(siren).get().toString());}
		else {
			str.setSiren("");
		}
		
		if (attrs.get(siteweb) != null)
		{ str.setSiteWeb(attrs.get(siteweb).get().toString());}
		else {
			str.setSiteWeb("");
		}
		
		if (attrs.get(email) != null)
		{ str.setEmail(attrs.get(email).get().toString());}
		else {
			str.setEmail("");
		}
		
		if (attrs.get(numeroUAI) != null)
		{ str.setNumeroUAI(attrs.get(numeroUAI).get().toString());}
		else {
			str.setNumeroUAI("");
		}
		if (attrs.get(contactENT) != null)
		{ str.setContactENT(attrs.get(contactENT).get().toString());}
		else {
			str.setContactENT("");
		}
		if (attrs.get(geoLoc) != null)
		{ 
			String s = attrs.get(geoLoc).get().toString();
			String[] coord = s.split(",");
			if(coord.length==2){
				str.setLatitude(coord[0]);
				str.setLongitude(coord[1]);
			}else{
				str.setLatitude("");
				str.setLongitude("");
			}
		}
		else {
			str.setLatitude("");
			str.setLongitude("");
		}
		
		attr = attrs.get(resentice);
		if (attr != null) {
			NamingEnumeration m = (attr.getAll());
			while (m.hasMore()) {
				try {
					str.addResentice((String)m.next());
				} catch (ToutaticeAnnuaireException e) {
					e.printStackTrace();
				}
			}
		}
		
		attr = attrs.get(daip);
		if (attr != null) {
			NamingEnumeration m = (attr.getAll());
			while (m.hasMore()) {
				try {
					str.addDaip((String)m.next());
				} catch (ToutaticeAnnuaireException e) {
					e.printStackTrace();
				}
			}
		}

		return str;
	}


	protected Attributes buildAttributes(Structure str) {
	     Attributes attrs = super.buildAttributes((Organisation)str);
	     Attribute attr = attrs.get("objectclass");
	     attr.add(classObjetStructure);
	     if(str.getNoRecepisseCNIL()!=null&&!str.getNoRecepisseCNIL().trim().isEmpty()){
	    	 attrs.put(noRecepisseCnil,str.getNoRecepisseCNIL());
	     }
	     if(str.getTypestructure()!=null&&!str.getTypestructure().trim().isEmpty()){
	    	 attrs.put(typeStructure,str.getTypestructure()); 
	     }
	     if(str.getNomResponsable()!=null&&!str.getNomResponsable().trim().isEmpty()){
	    	 attrs.put(responsable,str.getNomResponsable());
	     }
	     if(str.getSiren()!=null&&!str.getSiren().trim().isEmpty()){
	    	 attrs.put(siren,str.getSiren());
	     }
	     if(str.getSiteWeb()!=null&&!str.getSiteWeb().trim().isEmpty()){
	    	 attrs.put(siteweb,str.getSiteWeb());
	     }
	     if(str.getEmail()!=null&&!str.getEmail().trim().isEmpty()){
	    	 attrs.put(email,str.getEmail());
	     }
	     if(str.getContactENT()!=null&&!str.getContactENT().trim().isEmpty()){
	    	 attrs.put(contactENT,str.getContactENT());
	     }
	     if(str.getNumeroUAI()!=null&&!str.getNumeroUAI().trim().isEmpty()){
	     attrs.put(numeroUAI,str.getNumeroUAI());
	     }
	     if(str.getLatitude()!=null&&!str.getLatitude().trim().isEmpty()&&str.getLongitude()!=null&&!str.getLongitude().trim().isEmpty()){
		     attrs.put(geoLoc,str.getLatitude()+","+str.getLongitude());
		     }
	     if(str.getListeDnResentice()!=null && str.getListeDnResentice().size()>0){
		      BasicAttribute resenticeAttr = new BasicAttribute(resentice);
		      for (String s : str.getListeDnResentice())
		      {
		    	  resenticeAttr.add(s);
		      }
		      attrs.put(resenticeAttr);
	      }
	     if(str.getListeDnDaip()!=null && str.getListeDnDaip().size()>0){
		      BasicAttribute daipAttr = new BasicAttribute(daip);
		      for (String s : str.getListeDnResentice())
		      {
		    	  daipAttr.add(s);
		      }
		      attrs.put(daipAttr);
	      }
	     return attrs;
	}
	
	/**
	 * Construit le DN d'une structure à partir de son numéro
	 * @param rne numéro de la structure
	 * @return DN de la structure (sans la base de l'annuaire LDAP, défini dans le fichier properties)
	 */
	protected Name buildDn(String rne) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("ou", categorieLDAP);
		dn.add(super.getId(), rne);
		return dn;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#buildFullDn(java.lang.String)
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
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#findByPrimaryKey(java.lang.String)
	 */
	@Cacheable(key = "#rne", value = { "structureByPrimaryKeyCache" })
	public Structure findByPrimaryKey(String rne) {
		Structure str = null;
		
		if(!rne.trim().isEmpty()){
			Name dn = buildDn(rne);
			try {
				StructureAttributMapper strAttributMapper = new StructureAttributMapper();
				str = (Structure) ldapTemplateLecture.lookup(dn,strAttributMapper);
			} catch (NamingException e) {
				logger.info("La structure "+rne+" n'a pas été retrouvé dans l'annuaire");
			} 
		}
		return str;
	}
	

	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#findAllStructures()
	 */
	@SuppressWarnings("unchecked")
	public List<Structure> findAllStructures() {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetStructure));
		filter.and(new LikeFilter(id, "*"));
		StructureAttributMapper strAttributMapper = new StructureAttributMapper();
		return ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(),SearchControls.ONELEVEL_SCOPE ,strAttributMapper);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#findStructuresByPrefixe(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Structure> findStructuresByPrefixe(String prefixe) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetStructure));
		filter.and(new LikeFilter(id, prefixe+"*"));
		StructureAttributMapper strAttributMapper = new StructureAttributMapper();
		return ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(),SearchControls.ONELEVEL_SCOPE ,strAttributMapper);
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#findListeStrFiltreLdap(java.lang.String)
	 */
	public List<Structure> findListeStrFiltreLdap(String filtreLdap) throws ToutaticeAnnuaireException {
		if(filtreLdap==null){
			throw new ToutaticeAnnuaireException("paramètre filtreLdap non renseigné");
		}
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetStructure));
		
		String filtreGlobal;
		if (!filtreLdap.trim().isEmpty()){
			filtreGlobal = "(&" + filtreLdap.concat(filter.encode()) + ")";
		}
		else{
			filtreGlobal= filter.encode();
		}
		
		StructureAttributMapper strAttributMapper = new StructureAttributMapper();
		return ldapTemplateLecture.search("ou="+categorieLDAP, filtreGlobal,SearchControls.ONELEVEL_SCOPE ,strAttributMapper);
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#findStructurePersonneByProfil(fr.toutatice.outils.ldap.entity.Person)
	 */
	public List<Structure> findStructurePersonneByProfil(Person p){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetStructure));
		OrFilter orFilter = new OrFilter();
		for(String dnProfil : p.getListeProfils()){
			orFilter.or(new EqualsFilter(profils,dnProfil));
		}
		filter.and(orFilter);
		StructureAttributMapper strAttributMapper = new StructureAttributMapper();
		return ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(),SearchControls.ONELEVEL_SCOPE ,strAttributMapper);	
	}
	
		/* (non-Javadoc)
		 * @see fr.toutatice.outils.ldap.dao.StructureDao#findStructuresLieesProfil(java.lang.String)
		 */
		public List<Structure> findStructuresLieesProfil(String dnProfil){
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", classObjetStructure));
		filter.and(new EqualsFilter(profils, dnProfil));
		StructureAttributMapper strAttributMapper = new StructureAttributMapper();
		return ldapTemplateLecture.search("ou="+categorieLDAP, filter.encode(),SearchControls.ONELEVEL_SCOPE ,strAttributMapper);	
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#create(fr.toutatice.outils.ldap.entity.Structure)
	 */
	 public void create(Structure str) throws ToutaticeAnnuaireException {
		  Name dn = buildDn(str.getId());
		  try {
			  Attributes attr = buildAttributes(str);
			  ldapTemplateEcriture.bind(dn, null, attr);
		  } catch (Exception e) {
				logger.info("Impossible de créér la structure "+ str.getId());
				e.printStackTrace();
				throw new ToutaticeAnnuaireException("Erreur lors de la création de la structure "+str.getId()+" dans l'annuaire");
			} 
	   }
	 
	 
	 /* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#delete(fr.toutatice.outils.ldap.entity.Structure)
	 */
	   public void delete(Structure str) throws ToutaticeAnnuaireException {
		   try {
	      ldapTemplateEcriture.unbind(buildDn(str.getId()));
		   } 		
			catch (Exception e) {
				logger.info("La suppression de la structure "+str.getId()+ " a échoué");
				e.printStackTrace();
				throw new ToutaticeAnnuaireException("Erreur lors de la suppression de la structure "+str.getId());
			}
	   }
	
	   /* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#prepareContextForUpdate(org.springframework.ldap.core.DirContextOperations, fr.toutatice.outils.ldap.entity.Structure)
	 */
	public DirContextOperations prepareContextForUpdate(DirContextOperations context, Structure str){
			
			Structure strRef = this.findByPrimaryKey(str.getId());
			
			if (str.getNoRecepisseCNIL()!=null){
				if(!str.getNoRecepisseCNIL().equals(strRef.getNoRecepisseCNIL())) {
				context.setAttributeValue(noRecepisseCnil,	str.getNoRecepisseCNIL());
				}
			}
			
			return context;
		}
	   
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#update(fr.toutatice.outils.ldap.entity.Structure)
	 */
	@CacheEvict(value = "structureByPrimaryKeyCache", key = "#str.id")
	public void update(Structure str) throws ToutaticeAnnuaireException {

		Name dn = this.buildDn(str.getId());

		DirContextOperations context = ldapTemplateEcriture
				.lookupContext(dn);
		
		context = super.prepareContextForUpdate(context, str);
		context = this.prepareContextForUpdate(context, str);

		ldapTemplateEcriture.modifyAttributes(context);


	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.outils.ldap.dao.StructureDao#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext ctx)
	throws BeansException {
		context = ctx;

}
	
}
