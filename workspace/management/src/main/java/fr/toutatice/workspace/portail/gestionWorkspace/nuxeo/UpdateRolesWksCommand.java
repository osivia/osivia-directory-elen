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
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.workspace.portail.gestionWorkspace.Workspace;

public class UpdateRolesWksCommand  implements INuxeoCommand{
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.services");


	private Workspace workspace;
	
	private static final String ACE_DELIMITER = ",";
    private static final String ACE_FORMAT = "%s:%s:%s";

	
	public UpdateRolesWksCommand(Workspace wks) {
		super();
		this.workspace=wks;
		
	}
	

	public Object execute(Session automationSession) throws Exception {
		
		
		// Récupération workspace
		Document doc = (Document) automationSession.newRequest("Document.Fetch").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", workspace.getPath()).execute();
		 
		if(doc==null){
			return null;
		}else{
			
			
		}
		
		
		 final List<String> entries = new ArrayList<String>();
         entries.add(String.format(ACE_FORMAT, "Administrators", "Everything", true));
         entries.add(String.format(ACE_FORMAT, workspace.getGroupeAnimateurs().getCn(), "Everything", true));
         entries.add(String.format(ACE_FORMAT, workspace.getGroupeContributeurs().getCn(), "ReadWrite", true));
         if(workspace.getGroupeLecteurs() != null) {
        	 entries.add(String.format(ACE_FORMAT, workspace.getGroupeLecteurs().getCn(), "Read", true));
         }
         
         for(Profil prf:workspace.getListeProfilAnimateurs()){
				entries.add(String.format(ACE_FORMAT, prf.getCn(), "Everything", true));
			}
		
			for(Profil prf:workspace.getListeProfilContributeurs()){
				entries.add(String.format(ACE_FORMAT, prf.getCn(), "ReadWrite", true));
			}
		
			for(Profil prf:workspace.getListeProfilLecteurs()){
				entries.add(String.format(ACE_FORMAT, prf.getCn(), "Read", true));
			}

	       
		return automationSession.newRequest("Document.SetACL").setInput(doc).set("acl","local").set("overwrite", true).set("break", true).set("entries",  StringUtils.join(entries, ACE_DELIMITER)).execute();

	
	}

	
	

	public String getId() {
		return "CreateWorkspaceNuxeoCommand";
	}
	
	
	
	

}
