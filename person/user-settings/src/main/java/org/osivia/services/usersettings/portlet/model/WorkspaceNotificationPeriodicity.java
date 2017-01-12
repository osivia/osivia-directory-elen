package org.osivia.services.usersettings.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Workspace notification periodities enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum WorkspaceNotificationPeriodicity {

    /** No periodicity. */
    NONE,
    /** Daily. */
    DAILY,
    /** Weekly. */
    WEEKLY;


    /** Default value. */
    public static final WorkspaceNotificationPeriodicity DEFAULT = WorkspaceNotificationPeriodicity.NONE;


    /** Periodicity identifier. */
    private final String id;
    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     * 
     * @param id
     */
    private WorkspaceNotificationPeriodicity() {
        this.id = StringUtils.lowerCase(this.name());
        this.key = "USER_SETTINGS_PERIODICITY_" + StringUtils.upperCase(this.name());
    }


    /**
     * Get periodicity from identifier.
     * 
     * @param id periodicity identifier
     * @return periodicity
     */
    public static WorkspaceNotificationPeriodicity fromId(String id) {
        WorkspaceNotificationPeriodicity result = DEFAULT;

        for (WorkspaceNotificationPeriodicity value : WorkspaceNotificationPeriodicity.values()) {
            if (value.id.equals(id)) {
                result = value;
                break;
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
        return id;
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
