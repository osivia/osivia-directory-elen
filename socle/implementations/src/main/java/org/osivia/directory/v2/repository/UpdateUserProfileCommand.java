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
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.ext.Avatar;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Command used to update a user profile (avatar and properties).
 *
 * @author Loïc Billon
 * @see INuxeoCommand
 */
public class UpdateUserProfileCommand implements INuxeoCommand {

    /** User profile Nuxeo document. */
    private Document userProfile;
    /** Nuxeo document properties. */
    private Map<String, String> properties;
    /** Avatar */
    private Avatar avatar;


    /**
     * Constructor.
     *
     * @param userProfile user profile Nuxeo document
     * @param properties Nuxeo document properties to update
     * @param avatar avatar
     */
    public UpdateUserProfileCommand(Document userProfile, Map<String, String> properties, Avatar avatar) {
        this.userProfile = userProfile;
        this.properties = properties;
        this.avatar = avatar;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Property map
        PropertyMap propertyMap = new PropertyMap(this.properties.size());
        for (Entry<String, String> entry : this.properties.entrySet()) {
            propertyMap.set(entry.getKey(), StringUtils.trimToEmpty(entry.getValue()));
        }

        // Update properties
        this.userProfile = documentService.update(this.userProfile, propertyMap);

        // Update avatar BLOB
        if (this.avatar != null) {
            if (this.avatar.isUpdated()) {
                documentService.setBlob(this.userProfile, new FileBlob(this.avatar.getTemporaryFile()), "userprofile:avatar");
            } else if (this.avatar.isDeleted()) {
                documentService.removeBlob(this.userProfile, "userprofile:avatar");
            }
        }

        return this.userProfile;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
