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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.osivia.directory.v2.model.ext.Avatar;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Command used to update a user profile (avatar and properties).
 * @author Loïc Billon
 *
 */
public class UpdateUserProfileCommand implements INuxeoCommand {

	private Document userProfile;
	
	private Map<String, String> propertiesToUpdate;
	
	private Avatar avatar;
	
	public UpdateUserProfileCommand(Document userProfile, Map<String, String> propertiesToUpdate, Avatar avatar) {
		this.userProfile = userProfile;
		this.propertiesToUpdate = propertiesToUpdate;
		this.avatar = avatar;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {

		
		// Update avatar blob
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        if(avatar.isDeleted()) {
        	documentService.removeBlob(new DocRef(userProfile.getId()), "userprofile:avatar");
        }
        
        if(avatar.isUpdated()) {
        	documentService.setBlob(new DocRef(userProfile.getId()), new FileBlob(avatar.getTemporaryFile()), "userprofile:avatar");
        }
        
		// Update properties
		OperationRequest majFicheProfil = nuxeoSession.newRequest("Document.Update").setInput(userProfile);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> property : propertiesToUpdate.entrySet()) {
        	if(StringUtils.isNotBlank(property.getValue())) {
	            sb.append(property.getKey());
	            sb.append("=");
	            sb.append(property.getValue());
	            sb.append("\n");
        	}
        }

		majFicheProfil.set("properties",sb.toString());

		return majFicheProfil.execute();
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return null;
	}

}
