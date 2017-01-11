/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com)
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
 */
package org.osivia.directory.v2.repository;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Get a document representing the user profile
 */
public class GetUserProfileCommand  implements INuxeoCommand{
	
	private String username;

	public GetUserProfileCommand(String username) {
		this.username = username;
	}

	/**
	 *
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
	
		OperationRequest newRequest = automationSession.newRequest("Services.GetToutaticeUserProfile");
		if (username != null) {
			newRequest.set("username", username);
		}

		return newRequest.execute();
		
	}

	public String getId() {
		return "GetUserProfileCommand/" + username;
	}
	

}
