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


package fr.toutatice.workspace.portail.gestionWorkspace.nuxeo;




import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.internationalization.Bundle;
import org.springframework.context.ApplicationContext;

import fr.toutatice.outils.ldap.entity.Organisation;
import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.GestionWorkspacesConfig;
import fr.toutatice.workspace.portail.gestionWorkspace.Workspace;


public class CreateWorkspaceCommand  implements INuxeoCommand{
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");

	private static final String ACE_DELIMITER = ",";
    private static final String ACE_FORMAT = "%s:%s:%s";

	private Workspace workspace;
	private Person userConnecte;
	private Profil profil;
	private Organisation organisation;


	private ApplicationContext springCtx;
	private Bundle bundle;
	
	public CreateWorkspaceCommand(ApplicationContext springCtx, Bundle bundle, Workspace wks, Person userConnecte, Profil profil, Organisation org) {
		super();
		this.springCtx = springCtx;
		this.bundle = bundle;
		this.workspace=wks;
		this.userConnecte = userConnecte;
		this.profil = profil;
		this.organisation = org;
	}
	
	/**
	 * execution d'une requete nuxeo permettant de créér un document contenant la participation au projet refondons l'école
	 * @return 
	 * @throws Exception 
	 */
	public Object execute(Session automationSession) throws Exception   {
		

		GestionWorkspacesConfig config = springCtx.getBean(GestionWorkspacesConfig.class);
		
		// Récupération dossier dans lequel le workspace doit être créé
		Document root = (Document) automationSession.newRequest("Document.Fetch").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", config.getPathOpenWorkspaceDirectory()).execute();
		
		if(root==null){
			return null;
		}else{
			
			
			PropertyMap props = new PropertyMap();
			//props.set("acr:auteur", userConnecte.getUid());
			props.set("dc:title",workspace.getNom());
			props.set("dc:description",workspace.getDescription());//description
			props.set("dc:publisher",workspace.getSourceOrganisationnelle());
			//props.set("ttc:theme", workspace.getTheme());
			//props.set("ttc:pageTemplate","/default/templates/espace-num-etab-ws");
			props.set(config.getWsShortName(), workspace.getShortname());
		
			DocumentService rs = automationSession.getAdapter(DocumentService.class);
			Document doc;
			
			
			// Si un template de WS est défini, copie de ce template et application des properties
			if(config.getPathTemplateWorkspace() != null) {
				doc = rs.copy(new DocRef(config.getPathTemplateWorkspace()), new DocRef(config.getPathOpenWorkspaceDirectory()), workspace.getShortname());
				doc = rs.update(doc, props);
			}
			else {
				
				// Sinon création simple du WS
				doc = rs.createDocument(root, "Workspace", workspace.getShortname(), props);
			}
			

			
			String roleAnim = bundle.getString("label.animateur");
			
			Profil p1 = profil.getNewProfil(workspace.getShortname()+config.getGroupAnimateurs(), roleAnim+" "+workspace.getNom(), roleAnim+" "+workspace.getNom(), 
					"space-group", Profil.typePeuplement.EXPLICITE);
			p1.addMember(userConnecte.getDn());
			p1.addExplicitManager(p1.getDn());
			p1.addExplicitManager(userConnecte.getDn());
			try {
				p1.create();
			} catch (ToutaticeAnnuaireException e) {
				rs.remove(doc);
				throw e;
			}
			
			String roleContrib = bundle.getString("label.contributeur");
			
			Profil p2 = profil.getNewProfil(workspace.getShortname()+config.getGroupContributeurs(), roleContrib+" "+workspace.getNom(), roleContrib+" "+workspace.getNom(), 
					"space-group", Profil.typePeuplement.EXPLICITE);
			p2.addMember(userConnecte.getDn());
			p2.addExplicitManager(p1.getDn());
			p2.addExplicitManager(userConnecte.getDn());
			try {
				p2.create();
			} catch (ToutaticeAnnuaireException e) {
				rs.remove(doc);
				p1.delete();
				throw e;
			}
			
			Profil p3=null;
			if(config.getEnableReaders()) {
				String roleLecteur = bundle.getString("label.lecteur");
			
				p3 = profil.getNewProfil(workspace.getShortname()+config.getGroupLecteurs(), roleLecteur+" "+workspace.getNom(), roleLecteur+" "+workspace.getNom(), 
						"space-group", Profil.typePeuplement.EXPLICITE);
				p3.addMember(userConnecte.getDn());
				p3.addExplicitManager(p1.getDn());
				p3.addExplicitManager(userConnecte.getDn());
				
				try {
					p3.create();
				} catch (ToutaticeAnnuaireException e) {
					rs.remove(doc);
					p2.delete();
					p1.delete();
					throw e;
				}
			}
			
			
			//rattachement de ces groupes à une organisation le cas échéant
			Organisation org = organisation.findOrganisation(workspace.getSourceOrganisationnelle());
			if(org!=null){
				if(p1!=null){
					org.addProfil(p1.getDn());
				}
				if(p2!=null){
					org.addProfil(p2.getDn());
				}
				if(p3!=null){
					org.addProfil(p3.getDn());
				}
				org.update();
			}
			
			
			
			//Pose des droits nuxeo sur les groupes ldap
			
			try {
				 final List<String> entries = new ArrayList<String>();
		         entries.add(String.format(ACE_FORMAT, "Administrators", "Everything", true));
		         entries.add(String.format(ACE_FORMAT, p1.getCn(), "Everything", true));
		         entries.add(String.format(ACE_FORMAT, p2.getCn(), "ReadWrite", true));
		         if(p3!=null) {
		        	 entries.add(String.format(ACE_FORMAT, p3.getCn(), "Read", true));
		         }
		          
				doc = (Document) automationSession.newRequest("Document.SetACL").setInput(doc).set("acl","local").set("overwrite", true).set("break", true).set("entries",  StringUtils.join(entries, ACE_DELIMITER)).execute();

				
				
				
			} catch (Exception e) {
				rs.remove(doc);
				if(p1!=null){
					p1.delete();
				}
				if(p2!=null){
					p2.delete();
				}
				if(p3!=null){
					p3.delete();
				}
				throw e;
			}
		
			return doc;
		}
	}

	public String getId() {
		return "CreateWorkspaceNuxeoCommand";
	}
	

}
