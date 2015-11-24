/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *
 * Contributors:
 *  aguihomat
 * 
 *    
 */
package fr.toutatice.identite.portail.ficheProfil;




import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.portlet.PortalGenericPortlet;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.identite.portail.ficheProfil.PersonStatut.statut;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.entity.Profil.typePeuplement;
import fr.toutatice.outils.ldap.entity.Structure;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;




@Controller
@RequestMapping("VIEW")
@SessionAttributes("ficheProfil")

public class FicheProfilController extends PortalGenericPortlet implements PortletContextAware {

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.identite");
	protected static final Log logModifLdap = LogFactory.getLog("fr.toutatice.annuaire.modif");
	
	private PortletContext portletContext;

	@Autowired
	private FicheProfilConfig config;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Person personneInstance;
	
	@Qualifier("structure")
	@Autowired Structure structureInstance;
	
	@Autowired 
	private Profil profilInstance;
	
	@Autowired
	private Habilitation habilitation;

	public Person getUserConnecte(PortletRequest request) {
		DirectoryPerson person = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);
		Person userConnecte = personneInstance.findUtilisateur(person.getUid());	
		request.setAttribute("userConnecte", userConnecte);
		return userConnecte;
	}
	
	public Habilitation.level initLevelHabilitation(Person user, Profil profil, PortletRequest request){
		Habilitation.level level = habilitation.findRoleUser(user, profil);
		request.setAttribute("levelUserConnecte", level);
		return level;
	}
	
	public void getEtbUserConnecte(Person user, Profil profil, PortletRequest request){
		List<Structure> etbUserConnecte = structureInstance.findStructuresPersonneByProfil(user);
		
		List<Structure> etbProfil = profil.findOrganisationsLiees();
		for(Structure str:etbProfil){
			boolean existe=false;
			for(Structure str2:etbUserConnecte){
				if(str.getId().equals(str2.getId())){
					existe=true;
				}
			}
			if(!existe){
				etbUserConnecte.add(str);
			}
		}
					
		Structure academie = context.getBean("structure",Structure.class);
		academie.setId("");
		academie.setDescription("Toute l'académie");
		etbUserConnecte.add(academie);
		request.setAttribute("etbUserConnecte", etbUserConnecte);
	}
	
	
	private List<Profil> initMacroProfils(Person user, PortletRequest request){
		List<Profil> listeMacroProfils = new ArrayList<Profil>();
		String macroProfils = "";
		if(user.getSourceSI().equals("EM")){
			macroProfils = config.getMacroProfilMER();
		}else {
			if(user.getSourceSI().equals("AAF-AGRI")){
			macroProfils = config.getMacroProfilAGRI();
			}else{
			macroProfils = config.getMacroProfilEN();
			}
		}
		
		String[] tab =macroProfils.split(";");
		for(int i=0;i<tab.length;i++){
			Profil p = profilInstance.findProfilByCn(tab[i]);
			if(p!=null){
				listeMacroProfils.add(p);
			}
		}
		Profil pr = context.getBean("profil", Profil.class);
		pr.setCn("");
		pr.setDescription("Tous");
		listeMacroProfils.add(pr);
		request.setAttribute("macroProfils",listeMacroProfils);
		return listeMacroProfils;
	}


	@ModelAttribute("ficheProfil")
	public FicheProfil getFicheProfil(PortletRequest request) throws NamingException, ToutaticeAnnuaireException {

		FicheProfil ficheProfil = new FicheProfil();
	
		PortalWindow window = WindowFactory.getWindow(request);
		String cnProfil = window.getProperty("cnProfil");
		Profil p = profilInstance.findProfilByCn(cnProfil);
		ficheProfil.setProfil(p);
		
		if(p != null) {
		
			if(ficheProfil.getProfil().getListeMembersAttributes()!=null){
				if(ficheProfil.getProfil().getListeMembersAttributes().size() <= 5000){
					List<PersonStatut> liste = new ArrayList<PersonStatut>();
					for(Person p1 : personneInstance.findListePersonnesAvecProfil(ficheProfil.getProfil().getDn(),"cn")){
						liste.add(new PersonStatut(p1,statut.RAS));
					}
					ficheProfil.setListeMembresTries(liste);

				/*	for(String dn:ficheProfil.getProfil().getListeExplicitMembers()){
						Person explicitM = personneInstance.findPersonByDn(dn);
						List<PersonStatut> l1 = new ArrayList<PersonStatut>();
						for(PersonStatut ps : ficheProfil.getListeMembresTries()){
							if(!ps.getPerson().getUid().equalsIgnoreCase(explicitM.getUid()))
								l1.add(ps);
						}
						ficheProfil.setListeMembresTries(l1);
						
					}*/
					
					ficheProfil.setChgtMembres(true);
				//	ficheProfil.setNbMembresImplicitesProfil(ficheProfil.getListeMembresTries().size());
				}
			}
			else{
				ficheProfil.setListeMembresTries(new ArrayList<PersonStatut>());
				ficheProfil.setChgtMembres(true);
				//ficheProfil.setNbMembresImplicitesProfil(0);		
			}
			if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.MIXTE)){
				for(String s : ficheProfil.getProfil().getListeExplicitMembers()){
					Person p1 = personneInstance.findPersonByDn(s);
					if(p1!=null){
						int i=0;
						for(PersonStatut pe:ficheProfil.getListeMembresExplicitTries()){
							if(p1.getSn()!=null && pe.getPerson().getSn()!=null && pe.getPerson().getSn().compareToIgnoreCase(p1.getSn())<0){
								i++;
							}
						}
						ficheProfil.getListeMembresExplicitTries().add(i,new PersonStatut(p1,statut.RAS));
					}
				}
			}
			
			for(String dn:p.getListeExplicitManagers()){
				ficheProfil.getListeManagersTries().add(new ManagerStatut(dn, ManagerStatut.statut.RAS));
			}
			
			List<Structure> lstr = p.findOrganisationsLiees();

			for(Structure s:lstr){
				if(s.getId().equals(p.getCn().substring(0, 8))){
					ficheProfil.setFiltreRne(s.getId());
				}
			}
			
			List<Profil> groupesOrgaLiee = new ArrayList<Profil>();

			for(Structure s:lstr){
				for(String dn:s.getListeProfils()){
					Profil prf = profilInstance.findProfilByDn(dn);
					if(prf!=null){
						if(prf.getType()==null || (prf.getType()!=null&& !prf.getType().equalsIgnoreCase("space-group"))){
						groupesOrgaLiee.add(prf);
						}
					}
				}
			}
		
			ficheProfil.setGroupesOrgaLiee(groupesOrgaLiee);
			
			if(ficheProfil.getFiltreRne().isEmpty()){
				ficheProfil.setFiltreRne("*");
			}
		
			
			//ficheProfil.setNbMembresExplicitesProfil(ficheProfil.getProfil().getListeExplicitMembers().size());
		}	
		/*else {
			if(ficheProfil.getProfil().getListeMembersAttributes()!=null){
				ficheProfil.setNbMembresImplicitesProfil(ficheProfil.getProfil().getListeMembersAttributes().size());
			}else{
				ficheProfil.setNbMembresImplicitesProfil(0);
			}
			ficheProfil.setNbMembresExplicitesProfil(ficheProfil.getProfil().getListeExplicitMembers().size());
		}*/
		
		if (config.getEnableMacroProfils()) {
			Person userConnecte = this.getUserConnecte(request);
			List<Profil> listeMacroProfils = this.initMacroProfils(userConnecte,request);
			ficheProfil.setMacroProfil(listeMacroProfils.get(0).getCn());
		}
		
		return ficheProfil;
	}
	
	
		
	
	@RenderMapping 	
	public String showFicheProfil(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, RenderRequest request, RenderResponse response) throws Exception {
		
		Person userConnecte = this.getUserConnecte(request);
		Habilitation.level levelUserConnecte = this.initLevelHabilitation(userConnecte, ficheProfil.getProfil(), request);
		this.getEtbUserConnecte(userConnecte, ficheProfil.getProfil(), request);
		

		if(userConnecte==null){
			return "nonConnecte";
		}else{
			
			//Si on a changé de profil
			PortalWindow window = WindowFactory.getWindow(request);
			String cnProfil = window.getProperty("cnProfil");
			if (ficheProfil.getProfil()!=null && cnProfil!=null && !cnProfil.equals(ficheProfil.getProfil().getCn())) {	
				ficheProfil = this.getFicheProfil(request);
			}
			
			PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
			
			if(ficheProfil.getProfil().getListeMembersAttributes()!=null){
				if(ficheProfil.getProfil().getListeMembersAttributes().size() <= 5000){
					this.setListeMembresUrlModel(userConnecte,ficheProfil, ficheProfil.getListeMembresTries(), request, portalControllerContext);
				}
			}
			
			this.setListeMembresUrlModel(userConnecte, ficheProfil, ficheProfil.getListeMembresTries(), request, portalControllerContext);
			
			if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.MIXTE)){
				this.setListeMembresExplicitesUrlModel(userConnecte,ficheProfil, ficheProfil.getListeMembresExplicitTries(), request, portalControllerContext);
			}
			
			this.setListeManagersUrlModel(ficheProfil.getProfil(), userConnecte, request, portalControllerContext);
			this.setListeExplicitManagersUrlModel(ficheProfil.getProfil(), userConnecte, request, portalControllerContext);
	
			
			String retour;
			if (levelUserConnecte.equals(Habilitation.level.NONHABILITE)) {
				retour = "nonAutorise";
			}
			else {
				retour = "ficheProfil";
			}
		
			return retour;
		}
	}
	
	@RenderMapping(params = "action=gestionMembres") 
	public String showGestionMembres(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, RenderRequest request, RenderResponse response) throws Exception {
		
		Person userConnecte = this.getUserConnecte(request);
		Habilitation.level levelUserConnecte = this.initLevelHabilitation(userConnecte, ficheProfil.getProfil(), request);
		this.getEtbUserConnecte(userConnecte, ficheProfil.getProfil(), request);
		
		if(config.getEnableMacroProfils()) {
			this.initMacroProfils(userConnecte,request);
		}
	
		if (request.getParameter("newUserCollapse") != null) {
			request.setAttribute("newUserCollapse", "in");

		}

		PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
		if (ficheProfil.getProfil().getPeuplement().equals(typePeuplement.MIXTE)) {
			this.setListeMembresUrlModel(userConnecte, ficheProfil, ficheProfil.getListeMembresExplicitTries(), request, portalControllerContext);
		} else {
			this.setListeMembresUrlModel(userConnecte, ficheProfil, ficheProfil.getListeMembresTries(), request, portalControllerContext);
		}
		
		this.setListeMembresRechercheUrlModel(userConnecte, ficheProfil, request, portalControllerContext);
		
		
		String retour;
		if (levelUserConnecte.equals(Habilitation.level.NONHABILITE)) {
			retour = "nonAutorise";
		}
		else {
			if (levelUserConnecte.equals(Habilitation.level.ADMINISTRATEUR) || levelUserConnecte.equals(Habilitation.level.GESTIONNAIRE)){
				if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.MIXTE)){
					retour = "gestionMembres"; 
				}else{
					if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.EXPLICITE)){
						retour = "gestionMembres"; 
					}else{
						retour = "ficheProfil";
					}
				}
			}
			else {
				retour = "ficheProfil";
			}
		}
		
		return retour;
	
	}
	
	
	@RenderMapping(params = "action=gestionManagers")
	public String showGestionManagers(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, RenderRequest request, RenderResponse response) throws Exception {
		
		Person userConnecte = this.getUserConnecte(request);
		Habilitation.level levelUserConnecte = this.initLevelHabilitation(userConnecte, ficheProfil.getProfil(), request);
		this.getEtbUserConnecte(userConnecte, ficheProfil.getProfil(), request);
		if(config.getEnableMacroProfils()) {
			this.initMacroProfils(userConnecte,request);
		}
		
		if (request.getParameter("newUserCollapse") != null) {
			request.setAttribute("newUserCollapse", "in");

		}

		PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
		//this.setListeManagersUrlModel(ficheProfil.getProfil(),userConnecte, request, portalControllerContext);
		//this.setListeExplicitManagersUrlModel(ficheProfil.getProfil(),userConnecte, request, portalControllerContext);
		this.setListeManagersGestionUrlModel(ficheProfil.getListeManagersTries(), userConnecte, request, portalControllerContext);
		this.setListeManagersAjoutUrlModel(userConnecte,ficheProfil.getListeManagersAjout(), request, portalControllerContext);

		
		String retour;
		if (levelUserConnecte.equals(Habilitation.level.NONHABILITE)) {
			retour = "nonAutorise";
		}
		else {
			if (levelUserConnecte.equals(Habilitation.level.ADMINISTRATEUR) || levelUserConnecte.equals(Habilitation.level.GESTIONNAIRE)){
				retour = "gestionManagers"; }
			else {
				retour = "ficheProfil";
			}
		}

		return retour;
		
	}
	
	
	



	
	@ActionMapping(params = "action=rechercherMembre") 	
	public void rechercherMembre(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, ActionRequest request, ActionResponse response)
			throws ToutaticeAnnuaireException {

		PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);


		if(ficheProfil.getFiltreMembre().length()<3 && (ficheProfil.getFiltreRne().equals("*")||ficheProfil.getMacroProfil().equals(""))){
			addNotification(pcc, "label.3charmin", NotificationsType.ERROR);
			ficheProfil.setListeMembresRecherche(new ArrayList<Person>());
		}
		else {
			String filtre = ficheProfil.getProfil().getFiltreRecherche();
			
			// TODO peux-t'on enlever ceci ?
//			if(filtre==null){
//				filtre="(objectClass=ENTPerson)";
//			}
			List<String> liste = new ArrayList<String>();
			if(!ficheProfil.getMacroProfil().equals("")){
				liste.add(profilInstance.findProfilByCn(ficheProfil.getMacroProfil()).getDn());
			}
			ficheProfil.setListeMembresRecherche(personneInstance.findPersonneMultiCriteres(ficheProfil.getFiltreMembre(), ficheProfil.getFiltreRne(), liste,filtre, "sn"));
			if(ficheProfil.getListeMembresRecherche().size()<1){
				addNotification(pcc, "label.noresultMembre", NotificationsType.INFO);

			}
		}	
		response.setRenderParameter("newUserCollapse", "in");
		response.setRenderParameter("action", "gestionMembres");
	
	}

	@ActionMapping(params="action=deleteMember") 
	public void deleteMember(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, @RequestParam String uid, ActionResponse response) throws ToutaticeAnnuaireException, NamingException {
		
		
		if(ficheProfil==null||ficheProfil.getProfil().getPeuplement().equals(typePeuplement.IMPLICITE)) {
			response.setRenderParameter("action", "ficheProfil");
		} else {
			String dn = personneInstance.findFullDn(uid);
			if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.MIXTE)){
				ficheProfil.getProfil().removeExplicitMember(dn);
				List<PersonStatut> liste = new ArrayList<PersonStatut>();
				for(PersonStatut p:ficheProfil.getListeMembresExplicitTries()){
					if(!uid.equalsIgnoreCase(p.getPerson().getUid())){
						liste.add(p);
					}else{
						liste.add(new PersonStatut(p.getPerson(), statut.SUPPRIMER));
					}
				}
				ficheProfil.setListeMembresExplicitTries(liste);
				//ficheProfil.setNbMembresExplicitesProfil(ficheProfil.getProfil().getListeExplicitMembers().size());
			}
			if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.EXPLICITE)){
				ficheProfil.getProfil().removeMember(dn);
				List<PersonStatut> liste = new ArrayList<PersonStatut>();
				for(PersonStatut p:ficheProfil.getListeMembresTries()){
					if(!uid.equalsIgnoreCase(p.getPerson().getUid())){
						liste.add(p);
					}else{
						liste.add(new PersonStatut(p.getPerson(), statut.SUPPRIMER));
					}
				}
				ficheProfil.setListeMembresTries(liste);
				//ficheProfil.setNbMembresImplicitesProfil(ficheProfil.getProfil().getListeMembers().size());
			}
			ficheProfil.setModified(true);
			response.setRenderParameter("action", "gestionMembres");
		}
	}
	
	@ActionMapping(params="action=ajoutMember") 
	public void ajoutMember(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, @RequestParam String uid, ActionRequest request, ActionResponse response) throws ToutaticeAnnuaireException, NamingException {
		if(ficheProfil==null||ficheProfil.getProfil().getPeuplement().equals(typePeuplement.IMPLICITE)) {
			response.setRenderParameter("action", "ficheProfil");
		} else {
			
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			
			String dn = personneInstance.findFullDn(uid);
			
			if(ficheProfil.getProfil().isMember(dn)) {
					addNotification(pcc, "label.personneDejaAssocie", NotificationsType.ERROR);
			}
			else {
				if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.MIXTE)){
						ficheProfil.getProfil().addExplicitMember(dn);
					//	ficheProfil.setNbMembresExplicitesProfil(ficheProfil.getProfil().getListeExplicitMembers().size());
						ficheProfil.setModified(true);
						Person pAjout = personneInstance.findUtilisateur(uid);
						int i=0;
						for(PersonStatut p : ficheProfil.getListeMembresExplicitTries()){
							if(pAjout.getSn()!=null && p.getPerson().getSn()!=null && p.getPerson().getSn().compareToIgnoreCase(pAjout.getSn())<0){
									i++;
							}
									
						}
						ficheProfil.getListeMembresExplicitTries().add(i,new PersonStatut(pAjout, statut.AJOUTER));
				}
				if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.EXPLICITE)){
					ficheProfil.getProfil().addMember(dn);
					//ficheProfil.setNbMembresImplicitesProfil(ficheProfil.getProfil().getListeMembers().size());
					ficheProfil.setModified(true);
					Person pAjout = personneInstance.findUtilisateur(uid);
					int i=0;
					for(PersonStatut p : ficheProfil.getListeMembresTries()){
						if(pAjout.getSn()!=null && p.getPerson().getSn()!=null && p.getPerson().getSn().compareToIgnoreCase(pAjout.getSn())<0){
								i++;
						}
								
					}
					ficheProfil.getListeMembresTries().add(i,new PersonStatut(pAjout,statut.AJOUTER));
				}
				
					//suppression de la personne ajoutée des résultats de recherche
					List<Person> liste = new ArrayList<Person>();
					for(Person p : ficheProfil.getListeMembresRecherche()) {
						if(! p.getDn().toLowerCase().equals(dn.toLowerCase())) {
							liste.add(p);
						}
					}
					ficheProfil.setListeMembresRecherche(liste);
			
				}

				response.setRenderParameter("action", "gestionMembres");
			response.setRenderParameter("newUserCollapse", "in");
		}
	}
	
	@ActionMapping(params="action=ajouterGroupe") 
	public void ajouterGroupe(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, ActionResponse response)
			throws ToutaticeAnnuaireException, NamingException {
		if(ficheProfil==null||ficheProfil.getProfil().getPeuplement().equals(typePeuplement.IMPLICITE)) {
			response.setRenderParameter("action", "ficheProfil");
		} else {
			String cn = ficheProfil.getFiltreAjoutGroupe();

			Profil prf = profilInstance.findProfilByCn(cn);
			ficheProfil.setModified(true);
			
			for(String dn:prf.getListeMembers()){
				Person p = personneInstance.findPersonByDn(dn);
				if(p!=null){
					if(!ficheProfil.getProfil().isMember(dn)){
						if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.MIXTE)){
							ficheProfil.getProfil().addExplicitMember(dn);
						//	ficheProfil.setNbMembresExplicitesProfil(ficheProfil.getProfil().getListeExplicitMembers().size());
							int i=0;
							for(PersonStatut p1 : ficheProfil.getListeMembresExplicitTries()){
								if(p.getSn()!=null && p1.getPerson().getSn()!=null && p1.getPerson().getSn().compareToIgnoreCase(p.getSn())<0){
										i++;
								}	
							}
							ficheProfil.getListeMembresExplicitTries().add(i,new PersonStatut(p,statut.AJOUTER));
						}
						if(ficheProfil.getProfil().getPeuplement().equals(typePeuplement.EXPLICITE)){
							ficheProfil.getProfil().addMember(dn);
						//	ficheProfil.setNbMembresImplicitesProfil(ficheProfil.getProfil().getListeMembers().size());
							int i=0;
							for(PersonStatut p1 : ficheProfil.getListeMembresTries()){
								if(p.getSn()!=null && p1.getPerson().getSn()!=null && p1.getPerson().getSn().compareToIgnoreCase(p.getSn())<0){
										i++;
								}
										
							}
							ficheProfil.getListeMembresTries().add(i,new PersonStatut(p,statut.AJOUTER));
						}
								
					}
				}
			}
			response.setRenderParameter("action", "gestionMembres");
			response.setRenderParameter("newUserCollapse", "in");
		}
			
	}
		
	
	
	
	@ActionMapping(params="action=ajoutManager") 
	public void ajoutManager(@RequestParam String dnManager, @ModelAttribute("ficheProfil") FicheProfil ficheProfil, ActionRequest request, ActionResponse response)
			throws Exception {
		
		PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);

		if (ficheProfil.getProfil().getListeManagers().contains(personneInstance.findFullDn(dnManager))
				|| ficheProfil.getProfil().getListeManagers().contains(dnManager)
				|| ficheProfil.getProfil().getListeExplicitManagers().contains(personneInstance.findFullDn(dnManager))
				|| ficheProfil.getProfil().getListeExplicitManagers().contains(dnManager)) {		
			addNotification(pcc, "label.dejaManager", NotificationsType.WARNING);
		}
		else {
			ficheProfil.getProfil().addExplicitManager(dnManager);
			ficheProfil.getListeManagersAjout().remove(dnManager);
			ficheProfil.setModified(true);
			
			boolean existe = false;
			for(ManagerStatut manager:ficheProfil.getListeManagersTries()){
				if (manager.getDn().equalsIgnoreCase(dnManager)){
					existe = true;
					manager.setStatut(ManagerStatut.statut.AJOUTER);
				}
			}
			if(!existe){
				ficheProfil.getListeManagersTries().add(new ManagerStatut(dnManager, ManagerStatut.statut.AJOUTER));
			}
		}
		
		response.setRenderParameter("action", "gestionManagers");		
		response.setRenderParameter("newUserCollapse", "in");
	
	}
	
	@ActionMapping(params="action=deleteManager") 
	public void deleteManager(@RequestParam String dnManager, @ModelAttribute("ficheProfil") FicheProfil ficheProfil, ActionResponse response) {


		List<String> liste = (ArrayList<String>)((ArrayList<String>)ficheProfil.getProfil().getListeExplicitManagers()).clone();
		for(String dn : liste) {
			if (dn.equalsIgnoreCase(dnManager)) {
				ficheProfil.getProfil().removeExplicitManager(dn);
			}
		}
		for(ManagerStatut manager:ficheProfil.getListeManagersTries()){
			if (manager.getDn().equalsIgnoreCase(dnManager)){
				manager.setStatut(ManagerStatut.statut.SUPPRIMER);
			}
		}
		
		ficheProfil.setModified(true);
		response.setRenderParameter("action", "gestionManagers");			
		
	}
	
	@ActionMapping(params = "action=rechercherManager") 	
	public void rechercherManager(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, ActionRequest request, ActionResponse response) {

		PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);

		if(ficheProfil.getTypeManager().equals("")) {
			
			addNotification(pcc, "label.selectType", NotificationsType.ERROR);
			
			ficheProfil.setListeManagersAjout(new ArrayList<String>());
		} else {
			List<String> liste = new ArrayList<String>();
			if(ficheProfil.getTypeManager().equals("person")){
	
				if(ficheProfil.getFiltreRne().equals("")&ficheProfil.getFiltreManager().equals("")){
					addNotification(pcc, "label.selectCritere", NotificationsType.WARNING);

					ficheProfil.setListeManagersAjout(new ArrayList<String>());
				}
				else {
					if(ficheProfil.getFiltreManager().length()<3 && (ficheProfil.getFiltreRne().equals("*")||ficheProfil.getMacroProfil().equals(""))){

						addNotification(pcc, "label.3charmin", NotificationsType.WARNING);

						ficheProfil.setListeMembresRecherche(new ArrayList<Person>());
					}
					else{					
						String filtre = ficheProfil.getProfil().getFiltreRecherche();
						if(filtre==null){
							filtre="(!(objectClass=ENTEleve))";
						}
						List<String> l = new ArrayList<String>();
						if(!ficheProfil.getMacroProfil().equals("")){
							l.add(profilInstance.findProfilByCn(ficheProfil.getMacroProfil()).getDn());
						}
						String rne = ficheProfil.getFiltreRne();
						if(rne.equals("*")){
							rne ="";
						}
						List<Person> listePerson = personneInstance.findPersonneMultiCriteres(ficheProfil.getFiltreManager(), rne, l,filtre, "sn") ;
						for(Person p : listePerson) {
							liste.add(p.findFullDn(p.getUid()));
						}

						
					}
				}
			}
			if(ficheProfil.getTypeManager().equals("profil")){
				if(ficheProfil.getFiltreManager().length()<3 && (ficheProfil.getFiltreRne().equals("*"))){

					addNotification(pcc, "label.3charmin", NotificationsType.WARNING);
					ficheProfil.setListeMembresRecherche(new ArrayList<Person>());
				}
				else{
					String rne = ficheProfil.getFiltreRne();
					if(rne.equals("*")){
						rne ="";
					}
					List<Profil> listeProfil = profilInstance.findProfilByRneNom(rne, ficheProfil.getFiltreManager());
					for(Profil p : listeProfil) {
						liste.add(p.getDn());
					}
				}
			}
			ficheProfil.setListeManagersAjout(liste);
			if(ficheProfil.getListeManagersAjout().size()<1){

				addNotification(pcc, "label.noresult", NotificationsType.INFO);
			}
		}
		
		response.setRenderParameter("action", "gestionManagers");	
		response.setRenderParameter("newUserCollapse", "in");
	}
	
	@ActionMapping(params="action=valider") 
	public void valider(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, ActionResponse response,  ActionRequest request, ModelMap model) throws ToutaticeAnnuaireException, NamingException {

		Person userConnecte = this.getUserConnecte(request);
		// Enregistrement dans l'annuaire
		ficheProfil.getProfil().updateProfil();
		ficheProfil = this.getFicheProfil(request);
		model.addAttribute("ficheProfil", ficheProfil);
		
		response.setRenderParameter("action", "ficheProfil");
		PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
		addNotification(pcc, "label.modifOk", NotificationsType.SUCCESS);
		logModifLdap.info("L'utilisateur "+userConnecte.getUid()+" a modifié le profil "+ficheProfil.getProfil().getDn());
		
		//request.setAttribute("osivia.updateContents", "true");
		//response.setRenderParameter("cnProfil",ficheProfil.getProfil().getCn());	
		

	}
	
	@ActionMapping(params="action=annuler") 
	public void annuler(@ModelAttribute("ficheProfil") FicheProfil ficheProfil, ActionResponse response,  ActionRequest request, ModelMap model) throws ToutaticeAnnuaireException, NamingException {
		ficheProfil = this.getFicheProfil(request);
		model.addAttribute("ficheProfil", ficheProfil);
		response.setRenderParameter("action", "ficheProfil");	
		
	}
	
	

	
	private void setListeMembresUrlModel(Person userConnecte,FicheProfil fiche, List<PersonStatut> listeMembres, RenderRequest request, PortalControllerContext portalControllerContext) throws Exception {
		List<PersonUrl> liste = new ArrayList<PersonUrl>();
		
		for(PersonStatut p: listeMembres) {
			
			if(p!=null&&p.getPerson()!=null) {
				PersonUrl p1 = this.buildUrlPerson(p.getPerson(), p.getStatut(), userConnecte, fiche, request, portalControllerContext);
				if(p1!=null);
				liste.add(p1);
			}
		}
		//Collections.sort(liste);
		request.setAttribute("listeMembresUrl",liste);
	}
	
	private void setListeMembresExplicitesUrlModel(Person userConnecte,FicheProfil fiche, List<PersonStatut> listeMembres, RenderRequest request, PortalControllerContext portalControllerContext) throws Exception {
		List<PersonUrl> liste = new ArrayList<PersonUrl>();
	
		for(PersonStatut p : listeMembres) {
			
			if(p!=null&&p.getPerson()!=null) {
				PersonUrl p1 = this.buildUrlPerson(p.getPerson(), p.getStatut(), userConnecte, fiche, request, portalControllerContext);
				if(p1!=null);
				liste.add(p1);
			}
		}
		
		request.setAttribute("listeMembresExplicitesUrl",liste);
	}
	
	@SuppressWarnings("unchecked")
	private void setListeMembresRechercheUrlModel(Person userConnecte,FicheProfil fiche, RenderRequest request, PortalControllerContext portalControllerContext) throws Exception {
		List<PersonUrl> liste = new ArrayList<PersonUrl>();

		for(Person p : fiche.getListeMembresRecherche()) {
			if(p!=null) {
				PersonUrl p1 = this.buildUrlPerson(p, statut.RAS, userConnecte, fiche, request, portalControllerContext);
				if(p1!=null){
					liste.add(p1);
				}
			}
		}
		Collections.sort(liste);
		request.setAttribute("listeMembresRechercheUrl",liste);
	}
	
	private PersonUrl buildUrlPerson(Person p, statut statut, Person userConnecte,FicheProfil fiche,RenderRequest request, PortalControllerContext portalControllerContext) throws Exception{

		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		windowProperties.put("osivia.hideTitle", "1");
		windowProperties.put("uidFichePersonne", p.getUid());

		String url = getPortalUrlFactory().getStartPortletUrl(portalControllerContext, "toutatice-identite-fichepersonne-portailPortletInstance",
				windowProperties, false);


		boolean clicable = true;
		return new PersonUrl(p,url,p.isMemberExplicit(fiche.getProfil().getCn()),clicable,false,statut);	
	}
	
	
	@SuppressWarnings("unchecked")
	private void setListeManagersUrlModel(Profil prof, Person userConnecte, RenderRequest request, PortalControllerContext portalControllerContext) throws Exception {
		List<ManagerUrl> liste = new ArrayList<ManagerUrl>();
	
		for(String dnManager : prof. getListeManagers()) {
			ManagerUrl m1 = this.createUrlManager(dnManager,ManagerStatut.statut.RAS, request, userConnecte, portalControllerContext);
			if(m1!=null){
				liste.add(m1);
			}
		}
		Collections.sort(liste);
		request.setAttribute("listeManagersUrl",liste);
	}
	
	@SuppressWarnings("unchecked")
	private void setListeExplicitManagersUrlModel(Profil prof, Person userConnecte, RenderRequest request, PortalControllerContext portalControllerContext) throws Exception {
		List<ManagerUrl> liste = new ArrayList<ManagerUrl>();
	
		for(String dnManager : prof. getListeExplicitManagers()) {
			ManagerUrl m1 = this.createUrlManager(dnManager, ManagerStatut.statut.RAS, request, userConnecte, portalControllerContext);
			if(m1!=null){
				liste.add(m1);
			}
		}
		Collections.sort(liste);
		request.setAttribute("listeExplicitManagersUrl",liste);
	}
	
	@SuppressWarnings("unchecked")
	private void setListeManagersGestionUrlModel(List<ManagerStatut> listeManagers, Person userConnecte, RenderRequest request, PortalControllerContext portalControllerContext) throws Exception {
		List<ManagerUrl> liste = new ArrayList<ManagerUrl>();
	
		for(ManagerStatut manager : listeManagers) {
			ManagerUrl m1 = this.createUrlManager(manager.getDn(), manager.getStatut(), request, userConnecte, portalControllerContext);
			if(m1!=null){
				liste.add(m1);
			}
		}
		Collections.sort(liste);
		request.setAttribute("listeManagersGestionUrl",liste);
	}
	
	@SuppressWarnings("unchecked")
	private void setListeManagersAjoutUrlModel(Person userConnecte,List<String> listeDnManagers, RenderRequest request, PortalControllerContext portalControllerContext) throws Exception {
		List<ManagerUrl> liste = new ArrayList<ManagerUrl>();

		for(String dnManager : listeDnManagers) {
			ManagerUrl m1 = this.createUrlManager(dnManager, ManagerStatut.statut.RAS, request, userConnecte, portalControllerContext);
			if(m1!=null){
			liste.add(m1);
			}
		}
	
		Collections.sort(liste);
		request.setAttribute("listeManagersUrlAjout",liste);
	}
	
	public ManagerUrl createUrlManager(String dnManager, fr.toutatice.identite.portail.ficheProfil.ManagerStatut.statut statut, RenderRequest request, Person userConnecte, PortalControllerContext portalControllerContext) throws Exception{
		ManagerUrl managerUrl=null;
		if(dnManager.length()>=4){
			if(dnManager.substring(0, 4).equals("uid=")) {
				Person p; 
				p = personneInstance.findPersonByDn(dnManager);
				if(p!=null) {

					Map<String, String> windowProperties = new HashMap<String, String>();
					windowProperties.put("osivia.ajaxLink", "1");
					windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
					windowProperties.put("osivia.title", p.getDisplayName());
					windowProperties.put("osivia.hideTitle", "1");
					windowProperties.put("uidFichePersonne", p.getUid());
				
					String url = getPortalUrlFactory().getStartPortletUrl(portalControllerContext, "toutatice-identite-fichepersonne-portailPortletInstance",
							windowProperties, false);
								
					boolean sameDn = userConnecte.getDn().toLowerCase().equals(p.getDn().toLowerCase());
				
					boolean userIsAdmin = habilitation.isAdmin(userConnecte);

					boolean clicable = true;
					boolean droitDelete;
					if(userIsAdmin){
						droitDelete= true;
					}else {
						droitDelete = ! sameDn;
					}
					managerUrl = new ManagerUrl(p.getCn(), p.getUid(), statut, p.getDn(), url, "user", droitDelete, true, false);
				}
			}
			if(dnManager.substring(0, 3).equals("cn=")) {
				Profil p;
				p = profilInstance.findProfilByDn(dnManager);
				if(p!=null) {

					Map<String, String> windowProperties = new HashMap<String, String>();
					windowProperties.put("osivia.ajaxLink", "1");
					windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
					windowProperties.put("osivia.title", "Groupe : "+p.getDisplayName());
					windowProperties.put("osivia.hideTitle", "1");
					windowProperties.put("cnProfil", p.getCn());

					//String url = portalUrlFactory.getExecutePortletLink(request,"toutatice-identite-ficheprofil-portailPortletInstance",windowProperties, params);	
					String url = getPortalUrlFactory().getStartPortletUrl(portalControllerContext, "toutatice-identite-ficheprofil-portailPortletInstance",
							windowProperties, false);
					boolean hasProfil = userConnecte.hasProfil(dnManager);
					boolean isManagedByUser = p.isManagedBy(userConnecte);
					boolean userIsAdmin = habilitation.isAdmin(userConnecte);
					boolean clicable = hasProfil||isManagedByUser||userIsAdmin;
					boolean modifiable = isManagedByUser||userIsAdmin;
					managerUrl = new ManagerUrl(p.getDescription(),p.getCn(), statut, p.getDn(), url, "group", true, clicable, modifiable);
				}
			}
		}
		return managerUrl;
	}
	
	
	@RequestMapping(params = "action=nonAutorise")
	public String showNonAutorise(RenderRequest request, RenderResponse response) {
		
		return "nonAutorise";
	}


	public void setPortletContext(PortletContext ctx) {
		portletContext = ctx;			
	}

}
