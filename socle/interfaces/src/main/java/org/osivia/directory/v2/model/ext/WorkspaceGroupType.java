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
package org.osivia.directory.v2.model.ext;

/**
 * Groups type in a workspace
 * @author Loïc Billon
 * @since 4.4
 */
public enum WorkspaceGroupType {

	/** All members of a workspace */
	space_group, 
	
	/** Security group mapped with ACL on the root element */
	security_group, 
	
	/** Custom group defined for special rights on an element */
	local_group, 
	
	/** Group used for members who are waiting for grant access to the workspace */
	pending_group;
}
