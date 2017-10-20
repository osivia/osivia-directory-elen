package org.osivia.services.person.card.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Person titles enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum PersonTitle {
    
    /** Mr. */
    MR,
    /** Mrs */
    MRS;
    

    /** Internationalization key prefix. */
    private static final String KEY_PREFIX = "PERSON_CARD_TITLE_";


    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     */
    private PersonTitle() {
        this.key = KEY_PREFIX + StringUtils.upperCase(this.name());
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
