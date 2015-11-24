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

package fr.toutatice.workspace.portail.gestionWorkspace;


import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.NamingException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.path.PortletPathItem;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.workspace.portail.gestionWorkspace.bean.FormGestion;
import fr.toutatice.workspace.portail.gestionWorkspace.bean.FormModifWks;
import fr.toutatice.workspace.portail.gestionWorkspace.bean.GestionPerson;
import fr.toutatice.workspace.portail.gestionWorkspace.bean.GestionProfil;
import fr.toutatice.workspace.portail.gestionWorkspace.bean.GestionProfil.statut;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.CreateWorkspaceCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.FetchWorkspaceCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.GetGroupListCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.IsAnimateurCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.IsWriteEnableCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.QueryDocumentCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.RefreshPrincipalCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.UpdateRolesWksCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.nuxeo.UpdateWorkspaceCommand;


@SuppressWarnings("restriction")
@Controller
@RequestMapping("VIEW")
@SessionAttributes("workspace")

public class GestionWorkspaceController extends CMSPortlet implements PortletContextAware, PortletConfigAware{

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.nuxeo");
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Person personneInstance;
	
	@Autowired
	private Profil profilInstance;
	
	@Autowired
	@Qualifier("organisation")
	private Organisation organisationInstance;
	
	@Autowired
	private GestionWorkspacesConfig config;
	
	private PortletContext portletContext;
	private PortletConfig portletConfig;

	
	private String cnRoleCreationOpenWks;
	private String pathOpenWorkspaceDirectory;
	

	@ModelAttribute("formGestion")
	public FormGestion gestFormGestion(){
		return new FormGestion();
	}
	
	
	@Autowired
	@Qualifier("modifyValidator")
	private Validator modifyValidator;
	
			
	public String getCnRoleCreationOpenWks() {
		return cnRoleCreationOpenWks;	}
	public void setCnRoleCreationOpenWks(String cnRoleCreationOpenWks) {
		this.cnRoleCreationOpenWks = cnRoleCreationOpenWks;	}

	public String getPathOpenWorkspaceDirectory() {
		return pathOpenWorkspaceDirectory;	}
	public void setPathOpenWorkspaceDirectory(String pathOpenWorkspaceDirectory) {
		this.pathOpenWorkspaceDirectory = pathOpenWorkspaceDirectory;	}

	@PostConstruct
	public void initNuxeoService() throws Exception {
		super.init();
		if (portletContext != null
				&& portletContext.getAttribute("nuxeoService") == null) {
			logger.info(" Start  nuxeo service ...");
			this.init(portletConfig);
			logger.info("Nuxeo service  started! ");
		}
	}

	@PreDestroy
	public void cleanUpNuxeoService() throws Exception {
		if (portletContext != null
				&& portletContext.getAttribute("nuxeoService") == null) {
			logger.info(" Stop  nuxeo service ...");
			this.destroy();
			logger.info("Nuxeo service  stopped! ");
		}
	}
	
	public Person getUserConnecte(PortletRequest request) {
		DirectoryPerson person = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);
		Person userConnecte = personneInstance.findUtilisateur(person.getUid());
		return userConnecte;
	}
	
	public List<Profil> setGroupesOrgaUser(Person user, RenderRequest request){
			
		List<Profil> groupesOrgaUser = new ArrayList<Profil>();
        Organisation orgRef = null;
        Document doc = (Document) request.getAttribute(Constants.PORTLET_ATTR_SPACE_CONFIG);

        if(doc != null) {
	        String rne = (String) doc.getProperties().get("dc:publisher");
	        if(rne != null) {
	        	orgRef = organisationInstance.findOrganisation(rne);
	        }
        }
			
        
        if(orgRef!=null){
        	for(String dn:orgRef.getListeProfils()){
				Profil prf = profilInstance.findProfilByDn(dn);
				if(prf!=null ){
					if(prf.getType()==null ||( prf.getType()!=null && !prf.getType().equalsIgnoreCase("space-group"))){
						groupesOrgaUser.add(prf);
					}
				}
			}
        }else{
			for(Organisation org :organisationInstance.findOrganisationPersonneByProfil(user) ){
				for(String dn:org.getListeProfils()){
					Profil prf = profilInstance.findProfilByDn(dn);
					if(prf!=null ){
						if(prf.getType()==null ||( prf.getType()!=null && !prf.getType().equalsIgnoreCase("space-group"))){
							groupesOrgaUser.add(prf);
						}
					}
				}
			}
        }
		request.setAttribute("groupesOrgaUser", groupesOrgaUser);
		return groupesOrgaUser;
	}
	
	
	public Workspace getWorkspace(PortletRequest request, NuxeoController nuxeoCtl, String path, Person userConnecte) throws Exception{
		
		Workspace wks = (Workspace) nuxeoCtl.executeNuxeoCommand(new FetchWorkspaceCommand(path,context));
		
		if(wks!=null){
			
			if(wks.getSourceOrganisationnelle()==null||wks.getSourceOrganisationnelle().trim().isEmpty()){
				Document doc = (Document) request.getAttribute(Constants.PORTLET_ATTR_SPACE_CONFIG);
				if(doc != null) {
					String rne = (String) doc.getProperties().get("dc:publisher");
	            	wks.setSourceOrganisationnelle(rne);
				}
			}
			
			boolean modifiableByUser = (Boolean) nuxeoCtl.executeNuxeoCommand(new IsWriteEnableCommand(wks.getPath(),userConnecte,profilInstance));
			wks.setModifiableByUser(modifiableByUser);

			this.setGroupesWorkspace(request,nuxeoCtl,wks);
			
			wks.setPermaLink(nuxeoCtl.createPermalink(wks.getPath()));
			wks.setUrl(wks.getPermaLink());
		}
		return wks;
		
	}
	
	@RenderMapping	
	public String showGestionWorkspace(RenderRequest request,RenderResponse response, PortletSession session, ModelMap model) throws Exception {
		
		PortalWindow window = WindowFactory.getWindow(request);
		String action = window.getProperty("action");

		logger.warn(action);
		
		if(action != null && action.equals("consulterRole")) {
			String path = window.getProperty("workspacePath");
			return consulterRole(path, request, response, session);
		}
		else {
			return showListeWorkspace(request, response, session, model);
		}
		
	}
	
	@RenderMapping		(params = "action=showListeWorkspace") 	
	public String showListeWorkspace(RenderRequest request,RenderResponse response, PortletSession session, ModelMap model) throws Exception {
	
		session.removeAttribute("workspace");
		model.remove("workspace");
		
		// breadcrumb
		adaptBreadcrumb("showListeWorkspace", new HashMap<String, String>(), request);
		
		
		Person userConnecte = this.getUserConnecte(request);
		if(userConnecte==null){
			request.setAttribute("osivia.emptyResponse", "1");
			return "nonconnecte";
		}else{
			NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);
			nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
			
			String nuxeoPath = this.getNuxeoPath(request);
			
			
			
			String requete = "ecm:isProxy=0 and ecm:primaryType='Workspace' and ecm:currentLifeCycleState <> 'deleted' and ecm:path startswith '"+nuxeoPath+"'";
			
			Documents results=null;
	
			try{
				results = (Documents) nuxeoCtl.executeNuxeoCommand(new QueryDocumentCommand(requete));		
			}catch(Exception e){
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.erreurGetListeWks", NotificationsType.ERROR);
				
			}
			
			List<Workspace> mesWorkspaces = new ArrayList<Workspace>();
			for(Document doc:results){
				boolean isAnimateur = (Boolean) nuxeoCtl.executeNuxeoCommand(new IsAnimateurCommand(doc.getPath(),userConnecte,profilInstance));
				if(isAnimateur){
					
					Workspace w = context.getBean("workspace",Workspace.class);
					w.setNom(doc.getString("dc:title"));
					w.setDescription(doc.getString("dc:description"));
					w.setShortname(doc.getString("workspace_acaren:shortName"));
					w.setPath(doc.getPath());
					String url = nuxeoCtl.getCMSLinkByPath(doc.getPath(), null).getUrl();
					w.setUrl(url);
					boolean modifiableByUser = (Boolean) nuxeoCtl.executeNuxeoCommand(new IsWriteEnableCommand(w.getPath(),userConnecte,profilInstance));
					w.setModifiableByUser(modifiableByUser);
					mesWorkspaces.add(w);
				}
			}
			request.setAttribute("mesWorkspaces", mesWorkspaces);
			
			
			boolean createEnable = (Boolean) nuxeoCtl.executeNuxeoCommand(new IsWriteEnableCommand(nuxeoPath,userConnecte,profilInstance));
			request.setAttribute("createEnable", createEnable);
			
			return "listeWorkspace";
		}
		
	}


	@RenderMapping	(params = "action=consulterEspace") 	
	public String consulterEspace( @RequestParam String path, RenderRequest request,RenderResponse response, PortletSession session, ModelMap model) throws Exception {
	
		Person userConnecte = this.getUserConnecte(request);

		NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
		nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
			
		Workspace workspace = this.getWorkspace(request, nuxeoCtl, path, userConnecte);
		
		
		if(workspace==null){
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
			return "listeWorkspace";	
		}
		
		session.setAttribute("workspace", workspace);
		model.addAttribute("workspace", workspace);
		return "consulterEspace";	

		
	}
	
	@RenderMapping(params = "action=consulterRole") 	
	public String consulterRole(@RequestParam String path, RenderRequest request,RenderResponse response, PortletSession session) throws Exception {
		
		
		
		NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
		nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
		
		Person userConnecte = this.getUserConnecte(request);
		Workspace workspace = this.getWorkspace(request, nuxeoCtl, path, userConnecte);
		
		if(workspace==null){
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
			return "listeWorkspace";
		}
		
		request.setAttribute("provenance","consulterRole");	
		request.setAttribute("workspace", workspace);
		session.setAttribute("workspace", workspace);
		
		
		// breadcrum
		Map<String, String> renderParams = new HashMap<String, String>();
    	renderParams.put("path", workspace.getPath());
    	renderParams.put("nom", workspace.getNom());
		adaptBreadcrumb("consulterRole", renderParams, request);
		
	
		if(workspace.getGroupeAnimateurs()==null||workspace.getGroupeContributeurs()==null||workspace.getGroupeLecteurs()==null){
			return "erreurGroupe";
		}else{
			return "consulterRole";	
		}
		
	}
	
	
	@RenderMapping(params = "action=modifier")
	public String showModifierEspace(@RequestParam String path, @RequestParam String provenance, RenderRequest request, RenderResponse response,PortletSession session) throws Exception {
		Person userConnecte = this.getUserConnecte(request);
	
		NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
		nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
		
		
		Workspace workspace = this.getWorkspace(request, nuxeoCtl, path, userConnecte);
		request.setAttribute("workspace", workspace);
		
	
		if(workspace==null){
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
			return "listeWorkspace";
		}
		
		FormModifWks form = new FormModifWks();
		form.setTheme(workspace.getTheme());
		request.setAttribute("formModifWks",form);
		
		request.setAttribute("provenance",provenance);	
		session.setAttribute("workspace", workspace);

		return "modifWorkspace";
	}
	
	@RenderMapping(params = "action=modifierRole")
	public String showModifierRole(@ModelAttribute Workspace workspace, @RequestParam String nomRole, @RequestParam String provenance, RenderRequest request,  RenderResponse response, PortletSession session, ModelMap model) throws Exception {
		Person userConnecte = this.getUserConnecte(request);
		this.setGroupesOrgaUser(userConnecte, request);
		
		if(workspace==null){
			return this.showListeWorkspace(request, response, session, model);
		}else{
		
			if(workspace.listeGestionProfils==null||workspace.getListeGestionPersonnes()==null){
				List<GestionProfil> listeGestionProfils = new ArrayList<GestionProfil>();
				List<GestionPerson> listeGestionPersonnes = new ArrayList<GestionPerson>();
				
				if(nomRole.equals("animateur")){
					for(String dn : workspace.getGroupeAnimateurs().getListeMembers()){
						listeGestionPersonnes.add(new GestionPerson(personneInstance.findPersonByDn(dn),statut.RAS));
					}
					for(Profil prf:workspace.getListeProfilAnimateurs()){
						listeGestionProfils.add(new GestionProfil(prf,statut.RAS));
					}
				}
				if(nomRole.equals("contributeur")){
					for(String dn : workspace.getGroupeContributeurs().getListeMembers()){
						listeGestionPersonnes.add(new GestionPerson(personneInstance.findPersonByDn(dn),statut.RAS));
					}
					for(Profil prf:workspace.getListeProfilContributeurs()){
						listeGestionProfils.add(new GestionProfil(prf,statut.RAS));
					}
				}
				if(nomRole.equals("lecteur")){
					for(String dn : workspace.getGroupeLecteurs().getListeMembers()){
						listeGestionPersonnes.add(new GestionPerson(personneInstance.findPersonByDn(dn),statut.RAS));
					}
					for(Profil prf:workspace.getListeProfilLecteurs()){
						listeGestionProfils.add(new GestionProfil(prf,statut.RAS));
					}
				}
				
				workspace.setListeGestionPersonnes(listeGestionPersonnes);
				workspace.setListeGestionProfils(listeGestionProfils);
			}
			
			
			request.setAttribute("nomRole",nomRole);
			request.setAttribute("provenance",provenance);
			
			// breadcrumb
			// breadcrum
			Map<String, String> renderParams = new HashMap<String, String>();
	    	renderParams.put("path", workspace.getPath());
	    	renderParams.put("nom", workspace.getNom());
	    	renderParams.put("nomRole",nomRole);
			adaptBreadcrumb("modifierRole", renderParams, request);
			
			return "modifRole";
		}
	}
	

	@RenderMapping(params = "action=demandeCreation") 	
	public String showFormulaireCreation(RenderRequest request,RenderResponse response, PortletSession session) throws Exception {
	
		Person userConnecte = this.getUserConnecte(request);
			
		NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
		nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
		String nuxeoPath = this.getNuxeoPath(request);
		
		Workspace wks = context.getBean("workspace",Workspace.class);
		
		if(nuxeoPath!=null && !nuxeoPath.trim().isEmpty()&&!nuxeoPath.equalsIgnoreCase(pathOpenWorkspaceDirectory)){
			//Mode workspace organisationnel
			wks.setModeOpenWorkspace(false);
		
			if(nuxeoPath==null||nuxeoPath.trim().isEmpty()){
				return "erreur";
			}else{
				try {
					Document parent = (Document) nuxeoCtl.executeNuxeoCommand(new FetchWorkspaceCommand(nuxeoPath,context));
					if(parent==null){
						PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
						addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
						return "erreur";
					}else{
						wks.setPathParent(nuxeoPath);

						
					}
				} catch (Exception e1) {
					PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
					addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
					return "erreur";
				}
			}
		
     
             Document doc = (Document) request.getAttribute(Constants.PORTLET_ATTR_SPACE_CONFIG);
             String rne = (String) doc.getProperties().get("dc:publisher");
             wks.setSourceOrganisationnelle(rne);
         
             
            boolean writeEnable = (Boolean) nuxeoCtl.executeNuxeoCommand(new IsWriteEnableCommand(wks.getPathParent(),userConnecte,profilInstance));
 			if(writeEnable){
 				session.setAttribute("workspace", wks);
 				request.setAttribute("workspace", wks);
 				return "creationWorkspace";
 			}else{
 				request.setAttribute("osivia.emptyResponse", "1");
 				return "nonAutorise";
 			}
		
			
		}else{
			//mode open workspace
			wks.setModeOpenWorkspace(true);
			wks.setPathParent(pathOpenWorkspaceDirectory);
			wks.setSourceOrganisationnelle("");
			session.setAttribute("workspace", wks);
			request.setAttribute("workspace", wks);
			
			if(! userConnecte.hasRole(cnRoleCreationOpenWks)){
				request.setAttribute("osivia.emptyResponse", "1");
				return "nonAutorise";
			}else{
				return "creationWorkspace";
			}	
		}
		
		
		
				
		

	}
	
	@RenderMapping(params = "action=confirmationCreation") 		
	public String confirmationCreation(@ModelAttribute("workspace") Workspace workspace, RenderRequest request, RenderResponse response) throws Exception {
	
		if(workspace==null||workspace.getNom().trim().isEmpty()){
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
			return "creationWorkspace";
		}else{
			request.setAttribute("workspace", workspace);
			return "confirmationCreation";
		}
	}

	
	@RenderMapping(params = "action=nonAutorise")
	public String showNonAutorise(RenderRequest request) throws Exception {
	
		return "nonAutorise";
	}
	
	@ActionMapping(value="demandeCreationAction")
	public void demandeCreationAction(@ModelAttribute("workspace") Workspace workspace, BindingResult result, ActionRequest request, ActionResponse response) throws ToutaticeAnnuaireException, UnsupportedEncodingException {		
		
		
		modifyValidator.validate(workspace, result);
	
		if (result.hasErrors()) {
			//mémorisation des informations saisies par l'utilisateur pour affichage
			request.setAttribute("workspace",workspace);
			response.setRenderParameter("action", "demandeCreation");
			
		}else{
			
			NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
		
			//Calcul du shortname à partir du nom saisi
			String shortname = workspace.getNom();
			shortname = shortname.replaceAll(" et | de | le | la | les | ou | un | une | des | au | aux | du ","-");
			shortname= GestionWorkspaceController.sansAccents(shortname);
			shortname = shortname.replaceAll(" ", "-");
			shortname = shortname.replaceAll("[\\W]",""); //suppression caractères spéciaux
			
			
			if(shortname.length()>30){
				shortname = shortname.substring(0, 30);
			}
			if(shortname.length()<3){
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.min3char", NotificationsType.ERROR);
				response.setRenderParameter("action", "demandeCreation");
			}
			
			else{
				if(workspace.getSourceOrganisationnelle()!=null && !workspace.getSourceOrganisationnelle().trim().isEmpty()){
					shortname = workspace.getSourceOrganisationnelle()+"_"+shortname;
				}
				
				// Vérification non existence workspace avec même title dans le même dossier
				String nom = workspace.getNom().replace("'", "\\'");

				String requete = "ecm:isProxy=0 and ecm:primaryType='Workspace' and dc:title='"+nom+"' and ecm:path startswith '"+workspace.getPathParent()+"'";
				Documents results=null;
				try{
					results = (Documents) nuxeoCtl.executeNuxeoCommand(new QueryDocumentCommand(requete));
				}catch(Exception e){
					PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
					addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
					response.setRenderParameter("action", "demandeCreation");
				}
				if (results==null || !results.isEmpty()){
					if(results==null){
						PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
						addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
						response.setRenderParameter("action", "demandeCreation");
					}else{
						PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
						addNotification(pcc, "label.sameTitle", NotificationsType.ERROR);
						response.setRenderParameter("action", "demandeCreation");
					}
				}else{
					// Vérification non existence d'un workspace avec le même shortname
					// si existence, changer shortname
					boolean shortNameUnique = false;
					String nomShortNameUnique=shortname;
					String prefixe = "";
					
					
					while(!shortNameUnique){
						try{
							
							requete = "ecm:isProxy=0 and ecm:currentLifeCycleState!='deleted' and ecm:primaryType='Workspace' and "+config.getWsShortName()+"='"+nomShortNameUnique+"'";
							results = (Documents) nuxeoCtl.executeNuxeoCommand(new QueryDocumentCommand(requete));
							if (results.isEmpty()){
								// Vérification non existence groupes  de même noms
								Profil p_animateurs = profilInstance.findProfilByCn(prefixe+nomShortNameUnique+config.getGroupAnimateurs());
								Profil p_contributeurs = profilInstance.findProfilByCn(prefixe+nomShortNameUnique+config.getGroupContributeurs());
								Profil p_lecteurs = profilInstance.findProfilByCn(prefixe+nomShortNameUnique+config.getGroupLecteurs());
								
								if(p_animateurs!=null||p_contributeurs!=null||p_lecteurs!=null){
									//un des groupes existe déjà
									if(nomShortNameUnique.equals(shortname)){
										nomShortNameUnique = nomShortNameUnique.concat("-1");
									}else{
										nomShortNameUnique = this.incrementeShortName(nomShortNameUnique);
									}
									
								}else{
									//nom unique
									shortNameUnique = true;
									
									String desc = workspace.getDescription().substring(0,1).toUpperCase()+workspace.getDescription().substring(1);
									workspace.setDescription(desc);
									
									workspace.setShortname(nomShortNameUnique);
									workspace.getGroupeAnimateurs().setCn(prefixe+nomShortNameUnique+config.getGroupAnimateurs());
									workspace.getGroupeContributeurs().setCn(prefixe+nomShortNameUnique+config.getGroupContributeurs());
									workspace.getGroupeLecteurs().setCn(prefixe+nomShortNameUnique+config.getGroupLecteurs());
								}
							}else{
								//un workspace portant ce shortName existe déjà
								if(nomShortNameUnique.equals(shortname)){
									nomShortNameUnique = nomShortNameUnique.concat("-1");
								}else{
									nomShortNameUnique = this.incrementeShortName(nomShortNameUnique);
								}
							}
							
							
						}catch(Exception e){
							PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
							addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
							response.setRenderParameter("action", "demandeCreation");
						}
					}
					workspace.setShortname(nomShortNameUnique);
					response.setRenderParameter("action", "confirmationCreation");
					
				}
				
			
			}
			
			
			

		}
			
		
	}
	
	@ActionMapping(value="creationWorkspace") 	
	public void creationWorkspace(@ModelAttribute("workspace") Workspace workspace, ActionRequest request, ActionResponse response, PortletSession session) throws ToutaticeAnnuaireException {		
		
		Person userConnecte = this.getUserConnecte(request);
		
		
		// Création workspace dans nuxeo
		try {
			
			Bundle bundle = getBundleFactory().getBundle(new PortalControllerContext(portletContext, request, null).getRequest().getLocale());
			
			NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
			nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
			Document wks = (Document) nuxeoCtl.executeNuxeoCommand(new CreateWorkspaceCommand(context, bundle, workspace,userConnecte, profilInstance, organisationInstance));
		
			workspace = this.getWorkspace(request, nuxeoCtl, wks.getPath(), userConnecte);
		
			NuxeoController nuxeoCtl2 = new NuxeoController(request, null, portletContext);
			nuxeoCtl2.executeNuxeoCommand(new RefreshPrincipalCommand(wks));
			
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			
			
			String workspaceUrl = getPortalUrlFactory().getCMSUrl(pcc, null, workspace.getPath(), null, null, null, null, null, null, null);
			addNotification(pcc, "label.creationOK", NotificationsType.SUCCESS, workspaceUrl);
			
			
			
			request.setAttribute("workspace", workspace);
			response.setRenderParameter("action", "consulterEspace");	
			response.setRenderParameter("path",workspace.getPath());
			
			
			
		} catch (Exception e) {
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
			response.setRenderParameter("action", "demandeCreation");	
		}
	
		
		
	}

	@ActionMapping(value="annuler") 
	public void annulerCreation(ActionResponse response,  ActionRequest request) throws ToutaticeAnnuaireException, NamingException {
		response.setRenderParameter("action", "demandeCreation");
	}
	
	@ActionMapping(value="nouvelleCreation") 
	public void nouvelleCreation(ActionResponse response,  ActionRequest request) throws ToutaticeAnnuaireException, NamingException {
		response.setRenderParameter("action", "demandeCreation");
	}
	
	@ActionMapping(value="annulerModif") 
	public void annuler(@ModelAttribute("workspace") Workspace workspace, @RequestParam String provenance, ActionResponse response,  ActionRequest request,  ModelMap model, PortletSession session) throws ToutaticeAnnuaireException, NamingException {	
		response.setRenderParameter("action", provenance);
		response.setRenderParameter("path",workspace.getPath());
		
		session.removeAttribute("workspace");
		model.remove("workspace");
	}
	
	@ActionMapping(value="rechercherPersonne") 	
	public void rechercherPerson(@ModelAttribute("workspace") Workspace workspace, @RequestParam String provenance, @RequestParam String nomRole, ActionRequest request, ActionResponse response) throws ToutaticeAnnuaireException {		
		
		if (workspace==null) {
			response.setRenderParameter("action", "listeWks");	
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
		} else {
	
			if(workspace.getUidPersonne().length()<3 ){
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.3charmin", NotificationsType.ERROR);
				workspace.setListePersonRecherche(new ArrayList<Person>());
			}
			else {
		
				workspace.setListePersonRecherche(personneInstance.findPersonneMultiCriteres(workspace.getUidPersonne(), workspace.getSourceOrganisationnelle(), null,"", "sn"));
				if(workspace.getListePersonRecherche().size()<1){
					PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
					addNotification(pcc, "label.noresultMembre", NotificationsType.INFO);
				}
			}	

			request.setAttribute("workspace", workspace);
			response.setRenderParameter("nomRole",nomRole);
			response.setRenderParameter("provenance",provenance);
			response.setRenderParameter("action", "modifierRole");
	}
	}
	
	@ActionMapping(value="rechercherGroupe") 	
	public void rechercherGroupe(@ModelAttribute("workspace") Workspace workspace, @RequestParam String cn, @RequestParam String provenance, @RequestParam String nomRole, ActionRequest request, ActionResponse response) throws ToutaticeAnnuaireException {		
	
		if (workspace==null) {
			response.setRenderParameter("action", "listeWks");	
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
		} else {
	
		
			workspace.setCnGroupe(cn);
			if(cn.length()<3 ){
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.3charmin", NotificationsType.ERROR);
			}
			else {
		
				workspace.setListeGroupeRecherche(profilInstance.findProfilByDebutCn(cn));
				if(workspace.getListeGroupeRecherche().size()<1){
					PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
					addNotification(pcc, "label.noresultProfil", NotificationsType.INFO);
				}
			}	
			response.setRenderParameter("provenance",provenance);
			response.setRenderParameter("nomRole",nomRole);
			response.setRenderParameter("action", "modifierRole");
	}
	}

	
	@ActionMapping(value="ajouterGroupe") 
	public void ajouterGroupe(@ModelAttribute("workspace") Workspace workspace, @RequestParam String cn, @RequestParam String provenance, @RequestParam String nomRole, ActionResponse response, ActionRequest request) throws Exception {	
	
		if (workspace==null) {
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
			response.setRenderParameter("action", "listeWks");	
		} else {
			Profil prf = profilInstance.findProfilByCn(cn);
			if(prf!=null){
				if(nomRole.equals("animateur")){
					boolean existe = false;
					for(Profil p : workspace.getListeProfilAnimateurs()){
						if(p.getCn().equalsIgnoreCase(cn)){
							existe=true;
							PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
							addNotification(pcc, "label.groupeDejaPresent", NotificationsType.ERROR);
						}
					}
					if(!existe){
						workspace.getListeProfilAnimateurs().add(prf);
						
						boolean present = false;
						for(GestionProfil gp : workspace.getListeGestionProfils()){
							if(gp.getProfil().getCn().equalsIgnoreCase(cn)){
								present = true;
								gp.setStatut(statut.AJOUTER);
							}
						}
						if(!present){
							workspace.getListeGestionProfils().add(new GestionProfil(prf,statut.AJOUTER));
						}
					}
				}
				if(nomRole.equals("contributeur")){
					boolean existe = false;
					for(Profil p : workspace.getListeProfilContributeurs()){
						if(p.getCn().equalsIgnoreCase(cn)){
							existe=true;
							PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
							addNotification(pcc, "label.groupeDejaPresent", NotificationsType.ERROR);
						}
					}
					if(!existe){
						workspace.getListeProfilContributeurs().add(prf);
						boolean present = false;
						for(GestionProfil gp : workspace.getListeGestionProfils()){
							if(gp.getProfil().getCn().equalsIgnoreCase(cn)){
								present = true;
								gp.setStatut(statut.AJOUTER);
							}
						}
						if(!present){
							workspace.getListeGestionProfils().add(new GestionProfil(prf,statut.AJOUTER));
						}
					}
				}
				if(nomRole.equals("lecteur")){
					boolean existe = false;
					for(Profil p : workspace.getListeProfilLecteurs()){
						if(p.getCn().equalsIgnoreCase(cn)){
							existe=true;
							PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
							addNotification(pcc, "label.groupeDejaPresent", NotificationsType.ERROR);
						}
					}
					if(!existe){
						workspace.getListeProfilLecteurs().add(prf);
						boolean present = false;
						for(GestionProfil gp : workspace.getListeGestionProfils()){
							if(gp.getProfil().getCn().equalsIgnoreCase(cn)){
								present = true;
								gp.setStatut(statut.AJOUTER);
							}
						}
						if(!present){
							workspace.getListeGestionProfils().add(new GestionProfil(prf,statut.AJOUTER));
						}
					}
				}
			}
			response.setRenderParameter("provenance",provenance);
			response.setRenderParameter("action", "modifierRole");		
			response.setRenderParameter("nomRole",nomRole);
		}
	}
	
	@ActionMapping(value="ajoutPersonne") 
	public void ajoutPersonne(@ModelAttribute("workspace") Workspace workspace, @RequestParam String uid, @RequestParam String provenance, @RequestParam String nomRole, ActionRequest request, ActionResponse response) throws Exception {	
		
		if (workspace==null) {
			response.setRenderParameter("action", "listeWks");	
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
	
		} else {
			Person prs = personneInstance.findUtilisateur(uid);
			if(prs!=null){
				if(nomRole.equals("animateur")){
					boolean existe = false;
					for(String s : workspace.getGroupeAnimateurs().getListeMembers()){
						if(s.equalsIgnoreCase(prs.getDn())){
							existe=true;
							PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
							addNotification(pcc, "label.personneDejaPresente", NotificationsType.ERROR);
						}
					}
					if(!existe){
						workspace.getGroupeAnimateurs().addMember(prs.getDn());
						boolean present = false;
						for(GestionPerson gp : workspace.getListeGestionPersonnes()){
							if(gp.getPersonne().getUid().equalsIgnoreCase(uid)){
								present = true;
								gp.setStatut(statut.AJOUTER);
							}
						}
						if(!present){
							workspace.getListeGestionPersonnes().add(new GestionPerson(prs,statut.AJOUTER));
						}
					}
				}
				if(nomRole.equals("contributeur")){
					boolean existe = false;
					for(String s : workspace.getGroupeContributeurs().getListeMembers()){
						if(s.equalsIgnoreCase(uid)){
							existe=true;
							PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
							addNotification(pcc, "label.personneDejaPresente", NotificationsType.ERROR);
						}
					}
					if(!existe){
						workspace.getGroupeContributeurs().addMember(prs.getDn());
						boolean present = false;
						for(GestionPerson gp : workspace.getListeGestionPersonnes()){
							if(gp.getPersonne().getUid().equalsIgnoreCase(uid)){
								present = true;
								gp.setStatut(statut.AJOUTER);
							}
						}
						if(!present){
							workspace.getListeGestionPersonnes().add(new GestionPerson(prs,statut.AJOUTER));
						}
					}
				}
				if(nomRole.equals("lecteur")){
					boolean existe = false;
					for(String s : workspace.getGroupeLecteurs().getListeMembers()){
						if(s.equalsIgnoreCase(uid)){
							existe=true;
							PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
							addNotification(pcc, "label.personneDejaPresente", NotificationsType.ERROR);
						}
					}
					if(!existe){
						workspace.getGroupeLecteurs().addMember(prs.getDn());
						boolean present = false;
						for(GestionPerson gp : workspace.getListeGestionPersonnes()){
							if(gp.getPersonne().getUid().equalsIgnoreCase(uid)){
								present = true;
								gp.setStatut(statut.AJOUTER);
							}
						}
						if(!present){
							workspace.getListeGestionPersonnes().add(new GestionPerson(prs,statut.AJOUTER));
						}
					}
				}
			}
			response.setRenderParameter("provenance",provenance);
			request.setAttribute("workspace", workspace);
			response.setRenderParameter("action", "modifierRole");		
			response.setRenderParameter("nomRole",nomRole);
		}
	}
	
	@ActionMapping(value="deleteGroupe") 
	public void deleteGroupe(@ModelAttribute("workspace") Workspace workspace, @RequestParam String cn, @RequestParam String nomRole, @RequestParam String provenance, ActionResponse response, ActionRequest request) {
	
		if (workspace==null) {
			response.setRenderParameter("action", "listeWks");	
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
		} else {
			if(nomRole.equals("animateur")){
				List<Profil> liste = new ArrayList<Profil>();
				for(Profil p : workspace.getListeProfilAnimateurs()){
					if(!p.getCn().equalsIgnoreCase(cn)){
						liste.add(p);
					}
				}
				workspace.setListeProfilAnimateurs(liste);
			}
			if(nomRole.equals("contributeur")){
				List<Profil> liste = new ArrayList<Profil>();
				for(Profil p : workspace.getListeProfilContributeurs()){
					if(!p.getCn().equalsIgnoreCase(cn)){
						liste.add(p);
					}
				}
				workspace.setListeProfilContributeurs(liste);
			}
			if(nomRole.equals("lecteur")){
				List<Profil> liste = new ArrayList<Profil>();
				for(Profil p : workspace.getListeProfilLecteurs()){
					if(!p.getCn().equalsIgnoreCase(cn)){
						liste.add(p);
					}
				}
				workspace.setListeProfilLecteurs(liste);
			}
			
			for(GestionProfil gp : workspace.getListeGestionProfils()){
				if(gp.getProfil().getCn().equalsIgnoreCase(cn)){
					gp.setStatut(statut.SUPPRIMER);
				}
			}
			response.setRenderParameter("provenance",provenance);
			request.setAttribute("workspace", workspace);
			response.setRenderParameter("action", "modifierRole");
			response.setRenderParameter("nomRole",nomRole);
		}
	}
	
	@ActionMapping(value="deletePersonne") 
	public void deletePersonne(@ModelAttribute("workspace") Workspace workspace, @RequestParam String uid, @RequestParam String provenance, @RequestParam String nomRole, ActionResponse response, ActionRequest request) {

		Person userConnecte = this.getUserConnecte(request);
	
		if (workspace==null) {
			response.setRenderParameter("action", "listeWks");	
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
		} else {
			if(uid.equalsIgnoreCase(userConnecte.getUid())){
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.suppressionUserConnecte", NotificationsType.ERROR);
				response.setRenderParameter("action", "modifierRole");	
			}else{
				Person prs = personneInstance.findUtilisateur(uid);
				if(prs!=null){
					try {
						if(nomRole.equals("animateur")){
							workspace.getGroupeAnimateurs().removeMember(prs.getDn());
						}
						if(nomRole.equals("contributeur")){
							workspace.getGroupeContributeurs().removeMember(prs.getDn());
						}
						if(nomRole.equals("lecteur")){
							workspace.getGroupeLecteurs().removeMember(prs.getDn());
						}
					}catch(Exception e){
						PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
						addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
						response.setRenderParameter("action", "modifierRole");		
					}
					for(GestionPerson gp : workspace.getListeGestionPersonnes()){
						if(gp.getPersonne().getUid().equalsIgnoreCase(uid)){
							gp.setStatut(statut.SUPPRIMER);
						}
					}
				}
			}
			response.setRenderParameter("provenance",provenance);
			request.setAttribute("workspace", workspace);
			response.setRenderParameter("action", "modifierRole");	
			response.setRenderParameter("nomRole",nomRole);
		}
	}
	
	@ActionMapping(value="validerModifWks") 
	public void validerModifWks(@ModelAttribute("formModifWks") FormModifWks formulaire,  BindingResult result, @RequestParam String path,ActionResponse response,  ActionRequest request, PortletSession session, ModelMap model) throws ToutaticeAnnuaireException, NamingException {

		modifyValidator.validate(formulaire, result);
		
		if (!result.hasErrors()) {
	
			NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
			
			// Update workspace dans nuxeo
			try {
				if(StringUtils.isEmpty(path)||path.equalsIgnoreCase(pathOpenWorkspaceDirectory)){
					//cas de l'openWorkspace : update en mode super user
					nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
				}
				
				Document wks = (Document) nuxeoCtl.executeNuxeoCommand(new UpdateWorkspaceCommand(formulaire,path));
	
				response.setRenderParameter("action", "consulterEspace");	
				response.setRenderParameter("path",wks.getPath());
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.modifOK", NotificationsType.SUCCESS);
				session.removeAttribute("workspace");
				model.remove("workspace");

			} catch (Exception e) {
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
				response.setRenderParameter("action", "listeWks");
			}
			
		}else{
			
			response.setRenderParameter("path", path);
			response.setRenderParameter("action", "consulterEspace");
		}
	}
	
	@ActionMapping(value="validerModifRole") 
	public void validerModifRole(@ModelAttribute("workspace") Workspace workspace, @ModelAttribute FormGestion form, @RequestParam String nomRole, @RequestParam String provenance, ActionResponse response,  ActionRequest request, ModelMap model, PortletSession session) throws ToutaticeAnnuaireException, NamingException {
	
		if(workspace==null) {
			response.setRenderParameter("action", "listeWks");
			PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
			addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
		} else {		
			
			NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
			nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
			
			// Update workspace dans nuxeo
			try {
				nuxeoCtl.executeNuxeoCommand(new UpdateRolesWksCommand(workspace));
				workspace.getGroupeAnimateurs().updateProfil();
				workspace.getGroupeContributeurs().updateProfil();
				if(config.getEnableReaders()) {
					workspace.getGroupeLecteurs().updateProfil();
				}
					
				response.setRenderParameter("action", "consulterRole");
				response.setRenderParameter("path",workspace.getPath());
				session.removeAttribute("workspace");
				model.remove("workspace");
				
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.modifOK", NotificationsType.SUCCESS);
				
				request.setAttribute("osivia.updateContents", "true");
			} 
			catch (Exception e) {
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
				response.setRenderParameter("action", "listeWks");
			}
			
		}
	}
	
	private String getNuxeoPath(PortletRequest request){
		
		String nuxeoPath = request.getParameter("nuxeoPath");		
		
		if(StringUtils.isEmpty(nuxeoPath)){
			PortalWindow window = WindowFactory.getWindow(request);
			nuxeoPath = window.getProperty("toutatice.workspace.creationworkspace.nuxeopath");
			if(StringUtils.isEmpty(nuxeoPath)){
				nuxeoPath = window.getProperty("nuxeoPath");
			}
		}
	
		if(!StringUtils.isEmpty(nuxeoPath)){
			if(nuxeoPath.contains("${basePath}")||nuxeoPath.contains("${domainPath}")){	
				nuxeoPath = this.calculNuxeoPath(nuxeoPath,request);
			}
	
		}else{
			nuxeoPath=pathOpenWorkspaceDirectory;
			
		}
		
		return nuxeoPath;
	}
	
	private String calculNuxeoPath(String nuxeoPath, PortletRequest request){
		NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);	
		String path = nuxeoCtl.getBasePath();
		String domainPath = nuxeoCtl.getDomainPath();
		
		if(domainPath!=null && nuxeoPath.startsWith("${domainPath}")){
			return nuxeoPath = nuxeoPath.replace("${domainPath}", domainPath);
		}else{
		
			if(path!=null){
				
				if(nuxeoPath.startsWith("${basePath}")){
					return nuxeoPath = nuxeoPath.replace("${basePath}", path);
				
				}else{
					if(path.startsWith("/")){
						path=path.substring(1);
					}
					String[] tabBasePath = path.split("/");
					String[] tabNuxeoPath = (nuxeoPath.substring(0,nuxeoPath.indexOf("${basePath}"))).split("/");
					
					if(tabNuxeoPath.length>=tabBasePath.length){
						return "";
					}else{
						String s = "";
						int nb = tabBasePath.length - tabNuxeoPath.length;
						for(int i=0;i<nb;i++){
							s = s.concat("/"+tabBasePath[i]);
						}
						s.concat(nuxeoPath).substring(nuxeoPath.indexOf("${basePath}"));
						
						return s;
					}
				}
			
				
			}else{
				return "";
			}
		}
		
	}
	

	
	
	
	private void setGroupesWorkspace(PortletRequest request, NuxeoController nuxeoCtl, Workspace wks) throws Exception{
		
		//Test existence shortname, si le workspace n'a pas de shortname on ne peut pas le reprendre
		if(wks.getShortname()==null){
			throw new Exception();
			
		}else{
			wks.getListeProfilAnimateurs().clear();
			wks.getListeProfilContributeurs().clear();
			wks.getListeProfilLecteurs().clear();
			wks.getMembresGroupeAnimateurs().clear();
			wks.getMembresGroupeContributeurs().clear();
			wks.getMembresGroupeLecteurs().clear();
			
			String prefixe="";
			Organisation org = null;
			if(wks.getSourceOrganisationnelle() != null) {
				org = organisationInstance.findOrganisation(wks.getSourceOrganisationnelle());
			}
			
			
			// Vérification de l'existence des profils animateur, contributeur et lecteur
			// Si l'un des profils n'existe pas il est créé, et rattaché à la structure a laquelle appartient le workspace
			
			Person userConnecte = getUserConnecte(request);
			
			
			Bundle bundle = getBundleFactory().getBundle(new PortalControllerContext(portletContext, request, null).getRequest().getLocale());
			
			
			
			Profil p_animateur = profilInstance.findProfilByCn(prefixe+wks.getShortname()+config.getGroupAnimateurs());
			if(p_animateur==null){
				
				String roleAnim = bundle.getString("label.animateur");
				
				p_animateur = profilInstance.getNewProfil(prefixe+wks.getShortname()+config.getGroupAnimateurs(), roleAnim+" "+wks.getNom(), roleAnim+" "+wks.getNom(), 
						"space-group", Profil.typePeuplement.EXPLICITE);	
				p_animateur.addExplicitManager(p_animateur.getDn());
				p_animateur.addMember(userConnecte.getDn());
				
				
				try {
					p_animateur.create();
				
					if(org!=null){
						org.addProfil(p_animateur.getDn());
						org.update();
					}
					
				} catch (ToutaticeAnnuaireException e) {
					throw e;
				}
			}
			
			Profil p_contributeur = profilInstance.findProfilByCn(prefixe+wks.getShortname()+config.getGroupContributeurs());
			if(p_contributeur==null){
				
				String roleContrib = bundle.getString("label.contributeur");
				
				p_contributeur = profilInstance.getNewProfil(prefixe+wks.getShortname()+config.getGroupContributeurs(), roleContrib+" "+wks.getNom(), roleContrib+" "+wks.getNom(), 
						"space-group", Profil.typePeuplement.EXPLICITE);
				p_contributeur.addExplicitManager(p_animateur.getDn());
				p_contributeur.addMember(userConnecte.getDn());
				try {
					p_contributeur.create();
					if(org!=null){
						org.addProfil(p_contributeur.getDn());
						org.update();
					}
				} catch (ToutaticeAnnuaireException e) {
					throw e;
				}
			}
			
			Profil p_lecteur = profilInstance.findProfilByCn(prefixe+wks.getShortname()+config.getGroupLecteurs());
			if(p_lecteur==null && config.getEnableReaders()){
				
				String roleLecteur = bundle.getString("label.lecteur");
				
				p_lecteur = profilInstance.getNewProfil(prefixe+wks.getShortname()+config.getGroupLecteurs(), roleLecteur+" "+wks.getNom(), roleLecteur+" "+wks.getNom(), 
						"space-group", Profil.typePeuplement.EXPLICITE);
				p_lecteur.addExplicitManager(p_animateur.getDn());
				p_lecteur.addMember(userConnecte.getDn());
				
				try {
					p_lecteur.create();
					if(org!=null){
						org.addProfil(p_lecteur.getDn());
						org.update();
					}
				} catch (ToutaticeAnnuaireException e) {
					throw e;
				}
			}
			
			//Récupération des personnes membres d'un des 3 groupes animateurs, contributeurs ou lecteurs
			if(p_animateur!=null){
				wks.setGroupeAnimateurs(p_animateur);
				for(String dn : p_animateur.getListeMembers()){
					Person p = personneInstance.findPersonByDn(dn);
					wks.getMembresGroupeAnimateurs().add(p);
				}
			}
			
			if(p_contributeur!=null){
				wks.setGroupeContributeurs(p_contributeur);
				for(String dn : p_contributeur.getListeMembers()){
					Person p = personneInstance.findPersonByDn(dn);
					wks.getMembresGroupeContributeurs().add(p);
				}
			}
			
			if(p_lecteur!=null){
				wks.setGroupeLecteurs(p_lecteur);
				for(String dn : p_lecteur.getListeMembers()){
					Person p = personneInstance.findPersonByDn(dn);
					wks.getMembresGroupeLecteurs().add(p);
				}
			}
			
			//Récupération des profils associés au workspace 
			if(p_animateur!=null&&p_contributeur!=null&&p_lecteur!=null){
				JSONArray rows = (JSONArray) nuxeoCtl.executeNuxeoCommand(new GetGroupListCommand(wks.getPath()));
				@SuppressWarnings("rawtypes")
				Iterator it = rows.iterator();
				
				
				while (it.hasNext()) {
					JSONObject obj = (JSONObject) it.next();
					String name = (String) obj.get("userOrGroup");
					String permission = (String) obj.get("permission");
					
					//Cas d'un profil
					Profil prf = profilInstance.findProfilByCn(name);
					if(prf!=null){
						// on ne présente pas les groupes d'administration de nuxeo
						if(prf.getType()==null || (!prf.getType().equals("Administration") && !prf.getType().equalsIgnoreCase("space-group"))){
							
						
							if(permission.equalsIgnoreCase("Everything")){
								wks.getListeProfilAnimateurs().add(prf);
							}
							if(permission.equalsIgnoreCase("ReadWrite")){
								wks.getListeProfilContributeurs().add(prf);
							}
							if(permission.equalsIgnoreCase("Read")){
								wks.getListeProfilLecteurs().add(prf);
							}		
						}
						
					}else{
						if(!name.equalsIgnoreCase("administrators")){
							//Cas d'une personne (normalement ce cas ne se présente pas
							Person p = personneInstance.findUtilisateur(name);
							if(p!=null){
								if(permission.equalsIgnoreCase("Everything")){
									wks.getMembresGroupeAnimateurs().add(p);
									wks.getGroupeAnimateurs().addMember(p.getDn());
								}
								if(permission.equalsIgnoreCase("ReadWrite")){
									wks.getMembresGroupeContributeurs().add(p);
									wks.getGroupeContributeurs().addMember(p.getDn());
								}
								if(permission.equalsIgnoreCase("Read")){
									wks.getMembresGroupeLecteurs().add(p);
									wks.getGroupeLecteurs().addMember(p.getDn());
								}
							}
						}
					}
				}
				
				wks.getGroupeAnimateurs().updateProfil();
				wks.getGroupeContributeurs().updateProfil();
				wks.getGroupeLecteurs().updateProfil();
			//	nuxeoCtl.executeNuxeoCommand(new UpdateRolesWksCommand(wks));
			}
		}
	
	}
	
	
	private String incrementeShortName(String nom){
		int i = nom.length();
		
		if(nom.substring(i-1, i).matches("[0-9]"))
			if(nom.charAt(i-1) != '9'){
				char c = nom.charAt(i-1);
				nom= nom.substring(0, i-1)+(char)(c+1);
			}else{
				//unité à 9
				if(nom.substring(i-2, i-1).matches("[0-9]")){
					if(nom.charAt(i-1) != '9'){
						char c = nom.charAt(i-2);
						nom= nom.substring(0, i-2)+(char)(c+1)+"0";
					}else{
						//99
						if(nom.substring(i-3, i-2).matches("[0-9]")){
							if(nom.charAt(i-3) != '9'){
								char c = nom.charAt(i-3);
								nom= nom.substring(0, i-3)+(char)(c+1)+"00";
							}else{
								char c = nom.charAt(i-4);
								nom= nom.substring(0, i-4)+(char)(c+1)+"000";
							}
						}else{
							nom= nom.substring(0, i-3)+"100";
						}
					}
				}else{
					nom=nom.substring(0,i-1)+"10";
				}
		
			
		}else{
			nom = nom.concat("1");
		}
		return nom;
	}
	
	
	
	public static String sansAccents(String source) {
		return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}
	
	public void setPortletConfig(PortletConfig portletConfig) {
		this.portletConfig = portletConfig;
	}

	public void setPortletContext(PortletContext portletContext) {
		this.portletContext = portletContext;
	}

	
	private void adaptBreadcrumb(String action, Map<String, String> renderParams, RenderRequest request) {
		
		PortalControllerContext pcc = new PortalControllerContext(portletContext, request, null);
		
		
        List<PortletPathItem> portletPath = new ArrayList<PortletPathItem>();
        
        // Page liste de workspaces toujours là sauf si venue de la menubar 
		PortalWindow window = WindowFactory.getWindow(request);
		String actionWindow = window.getProperty("action");
		if(!"consulterRole".equals(actionWindow)) {

			PortletPathItem pathItem = new PortletPathItem(renderParams ,"Gestion des workspaces");		
			portletPath.add(pathItem);
		}
		
		// Page consulter rôles
		if("consulterRole".equals(action) || "modifierRole".equals(action)) {

			PortletPathItem pathItem2 = new PortletPathItem(renderParams,"Gérer les participants de "+renderParams.get("nom"));
			portletPath.add( pathItem2);
		
		}
		
		if("modifierRole".equals(action)) {

			Bundle bundle = getBundleFactory().getBundle(pcc.getRequest().getLocale());
			String role = bundle.getString("label."+renderParams.get("nomRole"));
			
			PortletPathItem pathItem2 = new PortletPathItem(renderParams,"Modifier le rôle "+role);
			portletPath.add( pathItem2);
		}
		
		request.setAttribute("osivia.portletPath", portletPath);
		
	}
}
