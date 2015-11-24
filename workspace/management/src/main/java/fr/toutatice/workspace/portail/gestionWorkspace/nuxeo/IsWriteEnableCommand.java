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

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.springframework.beans.factory.annotation.Autowired;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class IsWriteEnableCommand  implements INuxeoCommand{
	
	
	protected static final Log logger = LogFactory.getLog("fr.toutatice.identite");

	private String path;
	private Person user;
	private Profil profil;
	
	public IsWriteEnableCommand(String path, Person user, Profil profil) {
		super();
		this.path = path;
		this.user = user;
		this.profil = profil;
	}

	/**
	 * 
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
		
		Blob result = null;
		OperationRequest request = automationSession.newRequest("Document.GetUsersAndGroupsDocument");
		PathRef path = new PathRef(this.path);
		request.setInput(path);
		result = (Blob) request.execute();
		Boolean writeEnable = false;
		
		if (result != null)
		{
			String content = IOUtils.toString(result.getStream());
			JSONArray rows = JSONArray.fromObject(content);
	
			@SuppressWarnings("rawtypes")
			Iterator it = rows.iterator();
						
			while (it.hasNext()&&!writeEnable) {
				JSONObject obj = (JSONObject) it.next();
				String name = (String) obj.get("userOrGroup");
				if(name.equalsIgnoreCase(user.getUid())){
					String permission = (String) obj.get("permission");
					if(permission.equalsIgnoreCase("Everything")||permission.equalsIgnoreCase("Write")||permission.equalsIgnoreCase("ReadWrite")){
						writeEnable=true;
					}
				}
				if(!writeEnable){
					String dnProfil = profil.findFullDn(name);
					if(dnProfil!=null && user.hasProfil(dnProfil)){
						String permission = (String) obj.get("permission");
						if(permission.equalsIgnoreCase("Everything")||permission.equalsIgnoreCase("Write")||permission.equalsIgnoreCase("ReadWrite")){
							writeEnable=true;
						}
					}
				}
				
			}
			
		}
		
		return writeEnable;
		
	}
	
	public String getId() {
		return "GetGroupsDocumentCommand";
	}
	
	
}
