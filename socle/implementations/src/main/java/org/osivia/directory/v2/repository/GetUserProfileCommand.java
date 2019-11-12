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

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get user profile Nuxeo command.
 *
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetUserProfileCommand  implements INuxeoCommand{

    /** Username. */
    private String username;


    /**
     * Constructor.
     *
     * @param username username
     */
    public GetUserProfileCommand(String username) {
        this.username = username;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session automationSession) throws Exception {
        // Operation request
        OperationRequest newRequest = automationSession.newRequest("Services.GetToutaticeUserProfile");
        if (this.username != null) {
            newRequest.set("username", this.username);
        }
        newRequest.setHeader(Constants.HEADER_NX_SCHEMAS, "*");

        return newRequest.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.getClass().getSimpleName() + "/" + this.username;
    }

}
