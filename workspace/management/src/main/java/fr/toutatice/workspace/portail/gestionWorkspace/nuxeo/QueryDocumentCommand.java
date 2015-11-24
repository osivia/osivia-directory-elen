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
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


public class QueryDocumentCommand implements INuxeoCommand{

	protected static final Log logger = LogFactory.getLog("fr.toutatice.services.organisation");


	
	private String nuxeoRequest;

	public QueryDocumentCommand(String nuxeoRequest) {
		super();
		this.nuxeoRequest = nuxeoRequest;
	
	}
	
	
	

	/**
	 * execution d'une requete nuxéo permettant de récupérer les paramètres du formulaire de contact dans le document Nuxeo
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
		
		
		Documents result = (Documents) automationSession.newRequest("Document.Query").setHeader(Constants.HEADER_NX_SCHEMAS, "*")
				.set("query", "SELECT * FROM Document WHERE " +nuxeoRequest).execute();
		 
		return result;
	
	}

	public String getId() {
		return "FetchNuxeoCommand";
	}
	

	
}
