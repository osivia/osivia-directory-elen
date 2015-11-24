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
package fr.toutatice.workspace.portail.participantsWorkspace.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;




@Service
@Scope("prototype, request")
public class FormAdmin {

	
	String nuxeopath="";
	
	public String getNuxeopath() {
		return nuxeopath;	}

	public void setNuxeopath(String nuxeopath) {
		this.nuxeopath = nuxeopath;	}


	
	
}
