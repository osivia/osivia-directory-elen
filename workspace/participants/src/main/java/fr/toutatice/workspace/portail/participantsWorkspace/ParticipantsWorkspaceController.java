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

package fr.toutatice.workspace.portail.participantsWorkspace;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.IDirectoryService;
import org.osivia.portal.api.directory.IDirectoryServiceLocator;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.directory.helper.DirectoryPortlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.workspace.portail.participantsWorkspace.bean.ParticipantUrl;
import fr.toutatice.workspace.portail.participantsWorkspace.nuxeo.FetchWorkspaceCommand;
import fr.toutatice.workspace.portail.participantsWorkspace.nuxeo.GetGroupListCommand;


@SuppressWarnings("restriction")
@Controller
@RequestMapping("VIEW")


public class ParticipantsWorkspaceController extends CMSPortlet implements PortletContextAware, PortletConfigAware{

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.nuxeo");
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Person personneInstance;
	
	@Autowired
	private Profil profilInstance;
	
	@Autowired
	private ParticipantsConfig config;
	
	@Autowired
	private IDirectoryServiceLocator directoryServiceLocator;
	
	@Autowired
	@Qualifier("organisation")
	private Organisation organisationInstance;
	
	private PortletContext portletContext;
	private PortletConfig portletConfig;
	
	
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
	
	public Person initUserConnecte(RenderRequest request) {
		DirectoryPerson person = (DirectoryPerson) request.getAttribute(Constants.ATTR_LOGGED_PERSON);
		Person userConnecte = personneInstance.findUtilisateur(person.getUid());
		return userConnecte;
	}
	
	@RenderMapping	
	public String showListeWorkspace(RenderRequest request,RenderResponse response) throws Exception {
	
		String retour="";
	
		Person userConnecte = this.initUserConnecte(request);
		if(userConnecte==null){
			request.setAttribute("osivia.emptyResponse", "1");
			return "nonconnecte";
		}else{
			
			NuxeoController nuxeoCtl = new NuxeoController(request, null, portletContext);
			nuxeoCtl.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
			String path = nuxeoCtl.getBasePath();
			
			
			if(path==null||path.trim().isEmpty()){
				request.setAttribute("osivia.emptyResponse", "1");
				retour="erreur";
			}else{
				Workspace wks = (Workspace) nuxeoCtl.executeNuxeoCommand(new FetchWorkspaceCommand(path,context));
				
				if(wks==null){
					PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
					addNotification(pcc, "label.erreurTechnique", NotificationsType.ERROR);
				}else{	
					this.setGroupesWorkspace(nuxeoCtl,wks, request, response);
					request.setAttribute("workspace", wks);		
				}
				if(wks.getGroupeAnimateurs()==null||wks.getGroupeContributeurs()==null||wks.getGroupeLecteurs()==null){
					retour="erreurGroupe";
				}else{
					retour = "presentationRolesWks";	
				}
				request.setAttribute("provenance","presentationRolesWks");
				PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
				request.setAttribute("allMembersURL", buildAllMembersUrl(wks, pcc));
				
			}
			
			PortalWindow window = WindowFactory.getWindow(request);
			String allMembers = window.getProperty("allMembers");
			
			Integer thumbnailPerLine = 4;
			if(BooleanUtils.toBoolean(allMembers)) {
				thumbnailPerLine = 6;
			}

			request.setAttribute("allMembers",BooleanUtils.toBoolean(allMembers));
			request.setAttribute("thumbnailPerLine",thumbnailPerLine);
			request.setAttribute("md",new Integer(12/thumbnailPerLine));
			request.setAttribute("sm",new Integer((12/thumbnailPerLine))*2);
			
			
			return retour;
		}
		
	}
	
	
	@RenderMapping(params = "action=nonAutorise")
	public String showNonAutorise(RenderRequest request) throws Exception {
	
		return "nonAutorise";
	}
	
	
	
	
	
	
	
	
	private void setGroupesWorkspace(NuxeoController nuxeoCtl, Workspace wks, PortletRequest request, PortletResponse response) throws Exception{
		
		PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
		
		wks.getListeAnimateurs().clear();
		wks.getListeContributeurs().clear();
		wks.getListeLecteurs().clear();
		wks.getMembresGroupeAnimateurs().clear();
		wks.getMembresGroupeContributeurs().clear();
		wks.getMembresGroupeLecteurs().clear();
		String prefixe="";
		Organisation org=null; 
		if(wks.getSourceOrganisationnelle()!=null&&!wks.getSourceOrganisationnelle().trim().isEmpty()){
			//prefixe=wks.getSourceOrganisationnelle()+"_";
			org = organisationInstance.findOrganisation(wks.getSourceOrganisationnelle());
		}
		
		
		String excludeUsers = config.getExcludeUsers();
		IDirectoryService directoryService = directoryServiceLocator.getDirectoryService();
		
		
		Profil p_animateur = profilInstance.findProfilByCn(prefixe+wks.getShortname()+config.getGroupAnimateurs());
		Profil p_contributeur = profilInstance.findProfilByCn(prefixe+wks.getShortname()+config.getGroupContributeurs());
		Profil p_lecteur = profilInstance.findProfilByCn(prefixe+wks.getShortname()+config.getGroupLecteurs());
		

		if(p_animateur!=null){
			wks.setGroupeAnimateurs(p_animateur);
			for(String dn : p_animateur.getListeMembers()){
				Person p = personneInstance.findPersonByDn(dn);
				DirectoryPerson person = directoryService.getPerson(p.getUid());
				// Pas d'affichage pour certains utilisateurx. Ex, les admin
				if(!excludeUsers.contains(p.getUid())) {
					wks.getMembresGroupeAnimateurs().add(new ParticipantUrl(p.getCn() ,this.buildPersonUrl(p, portalControllerContext), person.getAvatar()));
				}
			}
		}
		

		if(p_contributeur!=null){
			wks.setGroupeContributeurs(p_contributeur);
			for(String dn : p_contributeur.getListeMembers()){
				Person p = personneInstance.findPersonByDn(dn);
				DirectoryPerson person = directoryService.getPerson(p.getUid());
				// Pas d'affichage pour certains utilisateurx. Ex, les admin
				if(!excludeUsers.contains(p.getUid())) {
					wks.getMembresGroupeContributeurs().add(new ParticipantUrl(p.getCn() ,this.buildPersonUrl(p, portalControllerContext), person.getAvatar()));
				}
			}
		}
		

		if(p_lecteur!=null){
			wks.setGroupeLecteurs(p_lecteur);
			for(String dn : p_lecteur.getListeMembers()){
				Person p = personneInstance.findPersonByDn(dn);
				DirectoryPerson person = directoryService.getPerson(p.getUid());
				// Pas d'affichage pour certains utilisateurx. Ex, les admin
				if(!excludeUsers.contains(p.getUid())) {
					wks.getMembresGroupeLecteurs().add(new ParticipantUrl(p.getCn() ,this.buildPersonUrl(p, portalControllerContext), person.getAvatar()));
				}
			}
		}
		
		
		
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
							wks.getListeAnimateurs().add(new ParticipantUrl(prf.getDisplayName(),this.buildProfilUrl(prf, portalControllerContext)));
						}
						if(permission.equalsIgnoreCase("ReadWrite")){
							wks.getListeContributeurs().add(new ParticipantUrl(prf.getDisplayName(),this.buildProfilUrl(prf, portalControllerContext)));
						}
						if(permission.equalsIgnoreCase("Read")){
							wks.getListeLecteurs().add(new ParticipantUrl(prf.getDisplayName(),this.buildProfilUrl(prf, portalControllerContext)));
						}		
					}
					
				}else{
					if(!name.equalsIgnoreCase("administrators")){
						//Cas d'une personne
						Person p = personneInstance.findUtilisateur(name);
						if(p!=null){
							
							// Pas d'affichage pour certains utilisateurx. Ex, les admin
							if(!excludeUsers.contains(p.getUid())) {
								
								DirectoryPerson person = directoryService.getPerson(p.getUid());
							
								if(permission.equalsIgnoreCase("Everything")){
									wks.getMembresGroupeAnimateurs().add(new ParticipantUrl(p.getCn() ,this.buildPersonUrl(p, portalControllerContext), person.getAvatar() ));
									wks.getGroupeAnimateurs().addMember(p.getDn());
								}
								if(permission.equalsIgnoreCase("ReadWrite")){
									wks.getMembresGroupeContributeurs().add(new ParticipantUrl(p.getCn() ,this.buildPersonUrl(p, portalControllerContext), person.getAvatar()));
									wks.getGroupeContributeurs().addMember(p.getDn());
								}
								if(permission.equalsIgnoreCase("Read")){
									wks.getMembresGroupeLecteurs().add(new ParticipantUrl(p.getCn() ,this.buildPersonUrl(p, portalControllerContext), person.getAvatar()));
									wks.getGroupeLecteurs().addMember(p.getDn());
								}
							
							}
						}
					}
				}
			}
		}
	
	
	}
	
	private String buildPersonUrl(Person p, PortalControllerContext portalControllerContext) throws PortalException{
		
		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		windowProperties.put("osivia.title", p.getDisplayName());
		windowProperties.put("uidFichePersonne", p.getUid());
		return  getPortalUrlFactory().getStartPortletUrl(portalControllerContext, DirectoryPortlets.fichePersonne.getInstanceName(), windowProperties, false);
	}
	
	private String buildProfilUrl(Profil p, PortalControllerContext portalControllerContext) throws PortalException{
		
		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		windowProperties.put("osivia.title", p.getDisplayName());
		windowProperties.put("cnProfil", p.getCn());
		return  getPortalUrlFactory().getStartPortletUrl(portalControllerContext, DirectoryPortlets.ficheProfil.getInstanceName(), windowProperties, false);
	}
	
	private String buildAllMembersUrl(Workspace wks, PortalControllerContext portalControllerContext) throws PortalException{
		
		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.ajaxLink", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
		windowProperties.put("osivia.title", "Participants à "+wks.getNom());
		windowProperties.put("allMembers", "true");
		
		return getPortalUrlFactory().getStartPortletUrl(portalControllerContext, DirectoryPortlets.participantsWorkspace.getInstanceName(), windowProperties, false);
	}
	
	
	public void setPortletConfig(PortletConfig portletConfig) {
		this.portletConfig = portletConfig;
	}

	public void setPortletContext(PortletContext portletContext) {
		this.portletContext = portletContext;
	}
	
	
	
}
