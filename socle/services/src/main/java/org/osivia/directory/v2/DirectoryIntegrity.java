/*
 * (C) Copyright 2017 OSIVIA (http://www.osivia.com) 
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
 */
package org.osivia.directory.v2;

import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.lang.BooleanUtils;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.batch.AbstractBatch;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;

/**
 * Batch de cohérence des données
 * @author Loïc Billon
 *
 */
public class DirectoryIntegrity extends AbstractBatch {


    /** Portlet context. */
    private static PortletContext portletContext;
    
	
	private static final String LDAP_CHECK_INTEGRITY = "ldap.check.integrity.enabled";

	private static final String LDAP_CHECK_INTEGRITY_CRON = "ldap.check.integrity.cron";
	
	private final boolean enabled;
	
	public DirectoryIntegrity() {
		if(System.getProperty(LDAP_CHECK_INTEGRITY) != null && BooleanUtils.toBoolean(System.getProperty(LDAP_CHECK_INTEGRITY))) {
			enabled = true;
		}
		else enabled = false;
	}
	
	
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isRunningOnMasterOnly() {
		return true;
	}

	@Override
	public String getJobScheduling() {
		return System.getProperty(LDAP_CHECK_INTEGRITY_CRON);
	}

	@Override
	public void execute(Map<String, Object> parameters) {
		

		WorkspaceService service = DirServiceFactory.getService(WorkspaceService.class);
		
		// TODO à compléter
		String workspaceId = "espace-de-test";
		
		if(parameters.get("workspaceId") != null) {
			workspaceId = (String) parameters.get("workspaceId");
		}
		
		boolean errorsDetected = service.checkGroups(workspaceId);
		
		if(errorsDetected) {
			
			service.sendIntegrityAlert(new PortalControllerContext(portletContext, null, null));
			
		}
		
	}



	public void setPortletContext(PortletContext portletContext) {
		DirectoryIntegrity.portletContext = portletContext;
	}

	
}
