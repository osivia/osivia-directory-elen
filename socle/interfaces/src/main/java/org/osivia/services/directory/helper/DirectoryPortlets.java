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

    fichePersonne("directory-person-card-instance"),

    ficheProfil("directory-group-card-instance"),

    gestionPersonnes("directory-person-management-instance"),

    gestionGroupes("directory-group-management-instance"),

    creationPersonne("directory-person-creation-instance"),

    gestionWorkspaces("directory-workspace-management-instance"),

    participantsWorkspace("directory-workspace-participants-instance");


	private final String instanceName;

	DirectoryPortlets(String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return this.instanceName;
	}

}
