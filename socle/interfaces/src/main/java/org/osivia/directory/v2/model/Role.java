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
package org.osivia.directory.v2.model;

import java.util.List;

import javax.naming.Name;

/**
 * LDAP applicative role used to manage authorization on using a function
 * @author Loïc Billon
 * @since 4.4
 */
public interface Role {

	/**
	 * @return
	 */
	Name getDn();

	/**
	 * @param dn
	 */
	void setDn(Name dn);

	/**
	 * @return
	 */
	String getCn();

	/**
	 * @param cn
	 */
	void setCn(String cn);

	/**
	 * @return
	 */
	String getDescription();

	/**
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * @return
	 */
	List<Name> getUniqueMember();

	/**
	 * @param uniqueMembers
	 */
	void setUniqueMember(List<Name> uniqueMember);

	/**
	 * @param uid
	 * @return
	 */
	Name buildDn(String cn);

	/**
	 * @return
	 */
	Name buildBaseDn();

}
