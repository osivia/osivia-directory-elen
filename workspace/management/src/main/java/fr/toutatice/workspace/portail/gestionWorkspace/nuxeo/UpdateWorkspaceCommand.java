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




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.bean.FormModifWks;


public class UpdateWorkspaceCommand  implements INuxeoCommand{
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");


	private String path;
	private FormModifWks formulaire;


	
	public UpdateWorkspaceCommand(FormModifWks form, String path) {
		super();
		this.formulaire=form;
		this.path=path;

	}
	


	/**
	 * execution d'une requete nuxeo permettant de créér un document contenant la participation au projet refondons l'école
	 * @return 
	 * @throws Exception 
	 */
	public Object execute(Session automationSession) throws Exception   {
		
		// Récupération workspace
		Document doc = (Document) automationSession.newRequest("Document.Fetch").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", path).execute();
		 
		if(doc==null){
			return null;
		}else{
			DocumentService rs = automationSession.getAdapter(DocumentService.class);
			
			
			PropertyMap props = new PropertyMap();
			props.set("dc:title",formulaire.getNom());
	
			props.set("dc:description",formulaire.getDescription());

//			if(formulaire.getTheme()!=null){
//				props.set("ttc:theme", formulaire.getTheme());
//			}

			rs.update(doc, props);

			return doc;
		}
	}

	public String getId() {
		return "CreateWorkspaceNuxeoCommand";
	}
	
	
	
	

}
