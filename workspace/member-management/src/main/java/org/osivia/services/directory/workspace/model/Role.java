package org.osivia.services.directory.workspace.model;

import org.apache.commons.lang.StringUtils;

/**
 * Workspace roles enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum Role {

    /** Administrator. */
    ADMIN(5),
    /** Owner. */
    OWNER(4),
    /** Editor. */
    EDITOR(3),
    /** Authors. */
    AUTHOR(2),
    /** Member. */
    MEMBER(1);


    /** Default role. */
    public static final Role DEFAULT = MEMBER;


    /** Weight. */
    private final int weight;
    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     * 
     * @param weight weight
     */
    private Role(int weight) {
        this.weight = weight;
        this.key = "ROLE_" + StringUtils.upperCase(this.name());
    }


    /**
     * Get role from his name.
     * 
     * @param name role name
     * @return role
     */
    public static Role fromName(String name) {
        Role result = DEFAULT;
        for (Role value : Role.values()) {
            if (value.name().equals(name)) {
                result = value;
                break;
            }
        }
        return result;
    }


    /**
     * Get role name.
     * 
     * @return role name
     */
    public String getName() {
        return this.name();
    }

    /**
     * Getter for weight.
     * 
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

}
