package org.osivia.services.person.card.portlet.model;

/**
 * Password edition modes enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum PersonPasswordEditionMode {

    /** Deny edition. */
    DENY(false),
    /** Allow edition. */
    ALLOW(true),
    /** Overwrite. */
    OVERWRITE(true);


    /** Editable password indicator. */
    private final boolean editable;


    /**
     * Constructor.
     * 
     * @param editable editable password indicator
     */
    private PersonPasswordEditionMode(boolean editable) {
        this.editable = editable;
    }


    /**
     * Getter for editable.
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

}
