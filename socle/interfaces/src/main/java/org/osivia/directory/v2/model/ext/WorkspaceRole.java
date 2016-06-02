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
 * Role in a workspace (means rights on the root element)
 * @author Loïc Billon
 * @since 4.4
 */
public enum WorkspaceRole {

	/** Owner, as all rights on all elemnts */
	owner, 
	
	/** Administrator, has all rights on a part or all elemnts */
	admin, 
	
	/** can read and write all documents */
	writer, 
	
	/** can write only his documents */
	contributor, 
	
	/** no rights except reading */
	reader;
	
}
