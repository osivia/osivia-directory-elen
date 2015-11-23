/*
 * (C) Copyright 2014 OSIVIA (http://www.osivia.com) 
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
package org.osivia.services.directory.helper;

/**
 * List all known identity portlets
 * @author lbillon
 *
 */
public enum DirectoryPortlets {

	fichePersonne("toutatice-identite-fichepersonne-portailPortletInstance"), ficheProfil("toutatice-identite-ficheprofil-portailPortletInstance"), gestionPersonnes(
			"toutatice-identite-gestionpersonnes-portailPortletInstance"), gestionGroupes("toutatice-identite-gestiongroupes-portailPortletInstance"), creationPersonne(
			"toutatice-identite-creationpersonnes-portailPortletInstance"),ficheApplication("toutatice-application-ficheapplication-portailPortletInstance"),
			ficheOrganisation("toutatice-organisation-ficheorganisation-portailPortletInstance"),gestionWorkspaces("toutatice-workspace-gestionworkspace-portailPortletInstance");

	private final String instanceName;

	DirectoryPortlets(String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return instanceName;
	}

}
