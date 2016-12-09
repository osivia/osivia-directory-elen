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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Role in a workspace (means rights on the root element).
 *
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @since 4.4
 */
public enum WorkspaceRole {

    /** Owner, as all rights on all elements. */
    OWNER(5, "Everything"),
    /** Administrator, has all rights on a part or all elements. */
    ADMIN(4, "Everything"),
    /** Can read and write all documents. */
    WRITER(3, "ReadWrite"),
    /** Can write only his documents. */
    CONTRIBUTOR(2, "Read", "WriteModifyOwnOnly"),
    /** No rights except reading. */
    READER(1, "Read");


    /** Identifier. */
    private final String id;
    /** Weight. */
    private final int weight;
    /** Permissions. */
    private final String[] permissions;
    /** Internationalization key. */
    private final String key;
    /** Class loader, user for internationalization resources. */
    private final ClassLoader classLoader;


    /**
     * Constructor.
     *
     * @param weight weight
     * @param permissions permissions
     */
    private WorkspaceRole(int weight, String... permissions) {
        this.id = StringUtils.lowerCase(this.name());
        this.weight = weight;
        this.permissions = permissions;
        this.key = "WORKSPACE_ROLE_" + StringUtils.upperCase(this.name());
        this.classLoader = this.getClass().getClassLoader();
    }


    /**
     * Get workspace role from its identifier.
     *
     * @param id identifier
     * @return workspace role
     */
    public static WorkspaceRole fromId(String id) {
        WorkspaceRole result = null;

        for (WorkspaceRole value : WorkspaceRole.values()) {
            if (value.id.equals(id)) {
                result = value;
                break;
            }
        }

        return result;
    }


    /**
     * Get workspace role from its permissions.
     *
     * @param permissions permissions
     * @return workspace role
     */
    public static WorkspaceRole fromPermissions(String... permissions) {
        WorkspaceRole result = null;

        for (WorkspaceRole value : WorkspaceRole.values()) {
            if (!WorkspaceRole.OWNER.equals(value) && ((result == null) || (value.weight > result.weight))) {
                // Check if value matches permissions
                boolean matches = true;
                for (String permission : value.getPermissions()) {
                    if (!ArrayUtils.contains(permissions, permission)) {
                        matches = false;
                        break;
                    }
                }

                if (matches) {
                    result = value;
                }
            }
        }

        return result;
    }


    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for weight.
     *
     * @return the weight
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Getter for permissions.
     *
     * @return the permissions
     */
    public String[] getPermissions() {
        return this.permissions;
    }

    /**
     * Getter for key.
     *
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Getter for classLoader.
     *
     * @return the classLoader
     */
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

}
